package com.bespoke.Bespoke.security.config;

import com.bespoke.Bespoke.service.AuthenticationSuccessHandler;
import com.bespoke.Bespoke.service.AppUserService;
import com.bespoke.Bespoke.service.CustomAccessDeniedHandler;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AppUserService appUserService;
    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;





    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {request.requestMatchers("/login", "/register"
                ,"/password-request", "/reset-password", "/account-verification", "/error").permitAll();
                request.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();//Allow static files
                request.requestMatchers("/admin/**","/upload").hasAuthority("ROLE_ADMIN");
                request.requestMatchers("/user/**").hasAuthority("ROLE_USER");
                request.requestMatchers("/video/**").hasAnyAuthority("ROLE_USER","ROLE_ADMIN");
                request.anyRequest().authenticated();
                })

                .exceptionHandling(exceptionHandlingConfigurer -> {
                    exceptionHandlingConfigurer.accessDeniedHandler(accessDeniedHandler());
                })

                .formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
                        .successHandler(authenticationSuccessHandler).permitAll())

                .logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                        .logoutSuccessUrl("/login?logout").permitAll())


                .build();

    }


    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }


    @Bean
    public AuthenticationProvider authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }




}
