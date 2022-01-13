package ru.restaurants.repository.restaurant;


import ru.restaurants.model.Restaurant;

import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    void delete(int id);

    Restaurant get(int id);

    List<Restaurant> getAll();

    Restaurant update(Restaurant restaurant);

    default Restaurant getWithMeals(int id) {
        throw new UnsupportedOperationException();
    }

    default List<Restaurant> getAllWithMeals() {
        throw new UnsupportedOperationException();
    }

    Restaurant getWithMealsToday(int id);

    List<Restaurant> getAllWithMealsToday();
}
