package ru.restaurants.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurants.model.Meal;
import ru.restaurants.repository.meal.MealRepository;

import java.net.URI;
import java.util.List;

import static ru.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.restaurants.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MealRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealRepository repository;

    static final String REST_URL = "/rest/admin/meals";

    public MealRestController(MealRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/allByRestaurant/{restaurantId}")
    public List<Meal> getAllByRestaurant(@PathVariable int restaurantId) {
        log.info("getAllByRestaurant");
        return repository.getAllByRestaurant(restaurantId);
    }

    @GetMapping("/{id}")
    public Meal get(@PathVariable int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @PostMapping(value = "/{restaurantId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Meal> createWithLocation(@RequestBody Meal meal,@PathVariable int restaurantId) {
        log.info("create {}", meal);
        checkNew(meal);
        Meal created = repository.save(meal,restaurantId);
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

    @PutMapping(value = "/{restaurantId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody Meal meal,@PathVariable int restaurantId) {
        log.info("update {} with id={}", meal, meal.getId());
        assureIdConsistent(meal, meal.getId());
        repository.update(meal,restaurantId);
    }
}