package com.flowers.shopping.config;

import com.flowers.shopping.filter.JwtFilter;
import com.flowers.shopping.handler.AuthEntryPointHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final AuthEntryPointHandler authEntryPointHandler;

    public SecurityConfig(JwtFilter jwtFilter, AuthEntryPointHandler authEntryPointHandler) {
        this.jwtFilter = jwtFilter;
        this.authEntryPointHandler = authEntryPointHandler;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPointHandler)) // 设置未登录处理器
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/user/add").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }

}
