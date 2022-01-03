package ru.restaurants.model;

import java.util.List;

public class Restaurant extends AbstractNamedEntity {
    private List<Meal> meals;
    private Integer votes;

    public Restaurant(Integer id, String name, List<Meal> meals, Integer votes) {
        super(id, name);
        this.meals = meals;
        this.votes = votes;
    }
}
