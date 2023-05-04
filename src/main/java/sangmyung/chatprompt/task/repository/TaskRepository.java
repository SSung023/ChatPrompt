package sangmyung.chatprompt.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangmyung.chatprompt.task.domain.Task;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("select t from Task t where t.id =:taskId")
    Optional<Task> findTaskByPK(@Param("taskId") Long taskId);

    @Query("select t from Task t where t.taskNum =:taskNum")
    Optional<Task> findTaskByTaskNum(@Param("taskNum") int taskNum);

    @Query("select t from Task t where t.assignedTaskId =:assignedTaskId")
    Optional<Task> findTaskByAssignedId(@Param("assignedTaskId") Long assignedTaskId);

    @Query("select t.id from Task t where t.assignedTaskId =:assignedTaskId")
    Optional<Long> findTaskPK(@Param("assignedTaskId") Long assignedTaskId);

    @Query("select t.assignedTaskId from Task t where t.id =:taskId")
    Optional<Long> findTaskAssignedId(@Param("taskId") Long taskId);
}
