package nl.slotboom.models.requests;

import com.fasterxml.jackson.annotation.JsonProperty;

// This is a class representing an update request, containing properties with getter and setter methods.
public class UpdateTaskRequest {
    @JsonProperty
    private String name;

    @JsonProperty
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
