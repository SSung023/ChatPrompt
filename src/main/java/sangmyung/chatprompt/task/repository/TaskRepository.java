package sangmyung.chatprompt.task.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sangmyung.chatprompt.task.domain.Task;

public interface TaskRepository extends JpaRepository<Long, Task> {
}
