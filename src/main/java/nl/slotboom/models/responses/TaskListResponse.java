package nl.slotboom.models.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import nl.slotboom.models.TaskLists;
import nl.slotboom.models.Tasks;
import nl.slotboom.models.User;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class TaskListResponse {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("username")
    private String username;

    @JsonProperty("tasks")
    private List<Tasks> tasks;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("createdAt")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonProperty("updatedAt")
    private Date updatedAt;

    public static TaskListResponse from(TaskLists taskLists, User user, List<Tasks> tasks) {
        return TaskListResponse.builder()
                .id(taskLists.getId())
                .name(taskLists.getName())
                .description(taskLists.getDescription())
                .username(user.getUsername())
                .createdAt(taskLists.getCreatedAt())
                .updatedAt(taskLists.getUpdatedAt())
                .tasks(tasks)
                .build();
    }
}
