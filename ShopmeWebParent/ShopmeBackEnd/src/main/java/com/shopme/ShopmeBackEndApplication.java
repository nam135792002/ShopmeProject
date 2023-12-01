package com.shopme;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EntityScan({"com.shopme.common.entity","com.shopme.admin.user"})
public class ShopmeBackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(ShopmeBackEndApplication.class, args);
    }

    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "ddfqlqfio",
                "api_key", "719983266129151",
                "api_secret", "h2MxGMlNYiPxloNgJGG64LdKjH4",
                "secure",true));
    }
}