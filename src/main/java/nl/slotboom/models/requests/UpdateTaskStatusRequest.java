package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.slotboom.models.TaskStatus;

public class UpdateTaskStatusRequest {

    @JsonProperty
    private TaskStatus taskStatus;


    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }
}
