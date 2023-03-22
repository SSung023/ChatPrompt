package sangmyung.chatprompt.assignment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangmyung.chatprompt.assignment.domain.Assignment;

import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // userId, taskId를 통해 작성한 Assignment를 반환 -> 유사지시문1,2
    @Query("select a from Assignment a where a.user.id =:userId and a.taskId =:taskId")
    Optional<Assignment> getAssignment(@Param("userId") Long userId, @Param("taskId") Long taskId);

    // userId, taskId, ioIndex를 통해 작성한 입출력 정보(Assignment)를 반환 -> 입출력 작성
    @Query("select a from Assignment a where a.user.id =:userId and a.taskId =:taskId and a.ioPairs.idx =:ioIndex")
    Optional<Assignment> getIOAssignment(@Param("userId") Long userId, @Param("taskId") Long taskId, @Param("ioIndex") int ioIndex);
}
