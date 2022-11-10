package com.bulb.javabulb.user.roles;

//Enums are constant, cannot dynamically edit
//therefore, for adding new roles (outside of the ones present in enum
//this needs to be replaced with a POJO

//TODO: currently, can add multiple roles with the same name - fix it
public enum Roles {
        ROLE_USER,
        ROLE_MODERATOR,
        ROLE_ADMIN
}