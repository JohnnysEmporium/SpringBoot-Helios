package com.bulb.javabulb;

import com.bulb.javabulb.user.dto.UserProfileDTO;
import com.bulb.javabulb.user.roles.Roles;
import com.bulb.javabulb.user.roles.dto.UserRoleDTO;
import com.bulb.javabulb.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;

//@EnableAutoConfiguration
@SpringBootApplication
@EntityScan("com.bulb.javabulb")
@EnableJpaRepositories("com.bulb.javabulb")
public class JavaBulbApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaBulbApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new UserRoleDTO(null, Roles.ROLE_USER));
			userService.saveRole(new UserRoleDTO(null, Roles.ROLE_ADMIN));

			userService.saveUser(new UserProfileDTO(null, "admin", "admin", new ArrayList<>()));
			userService.saveUser(new UserProfileDTO(null, "user", "user", new ArrayList<>()));

			userService.addRoleToUser("admin", Roles.ROLE_ADMIN);
			userService.addRoleToUser("user", Roles.ROLE_USER);
		};
	}
}
