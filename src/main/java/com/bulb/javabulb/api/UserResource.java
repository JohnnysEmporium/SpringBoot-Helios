package com.bulb.javabulb.api;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.bulb.javabulb.security.jwt.AlgorithmHolder;
import com.bulb.javabulb.security.jwt.JWTHelper;
import com.bulb.javabulb.user.dto.UserProfileDTO;
import com.bulb.javabulb.user.roles.RoleEnum;
import com.bulb.javabulb.user.roles.UserRoleDTO;
import com.bulb.javabulb.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserResource {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<UserProfileDTO>> getUsers(){
        return ResponseEntity.ok().body(userService.getUsers());
    }

    @GetMapping("/dupa")
    public ResponseEntity<String> dupa(Authentication a){
        return ResponseEntity.ok().body("GET NAME: " + a.getName());
    }

    @PostMapping("/user/save")
    public ResponseEntity<UserProfileDTO> saveUser(@RequestBody UserProfileDTO user){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/user/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveUser(user));
    }

    @PostMapping("/role/save")
    public ResponseEntity<UserRoleDTO> saveRole(@RequestBody UserRoleDTO role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addRoleToUser(@RequestBody AssignRoleToUserForm form){
        userService.addRoleToUser(form.getUsername(), RoleEnum.valueOf(form.getRoleName()));
        return ResponseEntity.ok().build();
    }

    //TODO: implement refreshToken, share the code with CustomAuthorizationFilter?
    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = AlgorithmHolder.algorithm;
                UserProfileDTO user = userService.getUser(JWTHelper.getUsername(refreshToken));

                String accessToken = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", user.getRoles().stream().map(UserRoleDTO::getName).collect(Collectors.toList()))
                        .sign(algorithm);

                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);


            //TODO: Utility class for catch? Same thing in CustomAuthorizationFilter
            } catch (Exception e){
                Map<String, String> error = new HashMap<>();

                response.setHeader("error", e.getMessage());
                response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());

                error.put("error", e.getMessage());
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