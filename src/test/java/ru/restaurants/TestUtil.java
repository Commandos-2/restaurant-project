package ru.restaurants;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.restaurants.model.Choice;
import ru.restaurants.model.Meal;
import ru.restaurants.model.User;
import ru.restaurants.repository.choice.ChoiceRepository;
import ru.restaurants.repository.meal.MealRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.restaurants.ChoiceTestData.CHOICE_2_ID;
import static ru.restaurants.ChoiceTestData.CHOICE_3_ID;
import static ru.restaurants.MealTestData.MEAL_ID;
import static ru.restaurants.RestaurantTestData.restaurant1;
import static ru.restaurants.web.choice.UserChoiceRestController.changeLimitTime;

public class TestUtil {

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public static RequestPostProcessor userAuth(User user) {
        return SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
    }

    public static void SetDateChoice(ChoiceRepository repository, LocalTime localTime, boolean switchBollean) {
        Choice choice3 = repository.get(CHOICE_3_ID);
        Choice choice2 = repository.get(CHOICE_2_ID);
        choice3.setRegistered(LocalDateTime.of(2022, 01, 8, 10, 20));
        choice2.setRegistered(LocalDateTime.of(2022, 01, 8, 10, 20));
        repository.save(choice3);
        repository.save(choice2);
        if (switchBollean) {
            changeLimitTime(localTime);
        }
    }

    public static void SetDateMeal(MealRepository repository) {
        Meal meal2 = repository.get(MEAL_ID + 1);
        Meal meal4 = repository.get(MEAL_ID + 3);
        meal2.setRegistered(LocalDateTime.of(2022, 01, 8, 10, 20));
        meal4.setRegistered(LocalDateTime.of(2022, 01, 8, 10, 20));
        repository.save(meal2, restaurant1.getId());
        repository.save(meal4, restaurant1.getId());
    }
}
