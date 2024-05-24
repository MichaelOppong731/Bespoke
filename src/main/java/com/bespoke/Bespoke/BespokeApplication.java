package com.bespoke.Bespoke;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.repository.AppUserRepository;
import com.bespoke.Bespoke.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
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
	CommandLineRunner setAdmin(AppUserService appUserService){
		return args ->{
			if(appUserService.findByEmail("michaeloppong731@gmail.com").isEmpty()){
				AppUser Admin = new AppUser("Paul","Oppong",
					"michaeloppong731@gmail.com",
					"password",
                    "ROLE_ADMIN", true);

				appUserService.saveUser(Admin);
				System.out.println("Admin has been created!!");

			}else {
				System.out.println("Admin exists Already!!");
			}

		};
	}

}
