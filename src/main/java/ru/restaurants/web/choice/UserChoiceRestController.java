package ru.restaurants.web.choice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurants.model.Choice;
import ru.restaurants.repository.choice.ChoiceRepository;
import ru.restaurants.repository.restaurant.RestaurantRepository;
import ru.restaurants.web.SecurityUtil;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = UserChoiceRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserChoiceRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private static LocalTime limitTime = LocalTime.of(11, 00);

    private final ChoiceRepository repository;
    private final RestaurantRepository restaurantRepository;

    static final String REST_URL = "/rest/choice";

    public UserChoiceRestController(ChoiceRepository repository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/by-user")
    public List<Choice> getAllByUserId() {
        log.info("getAllByUserId");
        return repository.getAllByUserId(SecurityUtil.get().getUser().getId());
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Choice> createWithLocation(@RequestBody int restaurantId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        int userId = SecurityUtil.get().getUser().getId();
        List<Choice> list = repository.getAllByUserId(userId);
        Choice choice = list.stream().
                filter((c) -> c.getRegistered().toLocalDate().equals(localDateTime.toLocalDate())).findAny().orElse(null);
        if (choice != null) {
            if (localDateTime.toLocalTime().isBefore(limitTime)) {
                log.info("update Choice, restaurant id={}", restaurantId);
                choice.setRestaurant(restaurantRepository.get(restaurantId));
                repository.update(choice);
                return ResponseEntity.ok(choice);
            } else {
                return ResponseEntity.badRequest().body(choice);
            }
        }
        log.info("create choice restaurant={}", restaurantId);
        Choice created = new Choice(restaurantRepository.get(restaurantId), SecurityUtil.get().getUser());
        Choice saved = repository.save(created);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(saved.getId()).toUri();
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }

    public static void changeLimitTime(LocalTime localTime) {
        limitTime = localTime;
    }
}