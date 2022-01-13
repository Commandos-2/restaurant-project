package ru.restaurants.repository.user;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.restaurants.AuthorizedUser;
import ru.restaurants.model.User;

import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFound;
import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository("userRepository")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class DataJpaUserRepository implements UserRepository, UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudUserRepository;

    public DataJpaUserRepository(CrudUserRepository crudUserRepository) {
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        return crudUserRepository.save(user);
    }

    @Override
    public void delete(int id) {
        checkNotFoundWithId(crudUserRepository.delete(id) != 0, id);
    }

    @Override
    public User get(int id) {
        return checkNotFoundWithId(crudUserRepository.findById(id).orElse(null), id);
    }

    @Override
    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(crudUserRepository.getByEmail(email).orElse(null), "email=" + email);
    }

    @Override
    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }

    @Override
    public User update(User user) {
        Assert.notNull(user, "user must not be null");
        return checkNotFoundWithId(save(user), user.getId());
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }
}
