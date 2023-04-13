package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.slotboom.models.TaskStatus;

// This is a class representing an update request, containing properties with getter and setter methods.
public class UpdateTaskStatusRequest {
    @JsonProperty
    private TaskStatus taskStatus;


    public TaskStatus getTaskStatus() {
        return taskStatus;
    }
}
