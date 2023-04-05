package nl.slotboom.repositories;

import nl.slotboom.models.TaskLists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskListsRepository extends JpaRepository<TaskLists, Integer> {
    Optional<TaskLists> findByName(String name);
}
