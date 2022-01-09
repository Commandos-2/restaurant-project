package ru.restaurants.web.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.restaurants.model.User;
import ru.restaurants.repository.user.UserRepository;

import java.net.URI;

import static ru.restaurants.util.ValidationUtil.assureIdConsistent;
import static ru.restaurants.web.SecurityUtil.authUserId;

@RestController
@RequestMapping(value = ProfileRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController{
    static final String REST_URL = "/rest/profile";

    private final UserRepository repository;

    public ProfileRestController(UserRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public User get() {
        return repository.get(authUserId());
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        repository.delete(authUserId());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<User> register(@RequestBody User user) {
        User created = repository.save(user);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL).build().toUri();
        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@RequestBody User user) {
        assureIdConsistent(user,authUserId());
            repository.update(user);
    }

    @GetMapping("/text")
    public String testUTF() {
        return "Русский текст";
    }

 /*   @GetMapping("/with-meals")
    public User getWithMeals( @ApiIgnore @AuthenticationPrincipal AuthorizedUser authUser) {
        return repository.getWithMeals(authUser.getId());
    }*/
}