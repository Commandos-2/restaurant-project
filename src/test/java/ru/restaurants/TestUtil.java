package ru.restaurants;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.restaurants.model.Choice;
import ru.restaurants.model.Dish;
import ru.restaurants.model.User;
import ru.restaurants.repository.choice.ChoiceRepository;
import ru.restaurants.repository.dish.DishRepository;

import java.time.LocalDate;
import java.time.LocalTime;

import static ru.restaurants.ChoiceTestData.CHOICE_2_ID;
import static ru.restaurants.ChoiceTestData.CHOICE_3_ID;
import static ru.restaurants.DishTestData.ADMIN_ID;
import static ru.restaurants.DishTestData.DISH_ID;
import static ru.restaurants.RestaurantTestData.restaurant1;
import static ru.restaurants.RestaurantTestData.restaurant2;
import static ru.restaurants.web.choice.UserChoiceRestController.changeLimitTime;

public class TestUtil {

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword());
    }

    public static RequestPostProcessor userAuth(User user) {
        return SecurityMockMvcRequestPostProcessors.authentication(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
    }

    public static void setDateChoice(ChoiceRepository repository, LocalTime localTime, boolean switchBollean) {
        Choice choice3 = repository.get(CHOICE_3_ID);
        Choice choice2 = repository.get(CHOICE_2_ID);
        choice3.setDate(LocalDate.of(2022, 01, 8));
        choice2.setDate(LocalDate.of(2022, 01, 8));
        repository.save(choice3);
        repository.save(choice2);
        if (switchBollean) {
            changeLimitTime(localTime);
        }
    }

    public static void setDateDish(DishRepository repository) {
        Dish dish2 = repository.get(DISH_ID + 1);
        Dish dish4 = repository.get(DISH_ID + 3);
        Dish dish7 = repository.get(ADMIN_ID + 1);
        dish2.setDate(LocalDate.of(2022, 01, 8));
        dish4.setDate(LocalDate.of(2022, 01, 8));
        dish7.setDate(LocalDate.of(2022, 01, 8));
        repository.save(dish2, restaurant1.getId());
        repository.save(dish4, restaurant1.getId());
        repository.save(dish7, restaurant2.getId());
    }
}
