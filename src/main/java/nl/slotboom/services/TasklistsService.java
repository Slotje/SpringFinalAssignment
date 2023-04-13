package nl.slotboom.services;

import nl.slotboom.exceptions.AppException;
import nl.slotboom.models.TaskLists;
import nl.slotboom.models.Tasks;
import nl.slotboom.models.User;
import nl.slotboom.models.requests.CreateTasklistRequest;
import nl.slotboom.models.requests.UpdateTasklistRequest;
import nl.slotboom.models.responses.CreateTaskListResponse;
import nl.slotboom.models.responses.TaskListResponse;
import nl.slotboom.models.responses.UpdateTaskListResponse;
import nl.slotboom.repositories.TaskListsRepository;
import nl.slotboom.repositories.TasksRepository;
import nl.slotboom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TasklistsService {
    @Autowired
    private TaskListsRepository taskListsRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TasksRepository tasksRepository;

    // getSpecificTaskListsForUser: get a specific tasklist of the authenticated user
    public TaskListResponse getSpecificTaskListsForUser(String username, String taskListName) {
        // Find the user with the given username from the user repository, or throw an exception if not found.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        // Find all task lists associated with the user.
        List<TaskLists> taskLists = taskListsRepository.findByUserId(user.getId());
        // Find the specific task list with the given name, or throw an exception if not found.
        TaskLists taskList = taskLists.stream()
                .filter(list -> list.getName().equals(taskListName))
                .findFirst()
                .orElseThrow(() -> new AppException("Task list not found", HttpStatus.NOT_FOUND));
        // Get the tasks associated with the task list.
        List<Tasks> tasks = taskList.getTasks();
        // Create a TaskListResponse object from the task list, user, and tasks, and return it.
        return TaskListResponse.from(taskList, user, tasks);
    }

    // getAllTaskListResponses: get all the tasklists that are created (admin)
    public List<TaskListResponse> getAllTaskListResponses() {
        // Retrieve all task lists from the repository.
        List<TaskLists> taskLists = taskListsRepository.findAll();
        // Create an empty list to hold the TaskListResponse objects.
        List<TaskListResponse> taskListResponses = new ArrayList<>();
        // Loop through each TaskLists object in the list of task lists.
        for (TaskLists taskList : taskLists) {
            User user = taskList.getUser();
            List<Tasks> tasks = taskList.getTasks();
            TaskListResponse taskListResponse = TaskListResponse.from(taskList, user, tasks);
            taskListResponses.add(taskListResponse);
        }
        // Check if the list of TaskListResponse objects is empty.
        if (taskListResponses.isEmpty()) {
            // Throw an exception with a message and status code indicating that no task lists were found.
            throw new AppException("No task lists found", HttpStatus.NOT_FOUND);
        }
        // Return the list of TaskListResponse objects.
        return taskListResponses;
    }

    // getTaskListsForUser: get tasklist for specific user (admin)
    public List<TaskListResponse> getTaskListsForUser(String username) {
        // Find the user with the given username from the user repository, or throw an exception if not found.
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        // Find all task lists associated with the user.
        List<TaskLists> taskLists = taskListsRepository.findByUser(user);
        List<TaskListResponse> taskListResponses = new ArrayList<>();
        // Loop through each TaskLists object in the list of task lists.
        for (TaskLists taskList : taskLists) {
            List<Tasks> tasks = taskList.getTasks();
            taskListResponses.add(TaskListResponse.from(taskList, user, tasks));
        }
        // Return the list of TaskListResponse objects.
        return taskListResponses;
    }

    // createTaskListForUser: create tasklist for authenticated user
    public CreateTaskListResponse createTaskListForUser(String username, CreateTasklistRequest request) {
        // Find the user with the given username from the user repository, or throw an exception if not found.
        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = optionalUser.orElseThrow(() -> new AppException("No user found with username: " + username, HttpStatus.NOT_FOUND));
        // Check if a task list with the given name already exists for the user, and throw an exception if it does.
        boolean isDuplicate = taskListsRepository.existsByNameAndUser(request.getName(), user);
        if (isDuplicate) {
            throw new AppException("Task list with name " + request.getName() + " already exists for user " + username, HttpStatus.CONFLICT);
        }
        // Create a new TaskLists object with the given name, description, user, and creation and update times.
        var taskList = TaskLists.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        // Save the new task list to the task lists repository.
        TaskLists savedTaskList = taskListsRepository.save(taskList);
        // Create a CreateTaskListResponse object from the saved task list and user, and return it.
        return CreateTaskListResponse.from(savedTaskList, user);
    }


    public UpdateTaskListResponse updateTaskListForUser(String username, String taskListName, UpdateTasklistRequest updateTaskListRequest) {
        // Find the user with the given username from the user repository, or throw an exception if not found.
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        // Find the task list with the given name and user, or throw an exception if not found.
        TaskLists taskList = taskListsRepository.findByUserAndName(user, taskListName)
                .orElseThrow(() -> new AppException("Task list not found", HttpStatus.NOT_FOUND));
        // Check if the new task list name is the same as the old one, and throw an exception if it is not.
        if (!taskList.getName().equals(taskListName)) {
            throw new AppException("Task list name cannot be changed", HttpStatus.BAD_REQUEST);
        }
        // Update the task list's name and description with the new values from the update request.
        taskList.setName(updateTaskListRequest.getName());
        taskList.setDescription(updateTaskListRequest.getDescription());
        // Save the updated task list to the task lists repository.
        TaskLists updatedTaskList = taskListsRepository.save(taskList);
        // Create an UpdateTaskListResponse object from the updated task list and user, and return it.
        return UpdateTaskListResponse.from(updatedTaskList, user);
    }

    public void deleteTaskList(String username, String taskListName) {
        // Find the user associated with the given username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found with name " + username, HttpStatus.NOT_FOUND));
        // Find the task list associated with the given name and user
        TaskLists taskList = taskListsRepository.findByUserAndName(user, taskListName)
                .orElseThrow(() -> new AppException("Task list not found with name " + taskListName, HttpStatus.NOT_FOUND));
        // Find all tasks associated with the task list and delete them
        List<Tasks> tasks = tasksRepository.findByTaskList(taskList);
        tasksRepository.deleteAll(tasks);
        // Delete the task list
        taskListsRepository.delete(taskList);
    }
}

