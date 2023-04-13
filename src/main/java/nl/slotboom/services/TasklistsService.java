package nl.slotboom.services;

import nl.slotboom.exceptions.AppException;
import nl.slotboom.models.TaskLists;
import nl.slotboom.models.Tasks;
import nl.slotboom.models.User;
import nl.slotboom.models.requests.CreateTasklistRequest;
import nl.slotboom.models.requests.UpdateTasklistRequest;
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

    public List<TaskListResponse> getAllTaskListResponses() {
        List<TaskLists> taskLists = taskListsRepository.findAll();
        List<TaskListResponse> taskListResponses = new ArrayList<>();

        for (TaskLists taskList : taskLists) {
            User user = taskList.getUser();
            List<Tasks> tasks = taskList.getTasks();
            TaskListResponse taskListResponse = TaskListResponse.from(taskList, user, tasks);
            taskListResponses.add(taskListResponse);
        }

        if (taskListResponses.isEmpty()) {
            throw new AppException("No task lists found", HttpStatus.NOT_FOUND);
        }

        return taskListResponses;
    }

    public List<TaskListResponse> getTaskListsForUser(String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        List<TaskLists> taskLists = taskListsRepository.findByUser(user);
        List<TaskListResponse> taskListResponses = new ArrayList<>();
        for (TaskLists taskList : taskLists) {
            List<Tasks> tasks = taskList.getTasks();
            taskListResponses.add(TaskListResponse.from(taskList, user, tasks));
        }
        return taskListResponses;
    }

    public TaskListResponse getSpecificTaskListsForUser(String username, String taskListName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        List<TaskLists> taskLists = taskListsRepository.findByUserId(user.getId());
        TaskLists taskList = taskLists.stream()
                .filter(list -> list.getName().equals(taskListName))
                .findFirst()
                .orElseThrow(() -> new AppException("Task list not found", HttpStatus.NOT_FOUND));

        List<Tasks> tasks = taskList.getTasks();
        return TaskListResponse.from(taskList, user, tasks);
    }


    public TaskListResponse createTaskListForUser(String username, CreateTasklistRequest request) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        User user = optionalUser.orElseThrow(() -> new AppException("No user found with username: " + username, HttpStatus.NOT_FOUND));
        boolean isDuplicate = taskListsRepository.existsByNameAndUser(request.getName(), user);
        if (isDuplicate) {
            throw new AppException("Task list with name " + request.getName() + " already exists for user " + username, HttpStatus.CONFLICT);
        }
        var taskList = TaskLists.builder()
                .name(request.getName())
                .description(request.getDescription())
                .user(user)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
        TaskLists savedTaskList = taskListsRepository.save(taskList);
        List<Tasks> tasks = taskList.getTasks();
        return TaskListResponse.from(savedTaskList, user, tasks);
    }

    public UpdateTaskListResponse updateTaskListForUser(String username, String taskListName, UpdateTasklistRequest updateTaskListRequest) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
        TaskLists taskList = taskListsRepository.findByUserAndName(user, taskListName)
                .orElseThrow(() -> new AppException("Task list not found", HttpStatus.NOT_FOUND));
        if (!taskList.getName().equals(taskListName)) {
            throw new AppException("Task list name cannot be changed", HttpStatus.BAD_REQUEST);
        }
        taskList.setName(updateTaskListRequest.getName());
        taskList.setDescription(updateTaskListRequest.getDescription());
        TaskLists updatedTaskList = taskListsRepository.save(taskList);
        return UpdateTaskListResponse.from(updatedTaskList, user);
    }

    public void deleteTaskList(String username, String taskListName) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException("User not found with name " + username, HttpStatus.NOT_FOUND));
        TaskLists taskList = taskListsRepository.findByUserAndName(user, taskListName)
                .orElseThrow(() -> new AppException("Task list not found with name " + taskListName, HttpStatus.NOT_FOUND));
        List<Tasks> tasks = tasksRepository.findByTaskList(taskList);
        tasksRepository.deleteAll(tasks);
        taskListsRepository.delete(taskList);
    }
}

