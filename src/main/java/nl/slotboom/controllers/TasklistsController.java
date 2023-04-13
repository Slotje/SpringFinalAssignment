package nl.slotboom.controllers;

import nl.slotboom.exceptions.AppException;
import nl.slotboom.models.requests.CreateTasklistRequest;
import nl.slotboom.models.responses.TaskListResponse;
import nl.slotboom.services.TasklistsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static nl.slotboom.constants.APIConstants.*;

@RestController // defines the class as a REST controller
@RequestMapping("/" + API + "/" + VERSION + "/" + TASKLISTS_ENDPOINT) // sets the base endpoint for the controller
@PreAuthorize("isAuthenticated()") // requires authentication to access any endpoint in the controller
public class TasklistsController {
    @Autowired // injects an instance of the TasklistsService into the controller
    private TasklistsService service;

    @GetMapping("/details") // defines an HTTP GET endpoint with a path parameter
    public ResponseEntity<List<TaskListResponse>> getTaskListsForUser(
            Authentication authentication) {
        String username = authentication.getName();
        List<TaskListResponse> taskListResponses = service.getTaskListsForUser(username);
        return ResponseEntity.ok(taskListResponses);
    }

    @GetMapping("/details/{taskListName}") // defines an HTTP GET endpoint with a path variable
    public ResponseEntity<TaskListResponse> getSpecificTaskListForUser(
            @PathVariable String taskListName,
            Authentication authentication) {
        String username = authentication.getName();
        TaskListResponse taskListResponse = service.getSpecificTaskListsForUser(username, taskListName);
        return ResponseEntity.ok(taskListResponse);
    }

    @GetMapping("/admin/{username}") // defines an HTTP GET endpoint with a path variable and requires ADMIN authority to access
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskListResponse>> getTaskListsForUser(
            @PathVariable String username){
        List<TaskListResponse> taskListResponses = service.getTaskListsForUser(username);
        return ResponseEntity.ok(taskListResponses);
    }

    @GetMapping("/admin/all") // defines an HTTP GET endpoint and requires ADMIN authority to access
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<TaskListResponse>> getAllTaskListResponses() {
        List<TaskListResponse> taskListResponses = service.getAllTaskListResponses();
        return ResponseEntity.ok(taskListResponses);
    }

    @PostMapping("/add") // defines an HTTP POST endpoint
    public ResponseEntity<TaskListResponse> createTaskListForUser(
            @RequestBody CreateTasklistRequest createTasklistRequest,
            Authentication authentication) {
        String username = authentication.getName();
        TaskListResponse taskListResponse = service.createTaskListForUser(username, createTasklistRequest);
        return ResponseEntity.ok(taskListResponse);
    }

    @DeleteMapping("/tasklists/{taskListName}")
    public ResponseEntity<Void> deleteTaskList(
            @PathVariable String taskListName,
            Authentication authentication) {
        if (authentication == null) {
            throw new AppException("User not authenticated", HttpStatus.FORBIDDEN);
        }
        String username = authentication.getName();
        service.deleteTaskList(username, taskListName);
        return ResponseEntity.ok().build();
    }
}
