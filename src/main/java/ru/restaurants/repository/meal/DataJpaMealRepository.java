package ru.restaurants.repository.meal;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.restaurants.model.Meal;
import ru.restaurants.repository.restaurant.CrudRestaurantRepository;

import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaMealRepository implements MealRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudMealRepository crudRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DataJpaMealRepository(CrudMealRepository crudRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudRepository = crudRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Override
    public Meal save(Meal meal,int restaurant_id) {
        if (!meal.isNew() && get(meal.getId()) == null) {
            return null;
        }
        meal.setRestaurant(crudRestaurantRepository.getById(restaurant_id));
        return crudRepository.save(meal);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(crudRepository.delete(id) != 0,id);
    }

    @Override
    public Meal get(int id) {
        return checkNotFoundWithId(crudRepository.findById(id).orElse(null), id);
    }

    @Override
    public List<Meal> getAll(int restaurantId) {
        return crudRepository.getAll(restaurantId);
    }

    @Override
    public Meal update(Meal meal,int restaurantId) {
        Assert.notNull(meal, "Meal must not be null");
        return checkNotFoundWithId(save(meal,restaurantId), meal.getId());
    }
}
