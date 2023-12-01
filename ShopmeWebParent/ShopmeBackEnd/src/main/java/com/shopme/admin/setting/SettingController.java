package com.shopme.admin.setting;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shopme.common.entity.Currency;
import com.shopme.common.entity.Setting;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class SettingController {
    @Autowired private SettingService settingService;

    @Autowired private CurrencyRepository currencyRepository;

    @Autowired private Cloudinary cloudinary;

    @GetMapping("/settings")
    public String listAll(Model model){
        List<Setting> listSettings = settingService.listALlSettings();
        List<Currency> listCurrencies = currencyRepository.findAllByOrderByNameAsc();
        model.addAttribute("listCurrencies",listCurrencies);
        for(Setting setting : listSettings){
            model.addAttribute(setting.getKey(),setting.getValue());
        }
        return "settings/settings";
    }

    @PostMapping("/settings/save_general")
    public String saveGeneralSettings(@RequestParam("fileImage")MultipartFile multipartFile,
                                      HttpServletRequest request, RedirectAttributes redirectAttributes) throws IOException {
        GeneralSettingBag settingBag = settingService.getGeneralSettings();
        List<Setting> listSettings = settingBag.list();

        if(!multipartFile.isEmpty()){
            String oldUrl = settingBag.getValue("SITE_LOGO");
            settingService.deleteImageInCloudinary(oldUrl);
            Map r = this.cloudinary.uploader().upload(multipartFile.getBytes(),
                    ObjectUtils.asMap("resource_type","auto"));
            String img = (String) r.get("secure_url");
            settingBag.updateSiteLogo(img);
        }

        Integer currencyId = Integer.parseInt(request.getParameter("CURRENCY_ID"));
        Optional<Currency>  findByIdResult = currencyRepository.findById(currencyId);
        if(findByIdResult.isPresent()){
            Currency currency = findByIdResult.get();
            settingBag.updateCurrencySymbol(currency.getSymbol());
        }

        for(Setting setting : listSettings){
            String value = request.getParameter(setting.getKey());
            if(value != null){
                setting.setValue(value);
            }
        }
        settingService.saveAll(listSettings);
        redirectAttributes.addFlashAttribute("message","General settings have saved");
        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_server")
    public String saveMailServerSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){
        List<Setting> mailServerSettings = settingService.getMailServerSettings();
        for(Setting setting : mailServerSettings){
            String value = request.getParameter(setting.getKey());
            if(value != null){
                setting.setValue(value);
            }
        }
        settingService.saveAll(mailServerSettings);
        redirectAttributes.addFlashAttribute("message","Mail server settings have been saved");
        return "redirect:/settings";
    }

    @PostMapping("/settings/save_mail_templates")
    public String saveMailTemplateSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){
        List<Setting> mailTemplateSettings = settingService.getEmailTemplateSettings();
        for(Setting setting : mailTemplateSettings){
            String value = request.getParameter(setting.getKey());
            if(value != null){
                setting.setValue(value);
            }
        }
        settingService.saveAll(mailTemplateSettings);
        redirectAttributes.addFlashAttribute("message","Mail server settings have been saved");
        return "redirect:/settings";
    }

    @PostMapping("/settings/save_payment")
    public String savePaymentSettings(HttpServletRequest request, RedirectAttributes redirectAttributes){
        List<Setting> paymentSettings = settingService.getPaymentSetting();
        for(Setting setting : paymentSettings){
            String value = request.getParameter(setting.getKey());
            if(value != null){
                setting.setValue(value);
            }
        }
        settingService.saveAll(paymentSettings);
        redirectAttributes.addFlashAttribute("message","Payment settings have been saved");
        return "redirect:/settings";
    }
}
