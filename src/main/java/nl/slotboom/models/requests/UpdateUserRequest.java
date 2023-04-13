package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

// This is a class representing an update request, containing properties with getter and setter methods.
public class UpdateUserRequest {
    @JsonProperty
    private String username;
    @JsonProperty
    private String password;

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

}



