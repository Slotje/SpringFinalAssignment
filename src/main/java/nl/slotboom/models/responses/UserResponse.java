package nl.slotboom.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import nl.slotboom.models.User;

import java.util.Date;

@Getter
@Setter
@Data
@AllArgsConstructor
@Builder
// This is a response class for User objects
public class UserResponse {

    @JsonProperty("username")
    private String username;

    @JsonProperty("role")
    private String role;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("updatedAt")
    private Date updatedAt;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .username(user.getUsername())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}



