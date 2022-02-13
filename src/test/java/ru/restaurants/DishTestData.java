package ru.restaurants;

import ru.restaurants.model.Dish;

import java.util.List;

import static ru.restaurants.RestaurantTestData.restaurant1;
import static ru.restaurants.RestaurantTestData.restaurant2;
import static ru.restaurants.model.AbstractBaseEntity.START_SEQ;

public class DishTestData {
    public static final MatcherFactory.Matcher<Dish> DISH_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Dish.class, "restaurant", "date");
    public static final int DISH_ID = START_SEQ + 4;
    public static final int ADMIN_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 10;

    public static final Dish dish1 = new Dish(DISH_ID, "Omelette", 500, restaurant1);
    public static final Dish dish2 = new Dish(DISH_ID + 1, "Cutlet", 1000, restaurant1);
    public static final Dish dish3 = new Dish(DISH_ID + 2, "Balyk", 300, restaurant1);
    public static final Dish dish4 = new Dish(DISH_ID + 3, "Paste", 100, restaurant1);
    public static final Dish dish5 = new Dish(DISH_ID + 4, "Pizza", 430, restaurant1);
    public static final List<Dish> dishesRestaurant1 = List.of(dish3, dish2, dish1, dish4, dish5);
    public static final Dish dish6 = new Dish(ADMIN_ID, "Bun", 1000, restaurant2);
    public static final Dish dish7 = new Dish(ADMIN_ID + 1, "Pudding", 510, restaurant2);
    public static final Dish dish8 = new Dish(ADMIN_ID + 2, "Samosa", 300, restaurant2);
    public static final Dish dish9 = new Dish(ADMIN_ID + 3, "Salad", 450, restaurant2);
    public static final List<Dish> dishes = List.of(dish3, dish2, dish1, dish4, dish5, dish6, dish7, dish9, dish8);

    public static Dish getNew() {
        return new Dish(null, "New", 555, restaurant2);
    }

    public static Dish getUpdated() {
        Dish updated = new Dish(dish1);
        updated.setName("UpdatedName");
        updated.setPrice(359);
        updated.setRestaurant(restaurant1);
        return updated;
    }
}
