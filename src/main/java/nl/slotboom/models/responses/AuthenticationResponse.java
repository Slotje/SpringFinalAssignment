package nl.slotboom.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    @JsonProperty("token")
    private String token;
}
