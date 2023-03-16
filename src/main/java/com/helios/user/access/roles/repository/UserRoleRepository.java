package com.helios.user.access.roles.repository;

import java.lang.String;
import com.helios.user.access.roles.dto.UserRoleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleDTO, Long> {
    UserRoleDTO findByName(String roleName);
}
