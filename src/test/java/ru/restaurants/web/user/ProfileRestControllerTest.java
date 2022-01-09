package ru.restaurants.web.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.restaurants.AbstractControllerTest;
import ru.restaurants.model.Role;
import ru.restaurants.model.User;
import ru.restaurants.repository.user.UserRepository;
import ru.restaurants.web.json.JsonUtil;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.restaurants.TestUtil.userHttpBasic;
import static ru.restaurants.UserTestData.*;
import static ru.restaurants.web.user.ProfileRestController.REST_URL;


class ProfileRestControllerTest extends AbstractControllerTest {
    @Autowired
    private UserRepository repository;

    @Test
    void get() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(USER_MATCHER.contentJson(user));
    }

    @Test
    void getUnAuth() throws Exception {
        perform(MockMvcRequestBuilders.get(REST_URL))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void delete() throws Exception {
        perform(MockMvcRequestBuilders.delete(REST_URL)
                .with(userHttpBasic(user)))
                .andExpect(status().isNoContent());
        USER_MATCHER.assertMatch(repository.getAll(), admin);
    }

    @Test
    void update() throws Exception {
        User updated = new User(null, "newName", "newPassword", "user@yandex.ru", Role.USER);
        perform(MockMvcRequestBuilders.put(REST_URL).contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(user))
                .content(JsonUtil.writeValue(updated)))
                .andDo(print())
                .andExpect(status().isNoContent());
        User newUser=repository.get(USER_ID);
        updated.setId(newUser.getId());
        USER_MATCHER.assertMatch(newUser, updated);
    }
}