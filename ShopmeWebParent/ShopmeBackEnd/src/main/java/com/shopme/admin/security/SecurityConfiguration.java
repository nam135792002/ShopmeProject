package com.shopme.admin.security;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
public class SecurityConfiguration{

    @Bean
    public UserDetailsService  userDetailsService(){
        return new ShopmeUserDetailsService();
    }
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider())
                .authorizeRequests()
                .requestMatchers("/api/**").permitAll()
                .requestMatchers("/users/**","/settings/**","/countries/**","/states/**").hasAuthority("Admin")
                .requestMatchers("/categories/**","/brands/**","/articles/**", "/menus/**" ).hasAnyAuthority("Admin","Editor")
                .requestMatchers("/products/new","/products/delete/**").hasAnyAuthority("Admin","Editor")
                .requestMatchers("/products/edit/**","/products/save","/products/check_unique").hasAnyAuthority("Admin","Editor","Salesperson")
                .requestMatchers("/products","/products/","/products/detail/**","/products/page/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
                .requestMatchers("/products/**").hasAnyAuthority("Admin","Editor")
                .requestMatchers("/customers/**","/shipping_rates/**", "/reports/**").hasAnyAuthority("Admin","Salesperson")
                .requestMatchers("/orders/**").hasAnyAuthority("Admin","Salesperson","Shipper")
                .requestMatchers("/products/detail/**","/customers/detail/**").hasAnyAuthority("Admin","Editor","Salesperson","Assistant")
                .requestMatchers("/reviews/**").hasAnyAuthority("Admin","Assistant")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                .permitAll()
                .and().logout()
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutSuccessUrl("/login")
                .permitAll()
                .and()
                .rememberMe()
                .key("AbcDefgHijKlmnOpqrs_1234567890")
                .tokenValiditySeconds(7 * 24 * 60 * 60);
        http.headers().frameOptions().disable();
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(staticResourcesMatcher());
    }

    private RequestMatcher staticResourcesMatcher() {
        return new RequestMatcher() {
            @Override
            public boolean matches(HttpServletRequest request) {
                String path = request.getRequestURI();
                return PathRequest.toStaticResources().atCommonLocations().matches(request)
                        || path.startsWith("/images/**")
                        || path.startsWith("/js/**")
                        || path.startsWith("/webjars/**");
            }
        };
    }

}
