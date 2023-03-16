package com.helios.user.service;

import com.helios.user.access.privileges.dto.UserPrivilegeDTO;
import com.helios.user.dto.UserProfileDTO;
import com.helios.user.repository.UserProfileRepository;
import com.helios.user.access.roles.exceptions.RoleAlreadyExistsException;
import java.lang.String;
import com.helios.user.access.roles.dto.UserRoleDTO;
import com.helios.user.access.roles.repository.UserRoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
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
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        UserProfileDTO user = userProfileRepository.findByUsername(username);
        UserRoleDTO role = userRoleRepository.findByName(roleName);
        user.addRole(role);
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
//        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        return new User(user.getUsername(), user.getPassword(), getAuthorities(user.getRoles()));
    }


    //Creates a granted authority Collection that consists of roles and privileges.
    //That way it is possible to define access based on both of them.
    private Collection<? extends GrantedAuthority> getAuthorities(Collection<UserRoleDTO> roles){
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(Collection<UserRoleDTO> roles){
        List<String> privileges = new ArrayList<>();
        List<UserPrivilegeDTO> collection = new ArrayList<>();

        //Adding roles to return variable
        for(UserRoleDTO role : roles){
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }

        //Adding privileges to return variable
        for(UserPrivilegeDTO item : collection){
            privileges.add(item.getName().toString());
        }
        return privileges;
    }


    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(String privilege : privileges){
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}