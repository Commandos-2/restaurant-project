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
import ru.restaurants.util.exсeption.NotFoundException;
import ru.restaurants.web.json.JsonUtil;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurants.ChoiceTestData.*;
import static ru.restaurants.RestaurantTestData.*;
import static ru.restaurants.TestUtil.setDateChoice;
import static ru.restaurants.TestUtil.userHttpBasic;
import static ru.restaurants.UserTestData.*;

class UserChoiceRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = UserChoiceRestController.REST_URL + '/';

    @Autowired
    private ChoiceRepository choiceRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void update() throws Exception {
        setDateChoice(choiceRepository, LocalTime.MAX, true);
        User updateUser = userRepository.get(USER_ID);
        updateUser.setDateLastChoice(user.getDateLastChoice());
        perform(MockMvcRequestBuilders.put(REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_2_ID)))
                .andExpect(status().isNoContent());

        Choice created2 = choiceRepository.get(CHOICE_1_ID);
        Choice choice = new Choice(choice1);
        choice.setRestaurant(restaurant2);
        CHOICE_MATCHER.assertMatch(created2, choice1);
        assertEquals(created2.getUser().getId(), user.getId());
        assertEquals(created2.getRestaurant().getId(), RESTAURANT_2_ID);
    }

    @Test
    void updateAfterLimitTime() throws Exception {
        setDateChoice(choiceRepository, LocalTime.MIN, true);
        User updateUser = userRepository.get(USER_ID);
        updateUser.setDateLastChoice(user.getDateLastChoice());
        assertThatThrownBy(() ->
                perform(MockMvcRequestBuilders.put(REST_URL)
                        .with(userHttpBasic(user))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JsonUtil.writeValue(RESTAURANT_2_ID)))
                        .andExpect(status().isInternalServerError()))
                .hasCause(new NotFoundException("It is not possible to update choice. You are trying to update choice after:00:00"));
        assertEquals(choiceRepository.get(CHOICE_1_ID).getRestaurant().getId(), RESTAURANT_1_ID);
    }


    @Test
    void createWithLocation() throws Exception {
        setDateChoice(choiceRepository, LocalTime.MIN, true);
        User updateAdmin = userRepository.get(ADMIN_ID);
        updateAdmin.setDateLastChoice(admin.getDateLastChoice());
        ResultActions action = perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(admin))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_2_ID)))
                .andExpect(status().isCreated());
        Choice created1 = CHOICE_MATCHER.readFromJson(action);
        int newId = created1.getId();
        Choice newChoice = new Choice(choice4);
        newChoice.setId(newId);
        Choice created2 = choiceRepository.get(newId);
        CHOICE_MATCHER.assertMatch(created1, newChoice);
        CHOICE_MATCHER.assertMatch(created2, newChoice);
        assertEquals(created2.getUser().getId(), admin.getId());
        assertEquals(created2.getRestaurant().getId(), RESTAURANT_2_ID);
    }

    @Test
    void createWithLocationFail() throws Exception {
        setDateChoice(choiceRepository, LocalTime.MIN, true);
        userRepository.get(USER_ID).setDateLastChoice(user.getDateLastChoice());
        perform(MockMvcRequestBuilders.post(REST_URL)
                .with(userHttpBasic(user))
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(RESTAURANT_2_ID)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void getAllByUserId() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(CHOICE_MATCHER.contentJson(choice1, choice3));

    }

    @Test
    void getChoiceToDay() throws Exception {
        setDateChoice(choiceRepository, null, false);
        userRepository.get(USER_ID).setDateLastChoice(user.getDateLastChoice());
        ResultActions action = perform(MockMvcRequestBuilders.get(REST_URL + "today")
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
        Choice toDayChoice = CHOICE_MATCHER.readFromJson(action);
        CHOICE_MATCHER.assertMatch(toDayChoice, choice1);
    }

    @Test
    void getChoiceToDayFail() throws Exception {
        setDateChoice(choiceRepository, null, false);
        perform(MockMvcRequestBuilders.get(REST_URL + "today")
                .with(userHttpBasic(admin)))
                .andExpect(status().isNotFound());
    }
}