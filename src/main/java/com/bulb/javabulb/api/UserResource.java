package com.bulb.javabulb.api;

import com.auth0.jwt.algorithms.Algorithm;
import com.bulb.javabulb.security.jwt.AlgorithmHolder;
import com.bulb.javabulb.security.jwt.JWTHelper;
import com.bulb.javabulb.user.dto.UserProfileDTO;
import com.bulb.javabulb.user.roles.Roles;
import com.bulb.javabulb.user.roles.dto.UserRoleDTO;
import com.bulb.javabulb.user.roles.exceptions.RoleAlreadyExistsException;
import com.bulb.javabulb.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserProfileDTO>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/dupa")
    public ResponseEntity<String> dupa(Authentication a){
        User user = (User) a.getPrincipal();
        System.out.println(user.getAuthorities() + " chuj");
        return ResponseEntity.ok().body("GET NAME: " + a.getName());
    }

    @PostMapping("/user/save")
    public ResponseEntity<UserProfileDTO> saveUser(@RequestBody UserProfileDTO user, Principal p){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<?> saveRole(@RequestBody UserRoleDTO role) {
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());

        try {
            return ResponseEntity.created(uri).body(userService.saveRole(role));

        } catch (RoleAlreadyExistsException e){
            Map<String, String> error = new HashMap<>();
            error.put("error", "Role already exists");

            return ResponseEntity.ok()
                    .header("error", "Role already exists")
                    .body(error);
        }
    }

    //TODO: Fix
    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody AssignRoleToUserForm form){
        userService.addRoleToUser(form.getUsername(), Roles.valueOf(form.getRoleName()));
        return ResponseEntity.ok().build();
    }

    //TODO: implement refreshToken, share the code with CustomAuthorizationFilter?
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                Algorithm algorithm = AlgorithmHolder.algorithm;
                UserProfileDTO user = userService.getUser(JWTHelper.getUsername(authorizationHeader));
                String accessToken = JWTHelper.generateAccessToken(user, request.getRequestURL().toString());

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);

                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);

            } catch (Exception e){
                e.printStackTrace();
                log.error("Error generating new token: {}", e.getMessage());

                Map<String, String> error = new HashMap<>();
                error.put("error", "Could not generate new token");

                response.setHeader("error", "Could not generate new token");
                new ObjectMapper().writeValue(response.getOutputStream(), error);

            }
        } else {

        }
    }
}

@Data
class AssignRoleToUserForm{
    private String username;
    private String roleName;
}