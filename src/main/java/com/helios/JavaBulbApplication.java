package com.helios;

import com.helios.user.access.privileges.dto.PrivilegeEnum;
import com.helios.user.access.privileges.dto.UserPrivilegeDTO;
import com.helios.user.access.privileges.repository.UserPrivilegeRepository;
import com.helios.user.dto.UserProfileDTO;
import com.helios.user.access.roles.dto.UserRoleDTO;
import com.helios.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.ArrayList;

//@EnableAutoConfiguration
@SpringBootApplication
@EntityScan("com.helios")
@EnableJpaRepositories("com.helios")
public class JavaBulbApplication {

	public static void main(java.lang.String[] args) {
		SpringApplication.run(JavaBulbApplication.class, args);
	}

	@Bean
	CommandLineRunner run(UserService userService, UserPrivilegeRepository privilegeRepository){
		return args -> {
			UserRoleDTO roleAdmin = new UserRoleDTO(null, "ROLE_ADMIN", null, new ArrayList<>());
			UserRoleDTO roleUser = new UserRoleDTO(null, "ROLE_USER", null, new ArrayList<>());

			roleAdmin.addPrivilege(privilegeRepository.save(new UserPrivilegeDTO(null, PrivilegeEnum.ROLE_WRITE, new ArrayList<>())));
			roleAdmin.addPrivilege(privilegeRepository.save(new UserPrivilegeDTO(null, PrivilegeEnum.USER_WRITE, new ArrayList<>())));
			roleUser.addPrivilege(privilegeRepository.save(new UserPrivilegeDTO(null, PrivilegeEnum.ROLE_READ, new ArrayList<>())));
			roleUser.addPrivilege(privilegeRepository.save(new UserPrivilegeDTO(null, PrivilegeEnum.USER_READ, new ArrayList<>())));

			userService.saveRole(roleAdmin);
			userService.saveRole(roleUser);

			userService.saveUser(new UserProfileDTO(null, "admin", "admin", new ArrayList<>()));
			userService.saveUser(new UserProfileDTO(null, "user", "user", new ArrayList<>()));

			userService.addRoleToUser("admin", "ROLE_ADMIN");
			userService.addRoleToUser("admin", "ROLE_USER");
			userService.addRoleToUser("user", "ROLE_USER");
		};
	}
}
