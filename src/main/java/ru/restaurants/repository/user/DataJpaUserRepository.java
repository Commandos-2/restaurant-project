package ru.restaurants.repository.user;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.restaurants.model.User;

import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFound;
import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class DataJpaUserRepository implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudRepository;

    public DataJpaUserRepository(CrudUserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Override
    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        return crudRepository.save(user);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(crudRepository.delete(id) != 0,id);
    }

    @Override
    public User get(int id) {
        return checkNotFoundWithId(crudRepository.findById(id).orElse(null), id);
    }

    @Override
    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(crudRepository.getByEmail(email), "email=" + email);
    }

    @Override
    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    public User update(User user) {
        Assert.notNull(user, "user must not be null");
        return checkNotFoundWithId(save(user), user.getId());
    }
}
