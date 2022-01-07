package ru.restaurants.repository.restaurant;


import ru.restaurants.model.Restaurant;
import ru.restaurants.model.User;

import java.util.List;

public interface RestaurantRepository {

    Restaurant save(Restaurant restaurant);

    void delete(int id);

    Restaurant get(int id);

    List<Restaurant> getAll();

    Restaurant update(Restaurant user);
}
