package nl.slotboom.models;

import lombok.AllArgsConstructor;

// fixed contants of task status
@AllArgsConstructor
public enum TaskStatus {

    ToDo("To Do"),
    Doing("Doing"),
    Done("Done");

    private String status;

    public String getStatus() {
        return status;
    }
}
