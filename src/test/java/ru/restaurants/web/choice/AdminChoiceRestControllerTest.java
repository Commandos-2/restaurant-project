package ru.restaurants.web.choice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurants.AbstractControllerTest;
import ru.restaurants.repository.choice.ChoiceRepository;
import ru.restaurants.util.exсeption.NotFoundException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurants.ChoiceTestData.*;
import static ru.restaurants.TestUtil.userHttpBasic;
import static ru.restaurants.UserTestData.admin;

class AdminChoiceRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = AdminChoiceRestController.REST_URL + '/';

    @Autowired
    private ChoiceRepository choiceRepository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL + CHOICE_1_ID)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(CHOICE_MATCHER.contentJson(choice1));
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL + CHOICE_1_ID)
                .with(userHttpBasic(admin)))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertThrows(NotFoundException.class, () -> choiceRepository.get(CHOICE_1_ID));
    }

    @Test
    void getAll() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(admin)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(CHOICE_MATCHER.contentJson(choices));
    }
}