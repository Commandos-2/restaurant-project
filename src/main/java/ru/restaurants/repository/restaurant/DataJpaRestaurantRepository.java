package ru.restaurants.repository.restaurant;

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
public class DataJpaRestaurantRepository implements RestaurantRepository {
    private static final Sort SORT_NAME = Sort.by(Sort.Direction.ASC, "name");

    private final CrudRestaurantRepository crudRestaurantRepository;
    private final CrudMealRepository crudMealRepository;

    public DataJpaRestaurantRepository(CrudRestaurantRepository crudRestaurantRepository, CrudMealRepository crudMealRepository) {
        this.crudRestaurantRepository = crudRestaurantRepository;
        this.crudMealRepository = crudMealRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        if (!restaurant.isNew() && get(restaurant.getId()) == null) {
            return null;
        }
        return crudRestaurantRepository.save(restaurant);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(crudRestaurantRepository.delete(id) != 0, id);
    }

    @Override
    public Restaurant get(int id) {
        return checkNotFoundWithId(crudRestaurantRepository.findById(id).orElse(null), id);
    }

    @Override
    public List<Restaurant> getAll() {
        return crudRestaurantRepository.findAll(SORT_NAME);
    }

    @Override
    public Restaurant update(Restaurant restaurant) {
        Assert.notNull(restaurant, "restaurant must not be null");
        return checkNotFoundWithId(save(restaurant), restaurant.getId());
    }

    @Override
    public Restaurant getWithMeals(int id) {
        return crudRestaurantRepository.getWithMeals(id).orElse(null);
    }

    @Override
    public Restaurant getWithMealsToday(int id) {
        Restaurant restaurant = get(id);
        return getWithMealsTodayUtil(restaurant);
    }

    public Restaurant getWithMealsToday(Restaurant restaurant) {
        return getWithMealsTodayUtil(restaurant);
    }

    private Restaurant getWithMealsTodayUtil(Restaurant restaurant){
        List<Meal> list = crudMealRepository.getAllByRestaurant(restaurant.getId());
        restaurant.setMeals(list.stream().filter(m -> m.getRegistered().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN))).collect(Collectors.toList()));
        return restaurant;
    }
    @Override
    public List<Restaurant> getAllWithMeals() {
        return crudRestaurantRepository.getAllWithMeals();
    }

    @Override
    public List<Restaurant> getAllWithMealsToday() {
        List<Restaurant> list = getAll();
        return list.stream().map(r -> getWithMealsToday(r)).collect(Collectors.toList());
    }
}
