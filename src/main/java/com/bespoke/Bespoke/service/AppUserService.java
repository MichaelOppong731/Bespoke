package com.bespoke.Bespoke.service;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.repository.AppUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    @Autowired
    private final AppUserRepository appUserRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    private static final String USER_NAME_NOT_FOUND = "User with email %s not found";



    // This part deals with App User Authentication by Email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<AppUser> opt = appUserRepository.findByEmail(email);
        if (opt.isEmpty()) {
            throw new UsernameNotFoundException(
                    String.format(USER_NAME_NOT_FOUND, email)
            );
        } else {
            return opt.get();
        }
    }

    //Get username of Existing User
    public UserDetails loadUserByUsernames(String username) throws UsernameNotFoundException {

        Optional<AppUser> opt = appUserRepository.findByUsername(username);
        if(opt.isEmpty()){
            throw new UsernameNotFoundException(
                    String.format(USER_NAME_NOT_FOUND, username)
            );
        }else {
            return opt.get();
        }
    }
    // Save User and manually assign the role of "USER" to the new user
        public Integer saveUser(AppUser appUser) {
        String passwd = appUser.getPassword();
        String encodePasswd = passwordEncoder.encode(passwd);
        appUser.setPassword(encodePasswd);
        appUser = appUserRepository.save(appUser);
        return appUser.getId();
    }

    public Optional<AppUser> findByEmail(String email) {

        return appUserRepository.findByEmail(email);
    }
}
