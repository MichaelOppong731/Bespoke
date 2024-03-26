package com.bespoke.Bespoke;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.repository.AppUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class BespokeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BespokeApplication.class, args);
	}


	@Bean
	CommandLineRunner setAdmin(AppUserRepository appUserRepository, BCryptPasswordEncoder passwordEncoder){
		return args ->{
			AppUser Admin = new AppUser("Paul","Oppong",
					"Admin","michaeloppong731@gmail.com",
					passwordEncoder.encode("password"),
                    "ADMIN");
			appUserRepository.save(Admin);

		};
	}

}
