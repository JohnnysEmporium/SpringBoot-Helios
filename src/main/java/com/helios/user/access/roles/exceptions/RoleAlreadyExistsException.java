package com.helios.user.access.roles.exceptions;

public class RoleAlreadyExistsException extends Exception {
    public RoleAlreadyExistsException (String errorMessage){
        super(errorMessage);
    }
}
