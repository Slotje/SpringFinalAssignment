package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.slotboom.models.Role;

public class UpdateUserRoleRequest {
    @JsonProperty
    private String username;

    @JsonProperty
    private Role role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}

