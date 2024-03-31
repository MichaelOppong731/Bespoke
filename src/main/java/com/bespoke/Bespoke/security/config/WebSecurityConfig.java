package com.bespoke.Bespoke.security.config;

import com.bespoke.Bespoke.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class WebSecurityConfig {
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AppUserService appUserService;



    @Bean
    public DefaultSecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        return http
                .csrf(c -> c.disable())
                .authorizeHttpRequests(request -> {request.requestMatchers("/login", "/registration").permitAll();
                request.requestMatchers("/Admin/**").hasAuthority("USER");
                request.requestMatchers("/User/**").hasAuthority("ADMIN");
                request.anyRequest().authenticated();
                })

                .formLogin(form -> {
                    form.loginPage("/login").permitAll();
                })
                .build();

    }


//                .logout(form -> form.invalidateHttpSession(true).clearAuthentication(true)
//                        .logoutRequestMatcher(new AntPathRequestMatcher("logout"))
//                        .logoutSuccessUrl("/login?logout").permitAll())


    @Bean
    public AuthenticationProvider authenticationManager(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(appUserService);
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        return provider;
    }




}
