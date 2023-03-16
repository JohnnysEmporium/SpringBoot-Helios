package com.helios.user.access.roles.service;

import com.helios.user.access.roles.dto.UserRoleDTO;
import com.helios.user.access.roles.exceptions.RoleAlreadyExistsException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RoleService {
    UserRoleDTO saveRole(UserRoleDTO role) throws RoleAlreadyExistsException;
    UserRoleDTO deleteRole(UserRoleDTO role);
    List<UserRoleDTO> getRoles();
}
