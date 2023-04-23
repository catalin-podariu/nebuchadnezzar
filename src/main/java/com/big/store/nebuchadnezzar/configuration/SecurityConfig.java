package com.big.store.nebuchadnezzar.configuration;

import com.big.store.nebuchadnezzar.exception.AuthFailureHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        AuthFailureHandler failureHandler = new AuthFailureHandler();
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers(HttpMethod.GET,
                                "/products/getAll",
                                "/products/get/{id:[0-9]+}"
                        ).hasRole("USER")
                        .requestMatchers(HttpMethod.POST,
                                "/products/create"
                        ).hasRole("USER")
                        .requestMatchers(HttpMethod.PATCH,
                                "/products/updateName/**",
                                "/products/updatePrice/**"
                        ).hasRole("USER")
                        .requestMatchers(HttpMethod.DELETE,
                                "/products/delete/{id:[0-9]+}"
                        ).hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .failureHandler(failureHandler) // Use the custom failure handler
                        .permitAll()
                )
                .httpBasic()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }



    @Bean
    public UserDetailsService userDetailsService() {
        User.UserBuilder users = User.builder();
        UserDetails user = users
                .username("user")
                .password(PasswordEncoderFactories
                        .createDelegatingPasswordEncoder()
                        .encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = users
                .username("admin")
                .password(PasswordEncoderFactories
                        .createDelegatingPasswordEncoder()
                        .encode("password"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }
}
