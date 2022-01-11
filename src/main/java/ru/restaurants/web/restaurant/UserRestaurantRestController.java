package ru.restaurants.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurants.model.Restaurant;
import ru.restaurants.repository.restaurant.RestaurantRepository;

import java.net.URI;
import java.util.List;

import static ru.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = UserRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantRepository repository;

    static final String REST_URL = "/rest/restaurant";

    public UserRestaurantRestController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/{id}/with-meals")
    public Restaurant getWithMeals(@PathVariable int id) {
        log.info("getWithMeals {}", id);
        return repository.getWithMeals(id);
    }

    @GetMapping("/{id}/all-with-meals")
    public List<Restaurant> getAllWithMeals() {
        log.info("getAllWithMeals ");
        return repository.getAllWithMeals();
    }
}