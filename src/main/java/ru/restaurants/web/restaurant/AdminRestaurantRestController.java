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
@RequestMapping(value = AdminRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminRestaurantRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final RestaurantRepository repository;

    static final String REST_URL = "/rest/admin/restaurant";

    public AdminRestaurantRestController(RestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Restaurant> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @GetMapping("/{id}")
    public Restaurant get(@PathVariable int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Restaurant> createWithLocation(@RequestBody Restaurant Restaurant) {
        log.info("create {}", Restaurant);
        checkNew(Restaurant);
        Restaurant created = repository.save(Restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        repository.delete(id);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Restaurant Restaurant, @PathVariable int id) {
        log.info("update {} with id={}", Restaurant, id);
        assureIdConsistent(Restaurant, id);
        repository.update(Restaurant);
    }

    @GetMapping("/{id}/with-meals")
    public Restaurant getWithMeals(@PathVariable int id) {
        log.info("getWithMeals {}", id);
        return repository.getWithMeals(id);
    }

    @GetMapping("/all-with-meals")
    public List<Restaurant> getAllWithMeals() {
        log.info("getAllWithMeals ");
        return repository.getAllWithMeals();
    }
}