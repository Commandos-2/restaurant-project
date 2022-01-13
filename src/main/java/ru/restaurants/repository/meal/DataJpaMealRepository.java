package ru.restaurants.repository.meal;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ru.restaurants.model.Meal;
import ru.restaurants.repository.restaurant.CrudRestaurantRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaMealRepository implements MealRepository {

    private final CrudMealRepository crudMealRepository;
    private final CrudRestaurantRepository crudRestaurantRepository;

    public DataJpaMealRepository(CrudMealRepository crudMealRepository, CrudRestaurantRepository crudRestaurantRepository) {
        this.crudMealRepository = crudMealRepository;
        this.crudRestaurantRepository = crudRestaurantRepository;
    }

    @Override
    @Transactional
    public Meal save(Meal meal, int restaurant_id) {
        Assert.notNull(meal, "meal must not be null");
        if (!meal.isNew() && get(meal.getId()) == null) {
            return null;
        }
        meal.setRestaurant(crudRestaurantRepository.getById(restaurant_id));
        return crudMealRepository.save(meal);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(crudMealRepository.delete(id) != 0, id);
    }

    @Override
    public Meal get(int id) {
        return checkNotFoundWithId(crudMealRepository.findById(id).orElse(null), id);
    }

    @Override
    public List<Meal> getAllByRestaurant(int restaurantId) {
        return crudMealRepository.getAllByRestaurant(restaurantId);
    }

    @Override
    @Transactional
    public List<Meal> getAllByRestaurantToday(int restaurantId) {
        List<Meal> list = crudMealRepository.getAllByRestaurant(restaurantId);
        return list.stream().filter(m -> m.getRegistered().isAfter(LocalDateTime.of(LocalDate.now(), LocalTime.MIN))).collect(Collectors.toList());
    }

    @Override
    public Meal update(Meal meal, int restaurantId) {
        Assert.notNull(meal, "Meal must not be null");
        return checkNotFoundWithId(save(meal, restaurantId), meal.getId());
    }

    @Override
    public Meal getWithRestaurant(int id) {
        return checkNotFoundWithId(crudMealRepository.getWithRestaurant(id).orElse(null), id);
    }
}
