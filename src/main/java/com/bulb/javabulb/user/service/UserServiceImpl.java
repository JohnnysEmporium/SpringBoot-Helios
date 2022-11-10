package com.bulb.javabulb.user.service;

import com.bulb.javabulb.user.dto.UserProfileDTO;
import com.bulb.javabulb.user.repository.UserProfileRepository;
import com.bulb.javabulb.user.roles.exceptions.RoleAlreadyExistsException;
import com.bulb.javabulb.user.roles.Roles;
import com.bulb.javabulb.user.roles.dto.UserRoleDTO;
import com.bulb.javabulb.user.roles.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserProfileRepository userProfileRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileDTO saveUser(UserProfileDTO user) {
        log.info("Saving user {} to the database", user.getUsername());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userProfileRepository.save(user);
    }

    @Override
    public UserRoleDTO saveRole(UserRoleDTO role) throws RoleAlreadyExistsException {
        if (userRoleRepository.findByName(role.getName()) == null) {
            log.info("Saving role {} to the database", role.getName());
            return userRoleRepository.save(role);
        } else {
            log.warn("Role with name of {} already exists", role.getName());
            throw new RoleAlreadyExistsException("Role already exists");
        }
    }

    @Override
    public void addRoleToUser(String username, Roles roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        UserProfileDTO user = userProfileRepository.findByUsername(username);
        UserRoleDTO role = userRoleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Override
    public UserProfileDTO getUser(String username) {
        log.info("Looking for user {}", username);
        return userProfileRepository.findByUsername(username);
    }

    @Override
    public List<UserProfileDTO> getUsers() {
        log.info("Getting all users");
        return userProfileRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserProfileDTO user = userProfileRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User '" + username + "' found in database");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> authorities.add(new SimpleGrantedAuthority(role.getName().toString())));
        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}