package com.helios.user.repository;

import com.helios.user.dto.UserProfileDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileDTO, Long> {
    UserProfileDTO findByUsername(String username);
}
