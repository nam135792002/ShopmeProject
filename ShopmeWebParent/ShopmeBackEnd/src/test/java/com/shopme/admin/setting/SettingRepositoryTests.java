package com.shopme.admin.setting;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class SettingRepositoryTests {
    @Autowired SettingRepository settingRepository;

    @Test
    public void testCreateGeneralSettings(){
//        Setting siteName = new Setting("SITE_NAME","Shopme", SettingCategory.GENERAL);
//        Setting siteLogo = new Setting("SITE_LOGO","Shopme.png", SettingCategory.GENERAL);
//        Setting copyright = new Setting("COPYRIGHT","Copyright (c) 2023 Shopme Ltd.", SettingCategory.GENERAL);
//        settingRepository.saveAll(List.of(siteLogo,copyright));
//        Setting currencyId = new Setting("CURRENCY_ID","1", SettingCategory.CURRENCY);
//        Setting symbol = new Setting("CURRENCY_SUMBOL","$",SettingCategory.CURRENCY);
//        Setting symbolPosition = new Setting("CURRENCY_SYMBOL_POSITION","before",SettingCategory.CURRENCY);
//        Setting decimalPointType = new Setting("DECIMAL_POINT_TYPE","POINT",SettingCategory.CURRENCY);
//        Setting decimalDigits = new Setting("DECIMAL_DIGITS","2",SettingCategory.CURRENCY);
//        Setting thousandsPointType = new Setting("THOUSANDS_POINT_TYPE","COMMA",SettingCategory.CURRENCY);

//        Setting setting_1 = new Setting("MAIL_HOST","smtp.gmail.com",SettingCategory.MAIL_SERVER);
//        Setting setting_2 = new Setting("MAIL_PORT","123",SettingCategory.MAIL_SERVER);
//        Setting setting_3 = new Setting("MAIL_USERNAME","username",SettingCategory.MAIL_SERVER);
//        Setting setting_4 = new Setting("MAIL_PASSWORD","password",SettingCategory.MAIL_SERVER);
//        Setting setting_5 = new Setting("MAIL_FORM","phuongnama32112002@gmail.com",SettingCategory.MAIL_SERVER);
//        Setting setting_6 = new Setting("SMTP_AUTH","true",SettingCategory.MAIL_SERVER);
//        Setting setting_7 = new Setting("SMTP_SECURED","true",SettingCategory.MAIL_SERVER);
//        Setting setting_8 = new Setting("MAIL_SENDER_NAME","Shopme Team",SettingCategory.MAIL_SERVER);
//        Setting setting_9 = new Setting("CUSTOMER_VERIFY_CONTENT","Email content",SettingCategory.MAIL_TEMPLATES);
//        Setting setting_10 = new Setting("CUSTOMER_VERIFY_SUBJECT","Email subject",SettingCategory.MAIL_TEMPLATES);

//        Setting setting_1 = new Setting("ORDER_CONFIRMATION_SUBJECT","Email content",SettingCategory.MAIL_TEMPLATES);
//        Setting setting_2 = new Setting("ORDER_CONFIRMATION_CONTENT","Email subject",SettingCategory.MAIL_TEMPLATES);

        Setting setting_1 = new Setting("PAYPAL_API_BASE_URL","abc",SettingCategory.PAYMENT);
        Setting setting_2 = new Setting("PAYPAL_API_CLIENT_ID","def",SettingCategory.PAYMENT);
        Setting setting_3 = new Setting("PAYPAL_API_CLIENT_SECRET","def",SettingCategory.PAYMENT);

        settingRepository.saveAll(List.of(setting_1, setting_2, setting_3));
    }

    @Test
    public void testListSettingByCategory(){
        List<Setting> settings = new ArrayList<>();

        List<Setting> generalSettings = settingRepository.findByCategory(SettingCategory.GENERAL);
        List<Setting> currencySettings = settingRepository.findByCategory(SettingCategory.CURRENCY);

        settings.addAll(generalSettings);
        settings.addAll(currencySettings);
        for(Setting setting : settings){
            System.out.println(setting.getKey());
        }
    }
}
