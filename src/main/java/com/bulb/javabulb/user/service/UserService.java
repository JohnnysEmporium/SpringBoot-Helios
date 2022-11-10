package com.bulb.javabulb.user.service;

import com.bulb.javabulb.user.dto.UserProfileDTO;
import com.bulb.javabulb.user.roles.Roles;
import com.bulb.javabulb.user.roles.dto.UserRoleDTO;
import com.bulb.javabulb.user.roles.exceptions.RoleAlreadyExistsException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    UserProfileDTO saveUser(UserProfileDTO user);
    UserRoleDTO saveRole(UserRoleDTO role) throws RoleAlreadyExistsException;
    void addRoleToUser(String username, Roles roleName);
    UserProfileDTO getUser(String username);
    List<UserProfileDTO> getUsers();
}
