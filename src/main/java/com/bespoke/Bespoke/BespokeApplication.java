package com.bespoke.Bespoke;

import com.bespoke.Bespoke.entities.AppUser;
import com.bespoke.Bespoke.service.AppUserService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BespokeApplication {



	public static void main(String[] args) {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();

       // Set environment variables programmatically
       dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));

		SpringApplication.run(BespokeApplication.class, args);
	}


	@Bean
	CommandLineRunner setAdmin(AppUserService appUserService){
		return args ->{
			if(appUserService.findByEmail("michaeloppong731@gmail.com").isEmpty()){
				AppUser Admin = new AppUser("Paul","Leonard",
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
