package nl.slotboom.services;

import nl.slotboom.exceptions.AppException;
import nl.slotboom.models.TaskLists;
import nl.slotboom.models.TaskStatus;
import nl.slotboom.models.Tasks;
import nl.slotboom.models.User;
import nl.slotboom.models.requests.CreateTaskRequest;
import nl.slotboom.models.requests.UpdateTaskRequest;
import nl.slotboom.models.requests.UpdateTaskStatusRequest;
import nl.slotboom.models.responses.TaskResponse;
import nl.slotboom.models.responses.UpdateTasksResponse;
import nl.slotboom.repositories.TaskListsRepository;
import nl.slotboom.repositories.TasksRepository;
import nl.slotboom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
public class TasksService {
    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TaskListsRepository taskListsRepository;

    @Autowired
    private UserRepository userRepository;

    // createTaskForTaskList: create a task in given tasklist
    public TaskResponse createTaskForTaskList(String username, String taskListName, CreateTaskRequest request) {
        // Find the user based on the given username, or throw an exception if no user is found
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("No user found with username: " + username, HttpStatus.NOT_FOUND));
        // Find the task list with the given name that belongs to the user, or throw an exception if not found
        TaskLists taskList = taskListsRepository.findByUserAndName(user, taskListName)
                .orElseThrow(() -> new AppException("No task list found with name: " + taskListName + " for user: " + username, HttpStatus.NOT_FOUND));
        // Check if there is already a task with the same name for the given task list, and throw an exception if so
        boolean isDuplicate = tasksRepository.existsByNameAndTaskList(request.getName(), taskList);
        if (isDuplicate) {
            throw new AppException("Task with name " + request.getName() + " already exists for tasklist " + taskListName + " for user " + username, HttpStatus.CONFLICT);
        }
        // Create a new task with the given properties
        var task = Tasks.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(TaskStatus.ToDo)
                .createdAt(new Date())
                .updatedAt(new Date())
                .taskList(taskList)
                .build();
        // Save the task to the database and return a TaskResponse object for it
        Tasks savedTask = tasksRepository.save(task);
        return TaskResponse.from(savedTask);
    }

    // findTask: This method finds a task by username, task name and task list name.
    private Tasks findTask(String username, String taskName, String taskListName) {
        // It retrieves the user from the userRepository based on the username, or throws a user is not found exception
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found with name " + username, HttpStatus.NOT_FOUND));
        // It retrieves the task list from the taskListsRepository based on the user and tasklist name
        TaskLists taskList = taskListsRepository.findByUserAndName(user, taskListName)
                .orElseThrow(() -> new AppException("Task list not found with name " + taskListName, HttpStatus.NOT_FOUND));
        // It retrieves the searched task from the tasksRepository based on the task list and task name
        return tasksRepository.findByTaskListAndName(taskList, taskName)
                .orElseThrow(() -> new AppException("No task found with name: " + taskName + " in task list: " + taskListName, HttpStatus.NOT_FOUND));
    }


    // addFileToTask: Used to add an attachment to a task
    public Resource addFileToTask(String username, String taskListName, String taskName, MultipartFile file) throws IOException {
        // It uses the findTask method to retrieve the Tasks object
        Tasks task = findTask(username, taskName, taskListName);
        // It updates or adds an attachment from the MultipartFile object
        task.setAttachment(file.getBytes());
        tasksRepository.save(task);
        return new ByteArrayResource(file.getBytes());
    }

    public Resource getFileFromTask(String username, String taskListName, String taskName) {
        // Retrieve the Tasks object
        Tasks task = findTask(username, taskName, taskListName);
        // Retrieve the attachment from the Tasks object
        byte[] attachment = task.getAttachment();
        // Create a ByteArrayResource from the attachment
        ByteArrayResource resource = new ByteArrayResource(attachment);
        return resource;
    }


    // updateTaskStatus: updates the status of a task
    public TaskResponse updateTaskStatus(String username, String taskName, String taskListName, UpdateTaskStatusRequest request) {
        // It uses the findTask method to retrieve the Tasks object
        Tasks task = findTask(username, taskName, taskListName);
        // It updates the status of the retrieved Tasks object with the TaskStatus from the UpdateTaskStatusRequest object
        task.setStatus(request.getTaskStatus());
        task.setUpdatedAt(new Date());
        // It saves the updated Tasks object to the database and returns a
        // TaskResponse object created from the updated Tasks object
        Tasks updatedTask = tasksRepository.save(task);
        return TaskResponse.from(updatedTask);
    }

    // updateTask: used to update a task
    public UpdateTasksResponse updateTask(String username, String taskName, String taskListName, UpdateTaskRequest request) {
        // Find the task to be updated
        Tasks task = findTask(username, taskName, taskListName);
        // Update the task's name, description and updatedAt fields with the new values
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setUpdatedAt(new Date());
        // Save the updated task to the database
        Tasks updatedTask = tasksRepository.save(task);
        // Convert the updated task to a TaskResponse object and return it
        return UpdateTasksResponse.from(updatedTask);
    }

    // deleteTask: used to delete a task
    public void deleteTask(String taskListName, String taskName, String username) {
        // Find the task to be deleted by calling the findTask method
        Tasks task = findTask(username, taskName, taskListName);
        // Delete the task from the database
        tasksRepository.delete(task);
    }
}
