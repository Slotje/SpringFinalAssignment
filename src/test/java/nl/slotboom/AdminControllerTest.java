package nl.slotboom;

import nl.slotboom.controllers.AdminController;
import nl.slotboom.models.Role;
import nl.slotboom.models.User;
import nl.slotboom.models.requests.UpdateUserRoleRequest;
import nl.slotboom.models.responses.UserResponse;
import nl.slotboom.repositories.UserRepository;
import nl.slotboom.services.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class AdminControllerTest {
    @Mock
    private AdminService service;

    @Mock
    private UserRepository repository;

    @InjectMocks
    private AdminController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllUsers() {
        // Create mock objects
        List<UserResponse> mockResponseList = new ArrayList<>();
        UserResponse mockResponse1 = new UserResponse("TestUser1", "USER", new Date(), new Date());
        UserResponse mockResponse2 = new UserResponse("TestUser2", "ADMIN", new Date(), new Date());
        mockResponseList.add(mockResponse1);
        mockResponseList.add(mockResponse2);
        when(service.getAllUserResponses()).thenReturn(mockResponseList);
        List<UserResponse> responseList = controller.getAllUsers();

        // Call method and assert expected result
        assertEquals(mockResponseList.size(), responseList.size());
        assertEquals(mockResponse1, responseList.get(0));
        assertEquals(mockResponse2, responseList.get(1));
    }

    @Test
    public void testUpdateUserRole() {
        // Create mock objects
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn("adminuser");
        UpdateUserRoleRequest request = new UpdateUserRoleRequest();

        // Create mock users
        User currentUser = new User();
        currentUser.setUsername("adminuser");
        currentUser.setRole(Role.ADMIN);
        User existingUser = new User();
        existingUser.setUsername("testuser");
        existingUser.setRole(Role.USER);

        // Set up mock UserRepository to return mock users
        Mockito.when(repository.findByUsername("adminuser")).thenReturn(Optional.of(currentUser));
        Mockito.when(repository.findByUsername("testuser")).thenReturn(Optional.of(existingUser));

        // Set up mock AdminService to return updated user
        User updatedUser = new User();
        updatedUser.setUsername("testuser");
        updatedUser.setRole(Role.ADMIN);
        Mockito.when(service.updateUserRole("testuser", request, authentication)).thenReturn(updatedUser);

        // Call method and assert expected result
        ResponseEntity<UserResponse> response = controller.updateUserRole("testuser", request, authentication);
        UserResponse userResponse = response.getBody();
        assertEquals("testuser", userResponse.getUsername());
        assertEquals("ADMIN", userResponse.getRole());
    }
}