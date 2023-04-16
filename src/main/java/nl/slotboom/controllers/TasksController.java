package nl.slotboom.controllers;

import nl.slotboom.models.requests.CreateTaskRequest;
import nl.slotboom.models.requests.UpdateTaskRequest;
import nl.slotboom.models.requests.UpdateTaskStatusRequest;
import nl.slotboom.models.responses.TaskResponse;
import nl.slotboom.models.responses.UpdateTasksResponse;
import nl.slotboom.services.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;

import java.io.IOException;

import static nl.slotboom.constants.APIConstants.*;

@RestController
@RequestMapping("/" + API + "/" + VERSION + "/" + TASKS_ENDPOINT)
@PreAuthorize("isAuthenticated()")
public class TasksController {


    @Autowired // Injecting TasksService bean
    private TasksService service;

    // This endpoint handles POST requests to create a new task for a specific task list
    @PostMapping("/{taskListName}/add")
    public ResponseEntity<TaskResponse> createTaskForTaskList(
            @PathVariable String taskListName,
            @RequestBody CreateTaskRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        TaskResponse response = service.createTaskForTaskList(username, taskListName, request);
        return ResponseEntity.ok(response);
    }

    // This endpoint handles POST requests to add a file to a specific task
    @PostMapping(value = "/addfile/{taskListName}/{taskName}", consumes = "multipart/form-data")
    public ResponseEntity<Resource> addFileToTask(
            @PathVariable String taskListName,
            @PathVariable String taskName,
            @RequestPart("file") MultipartFile file,
            Authentication authentication) throws IOException {
        String username = authentication.getName();
        Resource resource = service.addFileToTask(username, taskListName, taskName, file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getOriginalFilename() + "\"")
                .contentLength(file.getSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    // This endpoint handles GET requests to get a file from a specific task
    @GetMapping(value = "/getfile/{taskListName}/{taskName}")
    public ResponseEntity<Resource> getFileFromTask(
            @PathVariable String taskListName,
            @PathVariable String taskName,
            Authentication authentication) throws IOException {
        String username = authentication.getName();
        Resource resource = service.getFileFromTask(username, taskListName, taskName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + taskName + "\"")
                .contentLength(resource.contentLength())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }



    // This endpoint handles PUT requests to update the status of a specific task in a specific task list
    @PutMapping("/updatestatus/{taskListName}/{taskName}")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable String taskListName,
            @PathVariable String taskName,
            @RequestBody UpdateTaskStatusRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        TaskResponse response = service.updateTaskStatus(username, taskName, taskListName, request);
        return ResponseEntity.ok(response);
    }

    // This endpoint handles PUT requests to update the details of a specific task in a specific task list
    @PutMapping("/update/{taskListName}/{taskName}")
    public ResponseEntity<UpdateTasksResponse> updateTask(
            @PathVariable String taskName,
            @PathVariable String taskListName,
            @RequestBody UpdateTaskRequest request,
            Authentication authentication) {
        String username = authentication.getName();
        UpdateTasksResponse response = service.updateTask(username, taskName, taskListName, request);
        return ResponseEntity.ok(response);
    }

    // This endpoint handles DELETE requests to delete a specific task from a specific task list
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

