package com.bulb.javabulb.user.roles.exceptions;

public class RoleAlreadyExistsException extends Exception {
    public RoleAlreadyExistsException (String errorMessage){
        super(errorMessage);
    }
}
