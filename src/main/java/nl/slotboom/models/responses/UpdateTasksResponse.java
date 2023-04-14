package nl.slotboom.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nl.slotboom.models.TaskStatus;
import nl.slotboom.models.Tasks;

import java.util.Date;

@Getter
@Setter
@Builder
// This is a response class for Task objects
public class UpdateTasksResponse {
    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("status")
    private TaskStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("updatedAt")
    private Date updatedAt;

    public static UpdateTasksResponse from(Tasks tasks) {
        return UpdateTasksResponse.builder()
                .id(tasks.getId())
                .name(tasks.getName())
                .description(tasks.getDescription())
                .status(tasks.getStatus())
                .updatedAt(tasks.getUpdatedAt())
                .build();
    }
}