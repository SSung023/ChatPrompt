package sangmyung.chatprompt.Home.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import sangmyung.chatprompt.Home.domain.Home;

public interface HomeRepository extends JpaRepository<Home, Long> {
}
