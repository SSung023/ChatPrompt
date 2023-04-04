package sangmyung.chatprompt.assignment.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangmyung.chatprompt.assignment.domain.Assignment;

import java.util.List;
import java.util.Optional;

public interface AssignmentRepository extends JpaRepository<Assignment, Long> {

    // userId, taskId를 통해 작성한 Assignment를 반환 -> 유사지시문1,2
    @Query("select a from Assignment a where a.user.id =:userId and a.taskId =:taskId and a.ioPairs.id = null")
    Optional<Assignment> getAssignment(@Param("userId") Long userId, @Param("taskId") Long taskId);

    // 특정 Task의 subidx를 반환
    @Query("select a from Assignment a where a.user.id =:userId and a.taskId =:taskId and a.taskSubIdx=:taskSubIdx")
    Optional<Assignment> getSubIdxAssignment(@Param("userId") Long userId, @Param("taskId") Long taskId, @Param("taskSubIdx") Long taskSubIdx);

    // 특정 Task에 대해 사용자들이 작성한 모든 유사지시문 set을 반환
    @Query("select a from Assignment a where a.user.id != 1 and a.taskId =:taskId and a.ioPairs.id = null")
    List<Assignment> getAssignmentList(@Param("taskId") Long taskId);

    // 특정 Task에 대해 특정 사용자가 작성한 유사지시문 10개를 반환
    @Query("select a from Assignment a where a.user.id =:userId and a.taskId =:taskId and a.ioPairs.id = null and a.taskSubIdx != null order by a.taskSubIdx asc")
    List<Assignment> getWrittenAssignList(@Param("userId") Long userId, @Param("taskId") Long taskId, Pageable pageable);

    // 특정 Task에 대해 특정 사용자가 작성한 입출력을 반환
    @Query("select a from Assignment a where a.user.id =:userId and a.taskId =:taskId and a.ioPairs.id != null")
    List<Assignment> getIOPairList(@Param("userId") Long userId, @Param("taskId") Long taskId);


    // userId, taskId, ioIndex를 통해 작성한 입출력 정보(Assignment)를 반환 -> 입출력 작성
    @Query("select a from Assignment a where a.user.id =:userId and a.taskId =:taskId and a.ioPairs.idx =:ioIndex")
    Optional<Assignment> getIOAssignment(@Param("userId") Long userId, @Param("taskId") Long taskId, @Param("ioIndex") int ioIndex);


    // 입출력 assignment에서 특정 사용자의 특정 Task에 대해 검증이 완료된 입출력의 개수 반환
    @Query("select count(*) from Assignment a where a.user.id = :userId and a.taskId =:taskId and a.ioPairs.id != null")
    Integer getValidatedIOCnt(@Param("userId") Long userId, @Param("taskId") Long taskId);
}
