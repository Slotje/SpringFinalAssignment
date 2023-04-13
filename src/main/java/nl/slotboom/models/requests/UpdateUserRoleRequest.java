package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.slotboom.models.Role;

// This is a class representing an update request, containing properties with getter and setter methods.
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
}

