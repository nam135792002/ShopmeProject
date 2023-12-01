package com.shopme.customer;

import com.shopme.Utility;
import com.shopme.common.entity.Customer;
import com.shopme.setting.EmailSettingBag;
import com.shopme.setting.SettingService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.UnsupportedEncodingException;

@Controller
public class ForgotPasswordController {
    @Autowired private CustomerService  customerService;
    @Autowired private SettingService settingService;

    @GetMapping("/forgot_password")
    public String showRequestForm(){
        return "forgot_password_form";
    }

    @PostMapping("/forgot_password")
    public String processRequestForm(HttpServletRequest request, Model model)  {
        String email = request.getParameter("email");
        try {
            String token = customerService.updateResetPasswordToken(email);
            String link = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email,link);
            model.addAttribute("message","We have sent a reset password link to your email." +
                    " Please check.");
        } catch (CustomerNotFoundException e) {
            model.addAttribute("error",e.getMessage());
        } catch(MessagingException | UnsupportedEncodingException e){
            model.addAttribute("error","Could not send match");
        }

        return "/forgot_password_form";
    }

    private void sendEmail(String email, String link) throws MessagingException, UnsupportedEncodingException {
        EmailSettingBag emailSettingBag = settingService.getEmailSettings();
        JavaMailSenderImpl mailSender = Utility.prepareMailSender(emailSettingBag);

        String toAddress = email;
        String subject = "Here's the link to reset your password";

        String content = "<p>Hello,</p>" +
                "<p>You have requested to reset your password.</p>" +
                "<p>Click the link below to change your password:</p>" +
                "<p><a href=\""+link+"\">Change my password</a></p>" +
                "<br>" +
                "<p>Ignore this email if you do remember your password, " +
                "or you have not made the request.</p>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSettingBag.getForm(),emailSettingBag.getSenderName());
        helper.setTo(toAddress);
        helper.setSubject(subject);
        helper.setText(content,true);
        mailSender.send(message);
    }

    @GetMapping("/reset_password")
    public String showResetForm(@Param("token") String token, Model model){
        Customer customer = customerService.getByResetPasswordToken(token);
        if(customer != null){
            model.addAttribute("token",token);
        } else{
            model.addAttribute("pageTitle","Invalid Token");
            model.addAttribute("message","Invalid Token");
            return "message";
        }
        return "reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetForm(HttpServletRequest request, Model model){
        String token = request.getParameter("token");
        String password = request.getParameter("password");

        try {
            customerService.updatePassword(token,password);
            model.addAttribute("message","You have successfully changed your password.");
            model.addAttribute("title","Reset your password");
            model.addAttribute("pageTitle","Reset password");
            return "message";
        } catch (CustomerNotFoundException e) {
            model.addAttribute("pageTitle","Invalid Token");
            model.addAttribute("message",e.getMessage());
            return "message";
        }
    }
}
