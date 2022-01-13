package ru.restaurants.web.choice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.restaurants.model.Choice;
import ru.restaurants.repository.choice.ChoiceRepository;

import java.util.List;

@RestController
@RequestMapping(value = AdminChoiceRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminChoiceRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final ChoiceRepository choiceRepository;

    static final String REST_URL = "/rest/admin/choices";

    public AdminChoiceRestController(ChoiceRepository choiceRepository) {
        this.choiceRepository = choiceRepository;
    }

    @GetMapping
    public List<Choice> getAll() {
        log.info("getAll");
        return choiceRepository.getAll();
    }

    @GetMapping("/{id}")
    public Choice get(@PathVariable int id) {
        log.info("get {}", id);
        return choiceRepository.get(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        log.info("delete {}", id);
        choiceRepository.delete(id);
    }
}