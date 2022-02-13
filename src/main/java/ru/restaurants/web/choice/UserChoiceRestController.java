package ru.restaurants.web.choice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurants.model.Choice;
import ru.restaurants.model.User;
import ru.restaurants.repository.choice.ChoiceRepository;
import ru.restaurants.repository.restaurant.RestaurantRepository;
import ru.restaurants.util.exсeption.NotFoundException;
import ru.restaurants.web.SecurityUtil;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = UserChoiceRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserChoiceRestController {
    static final String REST_URL = "/rest/choices";
    private static LocalTime limitTime = LocalTime.of(11, 00);
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private final ChoiceRepository choiceRepository;
    private final RestaurantRepository restaurantRepository;

    public UserChoiceRestController(ChoiceRepository choiceRepository, RestaurantRepository restaurantRepository) {
        this.choiceRepository = choiceRepository;
        this.restaurantRepository = restaurantRepository;
    }

    public static void changeLimitTime(LocalTime localTime) {
        limitTime = localTime;
    }

    @GetMapping
    public List<Choice> getAllByUserId() {
        log.info("getAllByUserId");
        return choiceRepository.getAllByUserId(SecurityUtil.get().getUser().getId());
    }

    @Transactional
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Choice> createWithLocation(@RequestBody int restaurantId) {
        LocalDateTime localDateTime = LocalDateTime.now();
        User user = SecurityUtil.get().getUser();
        LocalDate lastChoice = user.getDateLastChoice();
        Choice choice = null;
        if (lastChoice != null) {
            if (lastChoice.equals(localDateTime.toLocalDate())) {
                return ResponseEntity.unprocessableEntity().body(choice);
            }
        }
        log.info("create choice restaurant={}", restaurantId);
        Choice created = new Choice(restaurantRepository.get(restaurantId), user);
        Choice saved = choiceRepository.save(created);
        user.setDateLastChoice(localDateTime.toLocalDate());
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/today").build("");
        return ResponseEntity.created(uriOfNewResource).body(saved);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody int restaurantId) {
        log.info("update Choice, restaurant id={}", restaurantId);
        User user = SecurityUtil.get().getUser();
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDate lastChoice = user.getDateLastChoice();
        if (lastChoice != null) {
            if (lastChoice.equals(localDateTime.toLocalDate())) {
                if (localDateTime.toLocalTime().isBefore(limitTime)) {
                    Choice choice = choiceRepository.getLastChoiceByUser(user.getId());
                    if (choice != null) {
                        log.info("update Choice, restaurant id={}", restaurantId);
                        choice.setRestaurant(restaurantRepository.get(restaurantId));
                        choiceRepository.update(choice);
                        user.setDateLastChoice(localDateTime.toLocalDate());
                    } else {
                        getNotFoundException("No found last choice.");
                    }
                } else {
                    getNotFoundException("You are trying to update choice after:" + limitTime);
                }
            } else {
                getNotFoundException("No found choice today.");
            }
        } else {
            getNotFoundException("No found last choice.");
        }
    }

    private void getNotFoundException(String text) throws NotFoundException {
        throw new NotFoundException("It is not possible to update choice. " + text);
    }

    @GetMapping(value = "/today")
    public ResponseEntity<Choice> getToDay() {
        log.info("get choice ToDay");
        User user = SecurityUtil.get().getUser();
        LocalDate dateLastChoice = user.getDateLastChoice();
        if (dateLastChoice != null) {
            if (dateLastChoice.equals(LocalDate.now())) {
                return ResponseEntity.ok(choiceRepository.getToDayChoiceByUser(user.getId()));
            }
        }
        return ResponseEntity.notFound().build();
    }
}