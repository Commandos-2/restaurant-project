package ru.restaurants.repository.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurants.RestaurantTestData;
import ru.restaurants.model.Meal;
import ru.restaurants.model.Restaurant;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.repository.meal.MealRepository;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.MealTestData.*;
import static ru.restaurants.RestaurantTestData.NOT_FOUND;
import static ru.restaurants.RestaurantTestData.*;
import static ru.restaurants.TestUtil.SetDateMeal;

class DataJpaRestaurantRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected MealRepository mealRepository;

    @Test
    public void create() {
        Restaurant created = restaurantRepository.save(RestaurantTestData.getNew());
        int newId = created.getId();
        Restaurant newRestaurant = RestaurantTestData.getNew();
        newRestaurant.setId(newId);
        RESTAURANT_MATCHER.assertMatch(created, newRestaurant);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(newId), newRestaurant);
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                restaurantRepository.save(new Restaurant(null, "Direkte")));
    }

    @Test
    void delete() {
        restaurantRepository.delete(RESTAURANT_1_ID);
        assertThrows(NotFoundException.class, () -> restaurantRepository.get(RESTAURANT_1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantRepository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Restaurant restaurant = restaurantRepository.get(RESTAURANT_2_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant2);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> restaurantRepository.get(NOT_FOUND));
    }

    @Test
    void update() {
        Restaurant updated = RestaurantTestData.getUpdated();
        restaurantRepository.update(updated);
        RESTAURANT_MATCHER.assertMatch(restaurantRepository.get(RESTAURANT_1_ID), RestaurantTestData.getUpdated());
    }

    @Test
    void getAll() {
        List<Restaurant> all = restaurantRepository.getAll();
        RESTAURANT_MATCHER.assertMatch(all, restaurant1, restaurant2);
    }

    @Test
    void getWithMeals() {
        Restaurant restaurant = restaurantRepository.getWithMeals(RESTAURANT_1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant1);
        MEAL_MATCHER.assertMatch(restaurant.getMeals(), mealsRestaurant1);
    }

    @Test
    void getAllWithMeals() {
        List<Restaurant> all = restaurantRepository.getAllWithMeals();
        RESTAURANT_MATCHER.assertMatch(all, restaurant1, restaurant2);
        List<Meal> list = new ArrayList<>();
        for (Restaurant restaurant : all) {
            list.addAll(restaurant.getMeals());
        }
        MEAL_MATCHER.assertMatch(list, meals);
    }

    @Test
    void getWithMealsToday() {
        SetDateMeal(mealRepository);
        Restaurant restaurant = restaurantRepository.getWithMealsToday(RESTAURANT_1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant1);
        MEAL_MATCHER.assertMatch(restaurant.getMeals(), meal3, meal1, meal5);
    }

    @Test
    void getAllWithMealsToday() {
        SetDateMeal(mealRepository);
        List<Restaurant> all = restaurantRepository.getAllWithMealsToday();
        RESTAURANT_MATCHER.assertMatch(all, restaurant1, restaurant2);
        List<Meal> list = new ArrayList<>();
        for (Restaurant restaurant : all) {
            list.addAll(restaurant.getMeals());
        }
        MEAL_MATCHER.assertMatch(list, meal3, meal1, meal5, meal6, meal9, meal8);
    }
}