package com.bulb.javabulb.user.repository;

import com.bulb.javabulb.user.dto.UserProfileDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfileDTO, Long> {
    UserProfileDTO findByUsername(String username);
}
