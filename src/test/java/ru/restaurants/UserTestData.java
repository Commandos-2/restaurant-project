package ru.restaurants;

import ru.restaurants.model.Role;
import ru.restaurants.model.User;

import java.time.LocalDate;
import java.util.Date;

import static ru.restaurants.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static final MatcherFactory.Matcher<User> USER_MATCHER = MatcherFactory.usingIgnoringFieldsComparator(User.class, "registered", "dateLastChoice");

    public static final int USER_ID = START_SEQ + 2;
    public static final int ADMIN_ID = START_SEQ + 3;
    public static final int NOT_FOUND = 10;

    public static final User user = new User(USER_ID, "User", "password", "user@yandex.ru", Role.USER, new Date(), LocalDate.now());
    public static final User admin = new User(ADMIN_ID, "Admin", "admin", "admin@gmail.com", Role.ADMIN, new Date(), LocalDate.of(2022, 01, 8));

    public static User getNew() {
        return new User(null, "New", "newPass", "new@gmail.com", Role.USER, new Date(), null);
    }

    public static User getUpdated() {
        User updated = new User(user);
        updated.setEmail("update@gmail.com");
        updated.setName("UpdatedName");
        updated.setPassword("newPass");
        updated.setRole(Role.ADMIN);
        return updated;
    }
}
