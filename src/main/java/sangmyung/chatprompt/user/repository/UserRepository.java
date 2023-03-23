package sangmyung.chatprompt.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangmyung.chatprompt.user.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name =:username")
    Optional<User> findUserByName(@Param("username") String username);

    @Query("select u from User u where u.identifier =:identifier")
    Optional<User> findUserByIdentifier(@Param("identifier") String identifier);
}
