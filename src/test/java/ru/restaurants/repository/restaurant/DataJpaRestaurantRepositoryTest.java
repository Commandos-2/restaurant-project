package ru.restaurants.repository.restaurant;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.restaurants.model.Restaurant;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.RestaurantTestData.*;

class DataJpaRestaurantRepositoryTest extends AbstractRepositoryTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    protected RestaurantRepository repository;

    @Test
    public void create() {
        Restaurant created = repository.save(getNew());
        int newId = created.getId();
        Restaurant newRestaurant = getNew();
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
        Restaurant updated = getUpdated();
        repository.update(updated);
        RESTAURANT_MATCHER.assertMatch(repository.get(RESTAURANT_1_ID), getUpdated());
    }

    @Test
    void getAll() {
        List<Restaurant> all = repository.getAll();
        RESTAURANT_MATCHER.assertMatch(all, restaurant1, restaurant2);
    }
}