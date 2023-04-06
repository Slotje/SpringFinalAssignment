package nl.slotboom.controllers;

import nl.slotboom.models.TaskLists;
import nl.slotboom.models.User;
import nl.slotboom.repositories.TaskListsRepository;
import nl.slotboom.repositories.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public class TasklistController {

//    UserRepository userRepository;
//    TaskListsRepository taskListsRepository;
//    @GetMapping("/users/{username}/tasklists")
//    public List<TaskLists> getTaskListsForUser(@PathVariable String username) {
//        User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
//        return taskListsRepository.findByName(user.getUsername());
//    }

}
