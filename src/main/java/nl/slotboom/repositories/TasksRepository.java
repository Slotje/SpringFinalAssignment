package nl.slotboom.repositories;

import nl.slotboom.models.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, UUID> {

}