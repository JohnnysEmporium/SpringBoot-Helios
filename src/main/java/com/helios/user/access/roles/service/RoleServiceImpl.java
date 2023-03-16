package com.helios.user.access.roles.service;

import com.helios.user.access.roles.dto.UserRoleDTO;
import com.helios.user.access.roles.exceptions.RoleAlreadyExistsException;
import com.helios.user.access.roles.repository.UserRoleRepository;
import com.helios.user.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public UserRoleDTO saveRole(UserRoleDTO role) throws RoleAlreadyExistsException {
        return null;
    }

    @Override
    public UserRoleDTO deleteRole(UserRoleDTO role) {
        return null;
    }

    @Override
    public List<UserRoleDTO> getRoles() {
        return userRoleRepository.findAll();
    }


}
