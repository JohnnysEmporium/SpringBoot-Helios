package com.bulb.javabulb.user.roles;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "user_roles")
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleEnum name;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public RoleEnum getName() {
        return name;
    }

    public void setName(RoleEnum roleName) {
        this.name = roleName;
    }
}
