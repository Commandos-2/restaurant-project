package ru.restaurants;

import ru.restaurants.model.User;

import java.io.Serial;
import java.util.Set;

public class AuthorizedUser extends org.springframework.security.core.userdetails.User {
    @Serial
    private static final long serialVersionUID = 1L;

    private User user;

    public AuthorizedUser(User user) {
        super(user.getEmail(), user.getPassword(), true, true, true, true, Set.of(user.getRole()));
        this.user = user;
    }

    public int getId() {
        return user.getId();
    }

    public void update(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return user.toString();
    }
}