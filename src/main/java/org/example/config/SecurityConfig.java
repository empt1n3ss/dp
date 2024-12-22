package org.example.config;

import org.example.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthService authService;

    public SecurityConfig(AuthService authService) {
        this.authService = authService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .requestMatchers(HttpMethod.POST, "/cloud/login").permitAll() // Разрешить доступ к /cloud/login
                .requestMatchers(HttpMethod.POST, "/cloud/logout").authenticated() // /cloud/logout требует авторизации
                .requestMatchers(HttpMethod.POST, "/cloud/file").authenticated() // /cloud/file требует авторизации
                .requestMatchers(HttpMethod.DELETE, "/cloud/file").authenticated() // /cloud/file требует авторизации
                .requestMatchers(HttpMethod.GET, "/cloud/file").authenticated() // /cloud/file требует авторизации
                .requestMatchers(HttpMethod.PUT, "/cloud/file").authenticated() // /cloud/file требует авторизации
                .requestMatchers(HttpMethod.GET, "/cloud/list").authenticated() // /cloud/list требует авторизации
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new TokenAuthenticationFilter(authService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
