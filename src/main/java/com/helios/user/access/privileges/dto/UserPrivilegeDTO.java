/* Info on user roles and permissions implementation:
No doubt there are multiple ways to do it, but the way I typically do it is to implement a custom UserDetails that knows
about roles and permissions. Role and Permission are just custom classes that you write. (Nothing fancy--Role has a name
and a set of Permission instances, and Permission has a name.) Then the getAuthorities() returns GrantedAuthority
objects that look like this:
PERM_CREATE_POST, PERM_UPDATE_POST, PERM_READ_POST

instead of returning things like
ROLE_USER, ROLE_MODERATOR

The roles are still available if your UserDetails implementation has a getRoles() method. (I recommend having one.)

Ideally you assign roles to the user and the associated permissions are filled in automatically. This would involve
having a custom UserDetailsService that knows how to perform that mapping, and all it has to do is source the mapping from the database. (See the article for the schema.)

Then you can define your authorization rules in terms of permissions instead of roles.

Hope that helps.
 */

package com.helios.user.access.privileges.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.helios.user.access.roles.dto.UserRoleDTO;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "roles_privileges")
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivilegeDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PrivilegeEnum name;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "privileges")
    //@JsonBackReference
    @JsonIgnoreProperties("privileges")
    private Collection<UserRoleDTO> roles;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PrivilegeEnum getName() {
        return name;
    }

    public void setName(PrivilegeEnum name) {
        this.name = name;
    }

    public Collection<UserRoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(Collection<UserRoleDTO> roles) {
        this.roles = roles;
    }
}
