package com.example.demo.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.UserInfoService;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    
    private final UserInfoService userInfoService;

    public SecurityConfig(UserInfoService userInfoService) {
        this.userInfoService = userInfoService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userInfoService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
        .formLogin(login -> login
            .loginProcessingUrl("/signin")
            .loginPage("/signin") 
            .usernameParameter("email") 
            .passwordParameter("password")
            .defaultSuccessUrl("/home", true)
            .permitAll()
        )
        .logout(logout -> logout
            .logoutUrl("/logout")
            .logoutSuccessUrl("/signin")
        )
        .authorizeHttpRequests(authz -> authz
            .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
            .requestMatchers("/signup").permitAll()
            .requestMatchers("/signin").permitAll()
            .requestMatchers("/home").permitAll()
            .requestMatchers("/products/{product_category_id}").permitAll()
            .requestMatchers("/low_categories/{sub_category_id}").permitAll()
            .requestMatchers("/mid_categories/{category_id}").permitAll()
            .requestMatchers("/top_categories").permitAll()
            .requestMatchers("/manufacturer").permitAll()
            .requestMatchers("/noItem").permitAll()
            .requestMatchers("/order_history_detail/{id}").permitAll()
            .requestMatchers("/order_history").permitAll()
            .requestMatchers("/orderComplete").permitAll()
            .requestMatchers("/orderForm").permitAll()
            .requestMatchers("/products").permitAll()
            .requestMatchers("/store_info").permitAll()
            .requestMatchers("/user_info/{id}").permitAll()
            .requestMatchers("/admin_list").permitAll()
            .requestMatchers("/edit/{id}").permitAll()
            .requestMatchers("/editComplete").permitAll()
            .requestMatchers("/delete/{id}").permitAll()
            .anyRequest().authenticated()
        )
        .authenticationProvider(authenticationProvider());
        
        return http.build();
    }
}
