package ru.restaurants.repository.meal;

import ru.restaurants.model.Meal;

import java.util.List;

public interface MealRepository {

    Meal save(Meal meal,int restaurantId);

    void delete(int id);

    Meal get(int id);

    List<Meal> getAll(int restaurantId);

    Meal update(Meal meal,int restaurantId);
}
