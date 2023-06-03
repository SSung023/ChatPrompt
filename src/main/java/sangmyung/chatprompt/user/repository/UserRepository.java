package sangmyung.chatprompt.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import sangmyung.chatprompt.user.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.name =:username")
    Optional<User> findUserByName(@Param("username") String username);

    @Query("select u from User u where u.identifier =:identifier")
    Optional<User> findUserByIdentifier(@Param("identifier") String identifier);

    @Query("select u from User u where u.name =:username and u.identifier =:identifier")
    Optional<User> findUserIsRegistered(@Param("username") String username, @Param("identifier") String identifier);

    @Query("select u from User u where u.taskStartIdx <= :taskId and u.taskEndIdx >= :taskId and u.id >= 3")
    Optional<User> findAssignedUserByTaskId(@Param("taskId") int assignedTaskId);
}
