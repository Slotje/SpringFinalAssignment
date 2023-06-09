package nl.slotboom.repositories;

import nl.slotboom.models.TaskLists;
import nl.slotboom.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
// TaskListsRepository: Repository of the Tasklists
public interface TaskListsRepository extends JpaRepository<TaskLists, Integer> {
    List<TaskLists> findByUser(User user);
    Optional<TaskLists> findByUserAndName(User user, String name);
    boolean existsByNameAndUser(String name, User user);
    List<TaskLists> findByUserId(int userId);
}



