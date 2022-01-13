package ru.restaurants.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurants.AbstractControllerTest;
import ru.restaurants.MealTestData;
import ru.restaurants.model.Meal;
import ru.restaurants.repository.meal.MealRepository;
import ru.restaurants.repository.restaurant.RestaurantRepository;
import ru.restaurants.util.exсeption.NotFoundException;
import ru.restaurants.web.json.JsonUtil;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurants.MealTestData.*;
import static ru.restaurants.RestaurantTestData.*;
import static ru.restaurants.TestUtil.userHttpBasic;
import static ru.restaurants.UserTestData.admin;

class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealRepository repository;

    @Autowired
    protected RestaurantRepository repositoryRestaurant;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + MEAL_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + MEAL_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> repository.get(MEAL_ID));
    }

    @Test
    void update() throws Exception {
        Meal updated = MealTestData.getUpdated();
        perform(MockMvcRequestBuilders.put(REST_URL + RESTAURANT_2_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated)))
                .andExpect(status().isNoContent());

        MEAL_MATCHER.assertMatch(repository.get(MEAL_ID), updated);
        RESTAURANT_MATCHER.assertMatch(repository.get(MEAL_ID).getRestaurant(), updated.getRestaurant());
    }

    @Test
    void createWithLocation() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL + RESTAURANT_2_ID)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = MEAL_MATCHER.readFromJson(action);
        int newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(repository.get(newId), newMeal);
        RESTAURANT_MATCHER.assertMatch(repository.get(newId).getRestaurant(), newMeal.getRestaurant());
    }

    @Test
    void getAllByRestaurant() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "allByRestaurant/" + RESTAURANT_1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(mealsRestaurant1));
    }
}