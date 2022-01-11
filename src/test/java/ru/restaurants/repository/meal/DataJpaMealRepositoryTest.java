package ru.restaurants.repository.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.restaurants.MealTestData;
import ru.restaurants.model.Meal;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.repository.restaurant.RestaurantRepository;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.MealTestData.NOT_FOUND;
import static ru.restaurants.MealTestData.*;
import static ru.restaurants.RestaurantTestData.*;

class DataJpaMealRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected MealRepository repository;
    @Autowired
    protected RestaurantRepository repositoryRestaurant;

    @Test
    public void create() {
        Meal created = repository.save(MealTestData.getNew(), RESTAURANT_2_ID);
        int newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(repository.get(newId), newMeal);
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                repository.save(new Meal(null, meal1.getName(), 222, restaurant1), RESTAURANT_1_ID));
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
    void getWithRestaurant() {
        Meal meal = repository.getWithRestaurant(ADMIN_ID);
        MEAL_MATCHER.assertMatch(meal, meal6);
        RESTAURANT_MATCHER.assertMatch(meal.getRestaurant(), meal6.getRestaurant());
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> repository.get(NOT_FOUND));
    }

    @Test
    void update() {
        Meal updated = MealTestData.getUpdated();
        repository.update(updated, RESTAURANT_2_ID);
        MEAL_MATCHER.assertMatch(repository.get(MEAL_ID), MealTestData.getUpdated());
    }

    @Test
    void getAllByRestaurant() {
        List<Meal> all = repository.getAllByRestaurant(RESTAURANT_1_ID);
        MEAL_MATCHER.assertMatch(all, meals);
    }
}