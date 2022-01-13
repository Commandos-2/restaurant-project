package ru.restaurants.web.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurants.model.Role;
import ru.restaurants.model.User;
import ru.restaurants.repository.restaurant.RestaurantRepository;
import ru.restaurants.repository.user.UserRepository;

import java.net.URI;

import static ru.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.restaurants.util.ValidationUtil.checkNew;
import static ru.restaurants.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    static final String REST_URL = "/rest/profile";

    private final UserRepository userRepository;
    private final RestaurantRepository restaurantRepository;

    public ProfileRestController(UserRepository repository, RestaurantRepository restaurantRepository) {
        this.userRepository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping
    public User get() {
        return userRepository.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        userRepository.delete(authUserId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@RequestBody User user) {
        log.info("create {}", user);
        checkNew(user);
        user.setRole(Role.USER);
        User created = userRepository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        assureIdConsistent(user, authUserId());
        userRepository.update(user);
    }
}