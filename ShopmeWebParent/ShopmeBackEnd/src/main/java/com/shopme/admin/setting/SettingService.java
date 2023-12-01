package com.shopme.admin.setting;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SettingService {
    @Autowired private SettingRepository settingRepository;
    @Autowired private Cloudinary cloudinary;

    public List<Setting> listALlSettings(){
        return settingRepository.findAll();
    }

    public GeneralSettingBag getGeneralSettings(){
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);

        return new GeneralSettingBag(settings);
    }

    public void saveAll(Iterable<Setting> settings){
        settingRepository.saveAll(settings);
    }

    public void deleteImageInCloudinary(String oldUrl) throws IOException {
        int lastSlashIndex = oldUrl.lastIndexOf('/');
        int lastDotIndex = oldUrl.lastIndexOf('.');
        String fileName = oldUrl.substring(lastSlashIndex + 1, lastDotIndex);
        cloudinary.uploader().destroy(fileName, ObjectUtils.asMap("resource_type","image"));
    }

    public List<Setting> getMailServerSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_SERVER);
    }

    public List<Setting> getEmailTemplateSettings(){
        return settingRepository.findByCategory(SettingCategory.MAIL_TEMPLATES);
    }

    public List<Setting> getCurrencySettings(){
        return settingRepository.findByCategory(SettingCategory.CURRENCY);
    }

    public List<Setting> getPaymentSetting(){
        return settingRepository.findByCategory(SettingCategory.PAYMENT);
    }
}
