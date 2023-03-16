package com.helios.user.access.privileges.repository;

import com.helios.user.access.privileges.dto.PrivilegeEnum;
import com.helios.user.access.privileges.dto.UserPrivilegeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPrivilegeRepository extends JpaRepository<UserPrivilegeDTO, Long> {
    UserPrivilegeDTO findByName(PrivilegeEnum name);
}
