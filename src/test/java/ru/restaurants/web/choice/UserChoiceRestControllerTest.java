package ru.restaurants.web.choice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurants.AbstractControllerTest;
import ru.restaurants.model.Choice;
import ru.restaurants.model.User;
import ru.restaurants.repository.choice.ChoiceRepository;
import ru.restaurants.repository.user.UserRepository;
import ru.restaurants.web.json.JsonUtil;

import java.time.LocalTime;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurants.ChoiceTestData.*;
import static ru.restaurants.RestaurantTestData.*;
import static ru.restaurants.TestUtil.SetDateChoice;
import static ru.restaurants.TestUtil.userHttpBasic;
import static ru.restaurants.UserTestData.*;

class UserChoiceRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = UserChoiceRestController.REST_URL + '/';

    @Autowired
    private ChoiceRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void update() throws Exception {
        SetDateChoice(repository, LocalTime.MAX,true);
        User updateUser=userRepository.get(USER_ID);
        updateUser.setDateLastChoice(user.getDateLastChoice());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_1_ID)))
                .andExpect(status().isOk());

        Choice created1 = CHOICE_MATCHER.readFromJson(action);
        int newId = created1.getId();
        Choice newChoice= new Choice(choice1);
        newChoice.setRestaurant(restaurant1);
        Choice created2 = repository.get(newId);
        CHOICE_MATCHER.assertMatch(created1, newChoice);
        CHOICE_MATCHER.assertMatch(created2, newChoice);
        USER_MATCHER.assertMatch(created2.getUser(), user);
        RESTAURANT_MATCHER.assertMatch(created2.getRestaurant(), restaurant1);
    }

    @Test
    void updateAfterLimitTime() throws Exception {
        SetDateChoice(repository, LocalTime.MIN,true);
        User updateUser=userRepository.get(USER_ID);
        updateUser.setDateLastChoice(user.getDateLastChoice());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_1_ID)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithLocation() throws Exception {
        SetDateChoice(repository, LocalTime.MIN,true);
        User updateadmin=userRepository.get(ADMIN_ID);
        updateadmin.setDateLastChoice(admin.getDateLastChoice());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_2_ID)))
                .andExpect(status().isCreated());
        Choice created1 = CHOICE_MATCHER.readFromJson(action);
        int newId = created1.getId();
        Choice newChoice = new Choice(choice4);
        newChoice.setId(newId);
        Choice created2 = repository.get(newId);
        CHOICE_MATCHER.assertMatch(created1, newChoice);
        CHOICE_MATCHER.assertMatch(created2, newChoice);
        USER_MATCHER.assertMatch(created2.getUser(), admin);
        RESTAURANT_MATCHER.assertMatch(created2.getRestaurant(), restaurant2);
    }

    @Test
    void getAllByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + "by-user")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(CHOICE_MATCHER.contentJson(choice1, choice3));
    }
}