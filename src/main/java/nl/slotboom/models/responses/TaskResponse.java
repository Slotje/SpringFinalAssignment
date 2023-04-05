package nl.slotboom.models.responses;

import nl.slotboom.models.TaskStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class TaskResponse {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private TaskStatus status;

    @JsonProperty("tasklist_id")
    private UUID taskListUuid;
}
