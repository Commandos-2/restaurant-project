package ru.restaurants;

import ru.restaurants.model.Choice;

import java.time.LocalDate;
import java.util.List;

import static ru.restaurants.RestaurantTestData.restaurant1;
import static ru.restaurants.RestaurantTestData.restaurant2;
import static ru.restaurants.UserTestData.admin;
import static ru.restaurants.UserTestData.user;
import static ru.restaurants.model.AbstractBaseEntity.START_SEQ;

public class ChoiceTestData {
    public static final MatcherFactory.Matcher<Choice> CHOICE_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(Choice.class, "date", "user", "restaurant");

    public static final int CHOICE_1_ID = START_SEQ + 13;
    public static final int CHOICE_2_ID = START_SEQ + 14;
    public static final int CHOICE_3_ID = START_SEQ + 15;
    public static final int CHOICE_4_ID = START_SEQ + 16;
    public static final int NOT_FOUND = 10;

    public static final Choice choice1 = new Choice(CHOICE_1_ID, restaurant1, user, LocalDate.now());
    public static final Choice choice2 = new Choice(CHOICE_2_ID, restaurant1, admin, LocalDate.of(2022, 01, 8));
    public static final Choice choice3 = new Choice(CHOICE_3_ID, restaurant2, user, LocalDate.of(2022, 01, 8));
    public static final List<Choice> choices = List.of(choice1, choice2, choice3);
    public static final Choice choice4 = new Choice(null, restaurant2, admin, LocalDate.now());
}
