package ru.restaurants.repository.meal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurants.model.Meal;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT m FROM Meal m WHERE m.restaurant.id=:restaurantId ORDER BY m.name")
    List<Meal> getAllByRestaurant(@Param("restaurantId") int restaurantId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.restaurant WHERE m.id=:id")
    Optional<Meal> getWithRestaurant(@Param("id") int id);
}
