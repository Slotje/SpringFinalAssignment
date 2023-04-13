package nl.slotboom.controllers;

import nl.slotboom.models.requests.CreateTasklistRequest;
import nl.slotboom.models.requests.UpdateTasklistRequest;
import nl.slotboom.models.responses.CreateTaskListResponse;
import nl.slotboom.models.responses.TaskListResponse;
import nl.slotboom.models.responses.UpdateTaskListResponse;
import nl.slotboom.services.TasklistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nl.slotboom.constants.APIConstants.*;

@RestController
@RequestMapping("/" + API + "/" + VERSION + "/" + TASKLISTS_ENDPOINT)
@PreAuthorize("isAuthenticated()")
public class TasklistsController {

    // Injecting TasklistsService bean
    @Autowired
    private TasklistsService service;

    // Retrieves all task lists for the authenticated user
    @GetMapping("/details")
    public ResponseEntity<List<TaskListResponse>> getTaskListsForUser(
            Authentication authentication) {
        String username = authentication.getName();
        List<TaskListResponse> response = service.getTaskListsForUser(username);
        return ResponseEntity.ok(response);
    }

    // Retrieves a specific task list for the authenticated user
    @GetMapping("/details/{taskListName}")
    public ResponseEntity<TaskListResponse> getSpecificTaskListForUser(
            @PathVariable String taskListName,
            Authentication authentication) {
        String username = authentication.getName();
        TaskListResponse response = service.getSpecificTaskListsForUser(username, taskListName);
        return ResponseEntity.ok(response);
    }

    // Retrieves all task lists for a specified user (requires admin permission)
    @GetMapping("/admin/{username}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskListResponse>> getTaskListsForUser(
            @PathVariable String username) {
        List<TaskListResponse> response = service.getTaskListsForUser(username);
        return ResponseEntity.ok(response);
    }

    // Retrieves all task lists for all users (requires admin permission)
    @GetMapping("/admin/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskListResponse>> getAllTaskListResponses() {
        List<TaskListResponse> response = service.getAllTaskListResponses();
        return ResponseEntity.ok(response);
    }

    // Creates a new task list for the authenticated user
    @PostMapping("/add")
    public ResponseEntity<CreateTaskListResponse> createTaskListForUser(
            @RequestBody CreateTasklistRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        CreateTaskListResponse response = service.createTaskListForUser(username, request);
        return ResponseEntity.ok(response);
    }

    // Updates an existing task list for the authenticated user
    @PutMapping("/update/{taskListName}")
    public ResponseEntity<UpdateTaskListResponse> updateTaskListForUser(
            @PathVariable String taskListName,
            @RequestBody UpdateTasklistRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        UpdateTaskListResponse response = service.updateTaskListForUser(username, taskListName, request);
        return ResponseEntity.ok(response);
    }

    // Deletes an existing task list for the authenticated user
    @DeleteMapping("/delete/{taskListName}")
    public ResponseEntity<Void> deleteTaskList(
            @PathVariable String taskListName,
            Authentication authentication) {
        String username = authentication.getName();
        service.deleteTaskList(username, taskListName);
        return ResponseEntity.ok().build();
    }
}
