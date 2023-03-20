package sangmyung.chatprompt.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sangmyung.chatprompt.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
