package com.bulb.javabulb.user.roles;

import javax.persistence.AttributeConverter;

public class RoleConverter implements AttributeConverter<Roles, String> {

    @Override
    public String convertToDatabaseColumn(Roles roles) {
        if(roles == null){
            return null;
        }
        return roles.name();
    }

    @Override
    public Roles convertToEntityAttribute(String s) {
        return null;
    }
}
