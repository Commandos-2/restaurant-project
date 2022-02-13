package ru.restaurants.repository.choice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.restaurants.model.Choice;
import ru.restaurants.repository.AbstractRepositoryTest;
import ru.restaurants.util.exсeption.NotFoundException;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.restaurants.ChoiceTestData.*;
import static ru.restaurants.TestUtil.setDateChoice;
import static ru.restaurants.UserTestData.user;

class DataJpaChoiceRepositoryTest extends AbstractRepositoryTest {
    @Autowired
    protected ChoiceRepository choiceRepository;

    @Test
    public void create() {
        Choice created = choiceRepository.save(new Choice(choice4));
        int newId = created.getId();
        Choice newChoice = new Choice(choice4);
        newChoice.setId(newId);
        CHOICE_MATCHER.assertMatch(created, newChoice);
        CHOICE_MATCHER.assertMatch(choiceRepository.get(newId), newChoice);
    }

    @Test
    void delete() {
        choiceRepository.delete(CHOICE_1_ID);
        assertThrows(NotFoundException.class, () -> choiceRepository.get(CHOICE_1_ID));
    }

    @Test
    void deletedNotFound() {
        assertThrows(NotFoundException.class, () -> choiceRepository.delete(NOT_FOUND));
    }

    @Test
    void get() {
        Choice Choice = choiceRepository.get(CHOICE_1_ID);
        CHOICE_MATCHER.assertMatch(Choice, choice1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> choiceRepository.get(NOT_FOUND));
    }

    @Test
    void update() {
        Choice updated = choice1;
        choiceRepository.update(updated);
        CHOICE_MATCHER.assertMatch(choiceRepository.get(CHOICE_1_ID), choice1);
    }

    @Test
    void getAll() {
        List<Choice> all = choiceRepository.getAll();
        CHOICE_MATCHER.assertMatch(all, choices);
    }

    @Test
    void getAllByUserId() {
        List<Choice> all = choiceRepository.getAllByUserId(user.getId());
        CHOICE_MATCHER.assertMatch(all, choice1, choice3);
    }

    @Test
    void getLastChoiceByUser() {
        setDateChoice(choiceRepository, LocalTime.MIN, false);
        Choice choice = choiceRepository.getLastChoiceByUser(user.getId());
        CHOICE_MATCHER.assertMatch(choice, choice1);
    }
}