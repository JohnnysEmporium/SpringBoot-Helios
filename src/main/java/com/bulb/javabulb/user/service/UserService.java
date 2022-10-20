package com.bulb.javabulb.user.service;

import com.bulb.javabulb.user.dto.UserProfileDTO;
import com.bulb.javabulb.user.roles.RoleEnum;
import com.bulb.javabulb.user.roles.UserRoleDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    UserProfileDTO saveUser(UserProfileDTO user);
    UserRoleDTO saveRole(UserRoleDTO role);
    void addRoleToUser(String username, RoleEnum roleName);
    UserProfileDTO getUser(String username);
    List<UserProfileDTO> getUsers();
}
