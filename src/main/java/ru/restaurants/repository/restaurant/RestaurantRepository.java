package ru.restaurants.repository.restaurant;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.restaurants.model.Meal;
import ru.restaurants.model.Restaurant;
import ru.restaurants.repository.meal.CrudMealRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository crudRestaurantRepository;
    private final CrudMealRepository crudMealRepository;

    public RestaurantRepository(CrudRestaurantRepository crudRestaurantRepository, CrudMealRepository crudMealRepository) {
        this.crudRestaurantRepository = crudRestaurantRepository;
        this.crudMealRepository = crudMealRepository;
    }

    @CacheEvict(value = {"getWithMealsToday","getAllWithMealsToday"}, allEntries = true)
    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        if (!restaurant.isNew() && get(restaurant.getId()) == null) {
            return null;
        }
        return crudRestaurantRepository.save(restaurant);
    }

    @CacheEvict(value = {"getWithMealsToday","getAllWithMealsToday"}, allEntries = true)
    public void delete(int id) {
        checkNotFoundWithId(crudRestaurantRepository.delete(id) != 0, id);
    }

    public Restaurant get(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.findById(id).orElse(null), id);
    }

    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll(SORT_NAME);
    }

    @CacheEvict(value = {"getWithMealsToday","getAllWithMealsToday"}, allEntries = true)
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return checkNotFoundWithId(save(restaurant), restaurant.getId());
    }

    public Restaurant getWithMeals(int id) {
        return crudRestaurantRepository.getWithMeals(id).orElse(null);
    }

    @Cacheable("getWithMealsToday")
    public Restaurant getWithMealsToday(int id) {
        Restaurant restaurant = get(id);
        List<Meal> list = crudMealRepository.getAllByRestaurant(restaurant.getId());
        restaurant.setMeals(list.stream().filter(m -> m.getRegistered().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN))).collect(Collectors.toList()));
        return restaurant;
    }

    public List<Restaurant> getAllWithMeals() {
        return crudRestaurantRepository.getAllWithMeals();
    }

    @Cacheable("getAllWithMealsToday")
    public List<Restaurant> getAllWithMealsToday() {
        List<Restaurant> list = getAll();
        return list.stream().map(r -> getWithMealsToday(r.getId())).collect(Collectors.toList());
    }
}
