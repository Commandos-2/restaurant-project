package ru.restaurants.repository.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import ru.restaurants.model.Role;
import ru.restaurants.model.User;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.util.exсeption.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.UserTestData.*;

class DataJpaUserRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected UserRepository repository;

    @Test
    public void create() {
        User created = repository.save(getNew());
        int newId = created.getId();
        User newUser = getNew();
        newUser.setId(newId);
        USER_MATCHER.assertMatch(created, newUser);
        USER_MATCHER.assertMatch(repository.get(newId), newUser);
    }

    @Test
    void duplicateMailCreate() {
        assertThrows(DataAccessException.class, () ->
                repository.save(new User(null, "Duplicate", "newPass", "user@yandex.ru", Role.USER, LocalDateTime.now())));
    }

    @Test
    void delete() {
        repository.delete(USER_ID);
        assertThrows(NotFoundException.class, () -> repository.get(USER_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> repository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        User user = repository.get(ADMIN_ID);
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> repository.get(NOT_FOUND));
    }

    @Test
    void getByEmail() {
        User user = repository.getByEmail("admin@gmail.com");
        USER_MATCHER.assertMatch(user, admin);
    }

    @Test
    void update() {
        User updated = getUpdated();
        repository.update(updated);
        USER_MATCHER.assertMatch(repository.get(USER_ID), getUpdated());
    }

    @Test
    void getAll() {
        List<User> all = repository.getAll();
        USER_MATCHER.assertMatch(all, admin, user);
    }
}