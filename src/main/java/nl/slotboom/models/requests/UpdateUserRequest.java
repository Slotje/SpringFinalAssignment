package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

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



