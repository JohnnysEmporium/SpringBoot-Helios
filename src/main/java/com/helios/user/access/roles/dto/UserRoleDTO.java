package com.helios.user.access.roles.dto;

import java.lang.String;
import java.util.Collection;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.helios.user.access.privileges.dto.UserPrivilegeDTO;
import com.helios.user.dto.UserProfileDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String name;

    @ManyToMany(mappedBy = "roles")
    //@JsonBackReference
    @JsonIgnoreProperties("roles")
    private Collection<UserProfileDTO> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role_privileges",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "privilege_id")}
    )
    //@JsonManagedReference
    @JsonIgnoreProperties("roles")
    private Collection<UserPrivilegeDTO> privileges;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String roleName) {
        this.name = roleName;
    }

    public Collection<UserProfileDTO> getUsers() {
        return users;
    }

    public void setUsers(Collection<UserProfileDTO> users) {
        this.users = users;
    }

    public Collection<UserPrivilegeDTO> getPrivileges() {
        return privileges;
    }

    //methods for bidirectional association
    public void addPrivilege(UserPrivilegeDTO privilege){
        this.privileges.add(privilege);
        privilege.getRoles().add(this);
    }

    public void removePrivilege(UserPrivilegeDTO privilege){
        this.privileges.remove(privilege);
        privilege.getRoles().remove(this);
    }
}
