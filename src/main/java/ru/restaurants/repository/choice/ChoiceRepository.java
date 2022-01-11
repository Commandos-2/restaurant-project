package ru.restaurants.repository.choice;

import ru.restaurants.model.Choice;

import java.util.List;

public interface ChoiceRepository {

    Choice save(Choice choice);

    void delete(int id);

    Choice get(int id);

    List<Choice> getAll();

    List<Choice> getAllByUserId(int userID);

    Choice update(Choice choice);
}
