package nl.slotboom.repositories;

import nl.slotboom.models.TaskLists;
import nl.slotboom.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
// TasksRepository: Repository of the Tasks
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    Optional<Tasks> findByTaskListAndName(TaskLists taskList, String name);
    List<Tasks> findByTaskList(TaskLists taskList);
    boolean existsByNameAndTaskList(String name, TaskLists taskLists);
}
