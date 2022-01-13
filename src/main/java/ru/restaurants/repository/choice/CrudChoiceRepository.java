package ru.restaurants.repository.choice;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurants.model.Choice;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudChoiceRepository extends JpaRepository<Choice, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Choice u WHERE u.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT u FROM Choice u WHERE u.id=?1")
    Optional<Choice> get(int id);

    @EntityGraph(attributePaths = {"user", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT u FROM Choice u WHERE u.user.id=:userId ORDER BY u.registered DESC")
    List<Choice> getAllByUserId(@Param("userId") int userId);

    @EntityGraph(attributePaths = {"user", "restaurant"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("FROM Choice ORDER BY registered DESC")
    List<Choice> getAll();
}