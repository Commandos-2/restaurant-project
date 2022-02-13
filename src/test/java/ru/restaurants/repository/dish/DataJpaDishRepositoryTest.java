package ru.restaurants.repository.dish;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import ru.restaurants.DishTestData;
import ru.restaurants.model.Dish;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.DishTestData.NOT_FOUND;
import static ru.restaurants.DishTestData.*;
import static ru.restaurants.RestaurantTestData.*;
import static ru.restaurants.TestUtil.setDateDish;

class DataJpaDishRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected DishRepository dishRepository;

    @Test
    public void create() {
        Dish created = dishRepository.save(DishTestData.getNew(), RESTAURANT_2_ID);
        int newId = created.getId();
        Dish newDish = DishTestData.getNew();
        newDish.setId(newId);
        DISH_MATCHER.assertMatch(created, newDish);
        DISH_MATCHER.assertMatch(dishRepository.get(newId), newDish);
        assertEquals(dishRepository.get(newId).getRestaurant().getId(), newDish.getRestaurant().getId());
    }

    @Test
    void duplicateNameCreate() {
        assertThrows(DataAccessException.class, () ->
                dishRepository.save(new Dish(null, dish1.getName(), 222, restaurant1), RESTAURANT_1_ID));
    }

    @Test
    void delete() {
        dishRepository.delete(DISH_ID);
        assertThrows(NotFoundException.class, () -> dishRepository.get(DISH_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> dishRepository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Dish Dish = dishRepository.get(ADMIN_ID);
        DISH_MATCHER.assertMatch(Dish, dish6);
    }

    @Test
    void getWithRestaurant() {
        Dish dish = dishRepository.getWithRestaurant(ADMIN_ID);
        DISH_MATCHER.assertMatch(dish, dish6);
        RESTAURANT_MATCHER.assertMatch(dish.getRestaurant(), dish6.getRestaurant());
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> dishRepository.get(NOT_FOUND));
    }

    @Test
    @Transactional
    void update() {
        Dish updated = DishTestData.getUpdated();
        dishRepository.update(updated, RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(dishRepository.get(DISH_ID), DishTestData.getUpdated());
        RESTAURANT_MATCHER.assertMatch(dishRepository.get(updated.getId()).getRestaurant(), updated.getRestaurant());
    }

    @Test
    void getAllByRestaurantToday() {
        setDateDish(dishRepository);
        List<Dish> all = dishRepository.getAllByRestaurantToday(RESTAURANT_1_ID);
        DISH_MATCHER.assertMatch(all, dish3, dish1, dish5);
    }
}