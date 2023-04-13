package nl.slotboom.converters;

import nl.slotboom.models.TaskStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.stream.Stream;


@Converter(autoApply = true)
public class TaskStatusConverter implements AttributeConverter<TaskStatus, String> {

    // convertToDatabaseColumn: Converts a TaskStatus object to its corresponding database column value
    @Override
    public String convertToDatabaseColumn(TaskStatus taskStatus) {
        if (taskStatus == null) {
            return null;
        }
        return taskStatus.getStatus();
    }

    // convertToEntityAttribute: Converts a String value from the database to a TaskStatus enum value in the entity class
    @Override
    public TaskStatus convertToEntityAttribute(String taskStatus) {
        if (taskStatus == null) {
            return null;
        }

        return Stream.of(TaskStatus.values())
                .filter(c -> c.getStatus().equals(taskStatus))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}