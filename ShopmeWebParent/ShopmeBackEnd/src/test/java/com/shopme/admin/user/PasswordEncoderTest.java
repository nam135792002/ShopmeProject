package com.shopme.admin.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {
    @Test
    public void testEncoderPassword(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "nam2020";
        String encodePassword = passwordEncoder.encode(rawPassword);

        System.out.println(encodePassword);

        boolean matches = passwordEncoder.matches(rawPassword, encodePassword);

        assertThat(matches).isTrue();
    }
}
