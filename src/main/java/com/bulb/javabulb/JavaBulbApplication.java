package com.bulb.javabulb;

import com.bulb.javabulb.user.dto.UserProfileDTO;
import com.bulb.javabulb.user.roles.RoleEnum;
import com.bulb.javabulb.user.roles.UserRoleDTO;
import com.bulb.javabulb.user.service.UserService;
import com.bulb.javabulb.user.service.UserServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner run(UserService userService){
		return args -> {
			userService.saveRole(new UserRoleDTO(null, RoleEnum.ROLE_USER));
			userService.saveRole(new UserRoleDTO(null, RoleEnum.ROLE_ADMIN));

			userService.saveUser(new UserProfileDTO(null, "admin", "admin", new ArrayList<>()));
			userService.saveUser(new UserProfileDTO(null, "user", "user", new ArrayList<>()));

			userService.addRoleToUser("admin", RoleEnum.ROLE_ADMIN);
			userService.addRoleToUser("user", RoleEnum.ROLE_USER);
		};
	}
}
