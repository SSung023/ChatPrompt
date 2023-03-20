package sangmyung.chatprompt.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangmyung.chatprompt.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.identifier = 'Z'")
    User findOriginUser();

    @Query("select u from User u where u.name =:username")
    User findUserByName(@Param("username") String username);
}
