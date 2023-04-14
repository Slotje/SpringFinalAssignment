package nl.slotboom;

import nl.slotboom.controllers.TasklistsController;
import nl.slotboom.models.requests.CreateTasklistRequest;
import nl.slotboom.models.requests.UpdateTasklistRequest;
import nl.slotboom.models.responses.CreateTaskListResponse;
import nl.slotboom.models.responses.TaskListResponse;
import nl.slotboom.models.responses.UpdateTaskListResponse;
import nl.slotboom.services.TasklistsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TasklistsControllerTest {

    @Mock
    private TasklistsService tasklistsService;

    @InjectMocks
    private TasklistsController tasklistsController;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetTaskListsForUser() {
        // Create mock objects
        String username = "testuser";
        List<TaskListResponse> response = new ArrayList<>();
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(tasklistsService.getTaskListsForUser(username)).thenReturn(response);

        // Call method and assert expected result
        ResponseEntity<List<TaskListResponse>> result = tasklistsController.getTaskListsForUser(authentication);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testGetSpecificTaskListForUser() {
        // Create mock objects
        String username = "testuser";
        String taskListName = "testlist";
        TaskListResponse response = new TaskListResponse(1, "Tasklist1", "Description of Tasklist1", "testuser", new Date(), new Date(), null);
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(tasklistsService.getSpecificTaskListsForUser(username, taskListName)).thenReturn(response);

        // Call method and assert expected result
        ResponseEntity<TaskListResponse> result = tasklistsController.getSpecificTaskListForUser(taskListName, authentication);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testGetTaskListsForUser_Admin() {
        // Create mock objects
        String username = "testuser";
        List<TaskListResponse> response = new ArrayList<>();
        Mockito.when(tasklistsService.getTaskListsForUser(username)).thenReturn(response);

        // Call method and assert expected result
        ResponseEntity<List<TaskListResponse>> result = tasklistsController.getTaskListsForUser(username);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    public void testGetAllTaskListResponses_Admin() {
        // Create mock objects
        List<TaskListResponse> response = new ArrayList<>();
        Mockito.when(tasklistsService.getAllTaskListResponses()).thenReturn(response);

        // Call method and assert expected result
        ResponseEntity<List<TaskListResponse>> result = tasklistsController.getAllTaskListResponses();
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testCreateTaskListForUser() {
        // Create mock objects
        String username = "testuser";
        CreateTaskListResponse response = new CreateTaskListResponse(1, "Tasklist1", "Description of Tasklist1", "testuser", new Date(), new Date());
        CreateTasklistRequest request = new CreateTasklistRequest();
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(tasklistsService.createTaskListForUser(username, request)).thenReturn(response);

        // Call method and assert expected result
        ResponseEntity<CreateTaskListResponse> result = tasklistsController.createTaskListForUser(request, authentication);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }

    @Test
    public void testUpdateTaskListForUser() {
        // Create mock objects
        String username = "testuser";
        String taskListName = "testlist";
        var response = new UpdateTaskListResponse(1, "Tasklist1", "Description of Tasklist1", "testuser", new Date());
        UpdateTasklistRequest request = new UpdateTasklistRequest();
        Mockito.when(authentication.getName()).thenReturn(username);
        Mockito.when(tasklistsService.updateTaskListForUser(username, taskListName, request)).thenReturn(response);

        // Call method and assert expected result
        ResponseEntity<UpdateTaskListResponse> result = tasklistsController.updateTaskListForUser(taskListName, request, authentication);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(response, result.getBody());
    }
}