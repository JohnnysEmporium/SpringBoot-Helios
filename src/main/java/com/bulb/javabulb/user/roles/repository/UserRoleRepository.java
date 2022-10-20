package com.bulb.javabulb.user.roles.repository;

import com.bulb.javabulb.user.roles.RoleEnum;
import com.bulb.javabulb.user.roles.UserRoleDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleDTO, Long> {
    UserRoleDTO findByName(RoleEnum roleName);
}
