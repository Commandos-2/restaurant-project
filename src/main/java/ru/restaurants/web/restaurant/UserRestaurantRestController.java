package ru.restaurants.web.restaurant;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.restaurants.model.Restaurant;
import ru.restaurants.repository.restaurant.RestaurantRepository;

import java.util.List;

@RestController
@RequestMapping(value = UserRestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserRestaurantRestController {
    static final String REST_URL = "/rest/restaurants";
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final RestaurantRepository restaurantRepository;

    public UserRestaurantRestController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/{id}/with-dishs-today")
    public Restaurant getWithDishesToday(@PathVariable int id) {
        log.info("getWithDishesToday {}", id);
        return restaurantRepository.getWithDishesToday(id);
    }

    @GetMapping("/all-with-dishs-today")
    public List<Restaurant> getAllWithDishesToday() {
        log.info("getAllWithDishesToday ");
        return restaurantRepository.getAllWithDishesToday();
    }
}