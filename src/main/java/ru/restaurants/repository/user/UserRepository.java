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
import ru.restaurants.util.exсeption.NotFoundException;

import java.util.List;

import static ru.restaurants.util.ValidationUtil.checkNotFoundWithId;

@Repository("userRepository")
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserRepository implements UserDetailsService {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    private final CrudUserRepository crudUserRepository;

    public UserRepository(CrudUserRepository crudUserRepository) {
        this.crudUserRepository = crudUserRepository;
    }

    public User save(User user) {
        Assert.notNull(user, "user must not be null");
        return crudUserRepository.save(user);
    }

    public void delete(int id) {
        checkNotFoundWithId(crudUserRepository.delete(id) != 0, id);
    }

    public User get(int id) {
        return crudUserRepository.findById(id).orElseThrow(new NotFoundException("Not found entity with ".concat(String.valueOf(id))));
    }

    public User getByEmail(String email) {
        Assert.notNull(email, "email must not be null");
        return crudUserRepository.getByEmail(email).orElseThrow(new NotFoundException("Not found entity with".concat(email)));
    }

    public List<User> getAll() {
        return crudUserRepository.findAll(SORT_NAME_EMAIL);
    }

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
