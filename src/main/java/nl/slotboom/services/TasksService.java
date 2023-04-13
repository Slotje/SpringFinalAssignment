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
import nl.slotboom.repositories.TaskListsRepository;
import nl.slotboom.repositories.TasksRepository;
import nl.slotboom.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TasksService {
    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TaskListsRepository taskListsRepository;

    @Autowired
    private UserRepository userRepository;


    public TaskResponse createTaskForTaskList(String username, String taskListName, CreateTaskRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("No user found with username: " + username, HttpStatus.NOT_FOUND));
        TaskLists taskList = taskListsRepository.findByUserAndName(user, taskListName)
                .orElseThrow(() -> new AppException("No task list found with name: " + taskListName + " for user: " + username, HttpStatus.NOT_FOUND));
        if (!user.getUsername().equals(authentication.getName())) {
            throw new AppException("Unauthorized access", HttpStatus.UNAUTHORIZED);
        }
        var task = Tasks.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(TaskStatus.ToDo)
                .createdAt(new Date())
                .updatedAt(new Date())
                .taskList(taskList)
                .build();

        Tasks savedTask = tasksRepository.save(task);
        return TaskResponse.from(savedTask);
    }

    private Tasks findTask(String username, String taskName, String taskListName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found with name " + username, HttpStatus.NOT_FOUND));
        TaskLists taskList = taskListsRepository.findByUserAndName(user, taskListName)
                .orElseThrow(() -> new AppException("Task list not found with name " + taskListName, HttpStatus.NOT_FOUND));
        return tasksRepository.findByTaskListAndName(taskList, taskName)
                .orElseThrow(() -> new AppException("No task found with name: " + taskName + " in task list: " + taskListName, HttpStatus.NOT_FOUND));
    }

    public TaskResponse updateTaskStatus(String username, String taskName, String taskListName, UpdateTaskStatusRequest request) {
        Tasks task = findTask(username, taskName, taskListName);
        task.setStatus(request.getTaskStatus());
        task.setUpdatedAt(new Date());
        Tasks updatedTask = tasksRepository.save(task);
        return TaskResponse.from(updatedTask);
    }

    public TaskResponse updateTask(String username, String taskName, String taskListName, UpdateTaskRequest request) {
        Tasks task = findTask(username, taskName, taskListName);
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setUpdatedAt(new Date());
        Tasks updatedTask = tasksRepository.save(task);
        return TaskResponse.from(updatedTask);
    }

    public void deleteTask(String taskListName, String taskName, String username) {
        Tasks task = findTask(username, taskName, taskListName);
        tasksRepository.delete(task);
    }
}
