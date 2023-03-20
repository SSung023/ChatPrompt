package sangmyung.chatprompt.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangmyung.chatprompt.assignment.domain.Assignment;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    @Query("select a from Assignment a where a.user.id =:userId and a.taskId =:taskId")
    Optional<Assignment> getAssignment(@Param("userId") Long userId, @Param("taskId") Long taskId);
}
