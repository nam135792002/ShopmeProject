package com.shopme;

import com.shopme.security.oauth.CustomerOauth2User;
import com.shopme.setting.EmailSettingBag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import java.util.Properties;

public class Utility {
    public static String getSiteURL(HttpServletRequest request){
        String siteURL = request.getRequestURL().toString();

        return siteURL.replace(request.getServletPath(),"");
    }

    public static JavaMailSenderImpl prepareMailSender(EmailSettingBag settings){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(settings.getHost());
        mailSender.setPort(settings.getPort());
        mailSender.setUsername(settings.getUsername());
        mailSender.setPassword(settings.getPassword());

        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth",settings.getAuth());
        properties.setProperty("mail.smtp.starttls.enable",settings.getSecured());
        mailSender.setJavaMailProperties(properties);
        return mailSender;
    }

    public static String getEmailOfAuthenticatedCustomer(HttpServletRequest request){
        Object principal = request.getUserPrincipal();
        if(principal == null) return null;
        String customerEmail = null;

        if(principal instanceof UsernamePasswordAuthenticationToken || principal instanceof RememberMeAuthenticationToken){
            customerEmail = request.getUserPrincipal().getName();
        } else if (principal instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) principal;
            CustomerOauth2User oauth2User = (CustomerOauth2User) oAuth2AuthenticationToken.getPrincipal();
            customerEmail = oauth2User.getEmail();
        }
        return customerEmail;
    }
}
