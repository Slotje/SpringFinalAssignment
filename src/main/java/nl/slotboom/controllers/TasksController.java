package nl.slotboom.controllers;

import nl.slotboom.models.requests.CreateTaskRequest;
import nl.slotboom.models.requests.UpdateTaskRequest;
import nl.slotboom.models.requests.UpdateTaskStatusRequest;
import nl.slotboom.models.responses.TaskResponse;
import nl.slotboom.services.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static nl.slotboom.constants.APIConstants.*;

@RestController
@RequestMapping("/" + API + "/" + VERSION + "/" + TASKS_ENDPOINT)
@PreAuthorize("isAuthenticated()")
public class TasksController {
    @Autowired
    private TasksService service;

    @PostMapping("/{taskListName}/add")
    public ResponseEntity<TaskResponse> createTaskForTaskList(
            @PathVariable String taskListName,
            @RequestBody CreateTaskRequest createTaskRequest) {
        TaskResponse taskResponse = service.createTaskForTaskList(taskListName, createTaskRequest);
        return ResponseEntity.ok(taskResponse);
    }


    @PutMapping("/updaterole/{taskName}")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable String taskName,
            @RequestBody UpdateTaskStatusRequest request) {
        TaskResponse taskResponse = service.updateTaskStatus(taskName, request);
        return ResponseEntity.ok(taskResponse);
    }


    @PutMapping("/update/{taskName}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable String taskName,
            @RequestBody UpdateTaskRequest updateTaskRequest) {
        TaskResponse taskResponse = service.updateTask(taskName, updateTaskRequest);
        return ResponseEntity.ok(taskResponse);
    }

    @DeleteMapping("/delete/{taskListName}/{taskName}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String taskListName,
            @PathVariable String taskName,
            Authentication authentication) {
        String username = authentication.getName();
        service.deleteTask(taskListName, taskName, username);
        return ResponseEntity.ok().build();
    }
}

