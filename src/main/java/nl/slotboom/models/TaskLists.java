package nl.slotboom.models;

import javax.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "task_lists")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskLists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id")
    private int user_id;

    @Column(name = "is_deleted", insertable = false)
    Boolean isDeleted;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "updated_at")
    private Date updatedAt;

    @OneToMany(mappedBy = "taskList")
    private List<Tasks> tasks;
}
