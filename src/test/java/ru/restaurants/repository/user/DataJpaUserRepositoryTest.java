package ru.restaurants.repository.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.restaurants.model.Role;
import ru.restaurants.model.User;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.util.exсeption.NotFoundException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.UserTestData.*;

class DataJpaUserRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected UserRepository userRepository;

    @Test
    public void create() {
        User created = userRepository.save(getNew());
        int newId = created.getId();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(userRepository.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                userRepository.save(new User(null, "Duplicate", "newPass", "user@yandex.ru", Role.USER, LocalDate.now())));
    }

    @Test
    void delete() {
        userRepository.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> userRepository.get(USER_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> userRepository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        User user = userRepository.get(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> userRepository.get(NOT_FOUND));
    }

    @Test
    void getByEmail() {
        User user = userRepository.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void update() {
        User updated = getUpdated();
        userRepository.update(updated);
        USER_MATCHER.assertMatch(userRepository.get(USER_ID), getUpdated());
    }

    @Test
    void getAll() {
        List<User> all = userRepository.getAll();
        USER_MATCHER.assertMatch(all, admin, user);
    }
}