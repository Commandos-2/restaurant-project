package ru.restaurants.repository.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
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
import static ru.restaurants.TestUtil.SetDateMeal;

class DataJpaMealRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected MealRepository mealRepository;
    @Autowired
    protected RestaurantRepository repositoryRestaurant;

    @Test
    @Transactional
    public void create() {
        Meal created = mealRepository.save(MealTestData.getNew(), RESTAURANT_2_ID);
        int newId = created.getId();
        Meal newMeal = MealTestData.getNew();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(mealRepository.get(newId), newMeal);
        RESTAURANT_MATCHER.assertMatch(mealRepository.get(newId).getRestaurant(), newMeal.getRestaurant());
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                mealRepository.save(new Meal(null, meal1.getName(), 222, restaurant1), RESTAURANT_1_ID));
    }

    @Test
    void delete() {
        mealRepository.delete(MEAL_ID);
        assertThrows(NotFoundException.class, () -> mealRepository.get(MEAL_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> mealRepository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Meal Meal = mealRepository.get(ADMIN_ID);
        MEAL_MATCHER.assertMatch(Meal, meal6);
    }

    @Test
    void getWithRestaurant() {
        Meal meal = mealRepository.getWithRestaurant(ADMIN_ID);
        MEAL_MATCHER.assertMatch(meal, meal6);
        RESTAURANT_MATCHER.assertMatch(meal.getRestaurant(), meal6.getRestaurant());
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> mealRepository.get(NOT_FOUND));
    }

    @Test
    @Transactional
    void update() {
        Meal updated = MealTestData.getUpdated();
        mealRepository.update(updated, RESTAURANT_2_ID);
        MEAL_MATCHER.assertMatch(mealRepository.get(MEAL_ID), MealTestData.getUpdated());
        RESTAURANT_MATCHER.assertMatch(mealRepository.get(updated.getId()).getRestaurant(), updated.getRestaurant());

    }

    @Test
    void getAllByRestaurant() {
        List<Meal> all = mealRepository.getAllByRestaurant(RESTAURANT_1_ID);
        MEAL_MATCHER.assertMatch(all, mealsRestaurant1);
    }

    @Test
    void getAllByRestaurantToday() {
        SetDateMeal(mealRepository);
        List<Meal> all = mealRepository.getAllByRestaurantToday(RESTAURANT_1_ID);
        MEAL_MATCHER.assertMatch(all, meal3, meal1, meal5);
    }
}