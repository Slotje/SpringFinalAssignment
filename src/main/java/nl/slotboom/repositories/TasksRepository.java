package nl.slotboom.repositories;

import nl.slotboom.models.TaskLists;
import nl.slotboom.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    Optional<Tasks> findByName(String name);
    Optional<Tasks> findByTaskListAndName(TaskLists taskList, String taskName);
    List<Tasks> findByTaskList(TaskLists taskList);
}
