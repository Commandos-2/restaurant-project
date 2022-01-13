package ru.restaurants.repository.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.restaurants.RestaurantTestData;
import ru.restaurants.model.Meal;
import ru.restaurants.model.Restaurant;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.MealTestData.*;
import static ru.restaurants.RestaurantTestData.*;
import static ru.restaurants.RestaurantTestData.NOT_FOUND;

class DataJpaRestaurantRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected RestaurantRepository repository;

    @Test
    public void create() {
        Restaurant created = repository.save(RestaurantTestData.getNew());
        int newId = created.getId();
        Restaurant newRestaurant = RestaurantTestData.getNew();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(repository.get(newId), newRestaurant);
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                repository.save(new Restaurant(null, "Direkte")));
    }

    @Test
    void delete() {
        repository.delete(RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () -> repository.get(RESTAURANT_1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> repository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Restaurant restaurant = repository.get(RESTAURANT_2_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant2);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> repository.get(NOT_FOUND));
    }

    @Test
    void update() {
        Restaurant updated = RestaurantTestData.getUpdated();
        repository.update(updated);
        RESTAURANT_MATCHER.assertMatch(repository.get(RESTAURANT_1_ID), RestaurantTestData.getUpdated());
    }

    @Test
    void getAll() {
        List<Restaurant> all = repository.getAll();
        RESTAURANT_MATCHER.assertMatch(all, restaurant1, restaurant2);
    }

    @Test
    void getWithMeals() {
        Restaurant restaurant = repository.getWithMeals(RESTAURANT_1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant1);
        MEAL_MATCHER.assertMatch(restaurant.getMeals(), mealsRestaurant1);
    }

    @Test
    void getAllWithMeals() {
        List<Restaurant> all = repository.getAllWithMeals();
        RESTAURANT_MATCHER.assertMatch(all, restaurant1, restaurant2);
        List<Meal> list = new ArrayList<>();
        for (Restaurant restaurant : all) {
            list.addAll(restaurant.getMeals());
        }
        MEAL_MATCHER.assertMatch(list,meals);
    }
}