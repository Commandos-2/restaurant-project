package ru.restaurants.repository.restaurant;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.restaurants.RestaurantTestData;
import ru.restaurants.model.Dish;
import ru.restaurants.model.Restaurant;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.repository.dish.DishRepository;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.DishTestData.*;
import static ru.restaurants.RestaurantTestData.NOT_FOUND;
import static ru.restaurants.RestaurantTestData.*;
import static ru.restaurants.TestUtil.setDateDish;

class DataJpaRestaurantRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected RestaurantRepository restaurantRepository;

    @Autowired
    protected DishRepository dishRepository;

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
    void getWithDishes() {
        Restaurant restaurant = restaurantRepository.getWithDishes(RESTAURANT_1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant1);
        DISH_MATCHER.assertMatch(restaurant.getDishes(), dishesRestaurant1);
    }

    @Test
    void getWithDishesToday() {
        setDateDish(dishRepository);
        Restaurant restaurant = restaurantRepository.getWithDishesToday(RESTAURANT_1_ID);
        RESTAURANT_MATCHER.assertMatch(restaurant, restaurant1);
        DISH_MATCHER.assertMatch(restaurant.getDishes(), dish3, dish1, dish5);
    }

    @Test
    void getAllWithDishesToday() {
        setDateDish(dishRepository);
        List<Restaurant> all = restaurantRepository.getAllWithDishesToday();
        RESTAURANT_MATCHER.assertMatch(all, restaurant1, restaurant2);
        List<Dish> list = new ArrayList<>();
        for (Restaurant restaurant : all) {
            list.addAll(restaurant.getDishes());
        }
        DISH_MATCHER.assertMatch(list, dish3, dish1, dish5, dish6, dish9, dish8);
    }
}