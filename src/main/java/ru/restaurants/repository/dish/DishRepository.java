package ru.restaurants.repository.dish;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.restaurants.model.Dish;
import ru.restaurants.repository.restaurant.RestaurantRepository;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DishRepository {

    private final CrudDishRepository crudDishRepository;
    private final RestaurantRepository restaurantRepository;

    public DishRepository(CrudDishRepository crudDishRepository, RestaurantRepository restaurantRepository) {
        this.crudDishRepository = crudDishRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    @CacheEvict(value = {"getWithDishesToday", "getAllWithDishesToday"}, allEntries = true)
    public Dish save(Dish dish, int restaurantId) {
        Assert.notNull(dish, "dish must not be null");
        if (!dish.isNew() && get(dish.getId()) == null) {
            return null;
        }
        dish.setRestaurant(restaurantRepository.get(restaurantId));
        return crudDishRepository.save(dish);
    }

    @CacheEvict(value = {"getWithDishesToday", "getAllWithDishesToday"}, allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudDishRepository.delete(id) != 0, id);
    }

    public Dish get(int id) {
        return crudDishRepository.findById(id).orElseThrow(new NotFoundException("Not found entity with ".concat(String.valueOf(id))));
    }

    @Transactional
    public List<Dish> getAllByRestaurantToday(int restaurantId) {
        return crudDishRepository.getAllByRestaurant(restaurantId);
    }

    @Transactional
    @CacheEvict(value = {"getWithDishesToday", "getAllWithDishesToday"}, allEntries = true)
    public Dish update(Dish dish, int restaurantId) {
        Assert.notNull(dish, "Dish must not be null");
        Dish dishInDB = get(dish.getId());
        if (dishInDB != null) {
            if (dishInDB.getRestaurant().getId().equals(restaurantId)) {
                return save(dish, restaurantId);
            }
        }
        throw new NotFoundException("Unable to update dish");
    }

    public Dish getWithRestaurant(int id) {
        return crudDishRepository.getWithRestaurant(id).orElseThrow(new NotFoundException("Not found entity with ".concat(String.valueOf(id))));
    }
}
