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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TasksService {
    @Autowired
    private TasksRepository tasksRepository;

    @Autowired
    private TaskListsRepository taskListsRepository;



    public TaskResponse createTaskForTaskList(String taskListName, CreateTaskRequest request) {
        TaskLists taskList = taskListsRepository.findByName(taskListName)
                .orElseThrow(() -> new AppException("No task list found with name: " + taskListName, HttpStatus.NOT_FOUND));

        var task = Tasks.builder()
                .name(request.getName())
                .description(request.getDescription())
                .status(TaskStatus.ToDo)
                .isDeleted(false)
                .createdAt(new Date())
                .updatedAt(new Date())
                .taskList(taskList)
                .build();

        Tasks savedTask = tasksRepository.save(task);
        return TaskResponse.from(savedTask);
    }

    public TaskResponse updateTaskStatus(String taskName, UpdateTaskStatusRequest request) {
        Tasks task = tasksRepository.findByName(taskName)
                .orElseThrow(() -> new AppException("No task found with name: " + taskName, HttpStatus.NOT_FOUND));
        task.setStatus(request.getTaskStatus());
        task.setUpdatedAt(new Date());
        Tasks updatedTask = tasksRepository.save(task);
        return TaskResponse.from(updatedTask);
    }

    public TaskResponse updateTask(String taskName, UpdateTaskRequest request) {
        Tasks task = tasksRepository.findByName(taskName)
                .orElseThrow(() -> new AppException("No task found with name: " + taskName, HttpStatus.NOT_FOUND));
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setUpdatedAt(new Date());
        Tasks updatedTask = tasksRepository.save(task);
        return TaskResponse.from(updatedTask);
    }

    public void deleteTask(String taskListName, String taskName, String username) {
        // Find task list by name
        TaskLists taskList = taskListsRepository.findByName(taskListName)
                .orElseThrow(() -> new AppException("No task list found with name: " + taskListName, HttpStatus.NOT_FOUND));
        // Check if authenticated user is the owner of the task list
        if (!taskList.getUser().getUsername().equals(username)) {
            throw new AppException("You are not authorized to delete tasks in this task list.", HttpStatus.FORBIDDEN);
        }
        // Find task by task list and name
        Tasks task = tasksRepository.findByTaskListAndName(taskList, taskName)
                .orElseThrow(() -> new AppException("No task found with name: " + taskName + " in task list: " + taskListName, HttpStatus.NOT_FOUND));
        User user = new User();
        // Check if authenticated user is the owner of the task
        if (!user.getUsername().equals(username)) {
            throw new AppException("You are not authorized to delete this task.", HttpStatus.FORBIDDEN);
        }
        // Delete task
        tasksRepository.delete(task);
    }

}
