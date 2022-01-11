package ru.restaurants.repository.user;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurants.model.Choice;
import ru.restaurants.model.User;

import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudUserRepository extends JpaRepository<User, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM User u WHERE u.id=:id")
    int delete(@Param("id") int id);

    Optional<User> getByEmail(String email);
}
