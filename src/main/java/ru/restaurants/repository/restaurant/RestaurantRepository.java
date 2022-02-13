package ru.restaurants.repository.restaurant;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.restaurants.model.Restaurant;
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository crudRestaurantRepository;

    public RestaurantRepository(CrudRestaurantRepository crudRestaurantRepository) {
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @CacheEvict(value = {"getWithDishesToday", "getAllWithDishesToday"}, allEntries = true)
    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        if (!restaurant.isNew() && get(restaurant.getId()) == null) {
            return null;
        }
        return crudRestaurantRepository.save(restaurant);
    }

    @CacheEvict(value = {"getWithDishesToday", "getAllWithDishesToday"}, allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudRestaurantRepository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return crudRestaurantRepository.findById(id).orElseThrow(new NotFoundException("Not found entity with ".concat(String.valueOf(id))));
    }

    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll(SORT_NAME);
    }

    @CacheEvict(value = {"getWithDishesToday", "getAllWithDishesToday"}, allEntries = true)
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return checkNotFoundWithId(save(restaurant), restaurant.getId());
    }

    public Restaurant getWithDishes(int id) {
        return crudRestaurantRepository.getWithDishes(id).orElseThrow(new NotFoundException("Not found entity with ".concat(String.valueOf(id))));
    }

    @Cacheable("getWithDishesToday")
    public Restaurant getWithDishesToday(int id) {
        return crudRestaurantRepository.getWithDishesToday(id).orElseThrow(new NotFoundException("Not found entity with ".concat(String.valueOf(id))));
    }

    @Cacheable("getAllWithDishesToday")
    public List<Restaurant> getAllWithDishesToday() {
        return crudRestaurantRepository.getAllWithDishesToday();
    }
}