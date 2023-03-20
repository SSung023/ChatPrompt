package sangmyung.chatprompt.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangmyung.chatprompt.task.domain.IOPairs;

import java.util.List;

public interface IoPairRepository extends JpaRepository<IOPairs, Long> {

    @Query("select i from IOPairs i where i.task.id =:taskId")
    List<IOPairs> findPairsByTaskId(@Param("taskId") Long taskId);
}
