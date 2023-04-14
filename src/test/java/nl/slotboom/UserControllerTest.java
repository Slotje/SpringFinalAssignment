package nl.slotboom;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import nl.slotboom.controllers.UserController;
import nl.slotboom.models.Role;
import nl.slotboom.models.User;
import nl.slotboom.models.requests.UpdateUserRequest;
import nl.slotboom.models.responses.UserResponse;
import nl.slotboom.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

class UserControllerTest {

    @Mock
    private UserService service;

    @InjectMocks
    private UserController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetUserProfile() {
        // Mock the dependencies
        String username = "TestUser";
        UserResponse expectedResponse = new UserResponse("TestUser", "USER", new Date(), new Date());
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "TestUser", null);
        // Set up the SecurityContextHolder
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Set up the mock UserService object to return a known User object
        when(service.getUserProfile(username)).thenReturn(expectedResponse);
        // Call the getUserProfile method with the mock request and authentication objects
        ResponseEntity<UserResponse> response = controller.getUserProfile(authentication);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResponse.getUsername(), response.getBody().getUsername());
        assertEquals(expectedResponse.getRole(), response.getBody().getRole());
        assertEquals(expectedResponse.getCreatedAt(), response.getBody().getCreatedAt());
        assertEquals(expectedResponse.getUpdatedAt(), response.getBody().getUpdatedAt());
    }

    @Test
    void testUpdateUser() {
        // Mock the dependencies
        String username = "testuser";
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, "TestUser", null);

        // Set up the mock UserService object to return a known User object
        UpdateUserRequest request = new UpdateUserRequest();
        User mockUser = new User();
        mockUser.setUsername(username);
        mockUser.setRole(Role.USER);
        Mockito.when(service.updateUser(username, request, authentication)).thenReturn(mockUser);

        // Create an instance of the controller and set the mocked UserService object as a dependency
        ReflectionTestUtils.setField(controller, "service", service);

        // Call the updateUser() method with the mock request and authentication objects
        ResponseEntity<UserResponse> response = controller.updateUser(request, authentication);

        // Assert that the returned ResponseEntity contains the expected UserResponse object
        assertEquals(mockUser.getUsername(), response.getBody().getUsername());
    }
}

