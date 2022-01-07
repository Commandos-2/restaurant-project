package ru.restaurants.repository.meal;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.restaurants.model.Meal;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.repository.restaurant.RestaurantRepository;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.MealTestData.*;
import static ru.restaurants.RestaurantTestData.RESTAURANT_1_ID;

class DataJpaMealRepositoryTest extends AbstractRepositoryTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    protected MealRepository repository;
    @Autowired
    protected RestaurantRepository repositoryRestaurant;

    @Test
    public void create() {
        Meal created = repository.save(getNew(),RESTAURANT_1_ID);
        int newId = created.getId();
        Meal newMeal = getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(repository.get(newId), newMeal);
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                repository.save(new Meal(null, meal1.getName(), 222), MEAL_ID));
    }

    @Test
    void delete() {
        repository.delete(MEAL_ID);
        assertThrows(NotFoundException.class, () -> repository.get(MEAL_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> repository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Meal Meal = repository.get(ADMIN_ID);
        MEAL_MATCHER.assertMatch(Meal, meal6);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> repository.get(NOT_FOUND));
    }

    @Test
    void update() {
        Meal updated = getUpdated();
        repository.update(updated,RESTAURANT_1_ID);
        MEAL_MATCHER.assertMatch(repository.get(MEAL_ID), getUpdated());
    }

    @Test
    void getAll() {
        List<Meal> all = repository.getAll(RESTAURANT_1_ID);
        MEAL_MATCHER.assertMatch(all, meals);
    }
}