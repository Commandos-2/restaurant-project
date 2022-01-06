package ru.restaurants.repository.user;


import ru.restaurants.model.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    void delete(int id);

    User get(int id);

    User getByEmail(String email);

    List<User> getAll();

    User update(User user);
}
