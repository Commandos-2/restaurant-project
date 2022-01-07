package ru.restaurants;

import ru.restaurants.model.Role;
import ru.restaurants.model.Meal;

import java.util.List;

import static ru.restaurants.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final MatcherFactory.Matcher<Meal> MEAL_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Meal.class, "restaurant");

    public static final int MEAL_ID = START_SEQ + 4;
    public static final int ADMIN_ID = START_SEQ + 9;
    public static final int NOT_FOUND = 10;

    public static final Meal meal1 = new Meal(MEAL_ID, "Omelette", 500);
    public static final Meal meal2 = new Meal(MEAL_ID + 1, "Cutlet", 1000);
    public static final Meal meal3 = new Meal(MEAL_ID + 2, "Balyk", 300);
    public static final Meal meal4 = new Meal(MEAL_ID + 3, "Paste", 100);
    public static final Meal meal5 = new Meal(MEAL_ID + 4, "Pizza", 430);
    public static final Meal meal6 = new Meal(ADMIN_ID, "Bun", 1000);
    public static final Meal meal7 = new Meal(ADMIN_ID + 1, "Pudding", 510);
    public static final Meal meal8 = new Meal(ADMIN_ID + 2, "Samosa", 300);
    public static final Meal meal9 = new Meal(ADMIN_ID + 3, "Salad", 450);

    public static final List<Meal> meals = List.of(meal3, meal2, meal1, meal4, meal5);

    public static Meal getNew() {
        return new Meal(null, "New", 555);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal1);
        updated.setName("UpdatedName");
        updated.setPrice(359);
        return updated;
    }
}
