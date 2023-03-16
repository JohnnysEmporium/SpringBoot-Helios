package com.helios.user.service;

import com.helios.user.dto.UserProfileDTO;
import java.lang.String;
import com.helios.user.access.roles.dto.UserRoleDTO;
import com.helios.user.access.roles.exceptions.RoleAlreadyExistsException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    UserProfileDTO saveUser(UserProfileDTO user);
    UserRoleDTO saveRole(UserRoleDTO role) throws RoleAlreadyExistsException;
    void addRoleToUser(java.lang.String username, String roleName);
    UserProfileDTO getUser(java.lang.String username);
    List<UserProfileDTO> getUsers();
}
