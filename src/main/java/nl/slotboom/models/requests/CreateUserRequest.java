package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
// This is a request class that defines a request object with given properties
public class CreateUserRequest {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    String password;
}