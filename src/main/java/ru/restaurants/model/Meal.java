package ru.restaurants.model;

public class Meal extends AbstractNamedEntity {

    private Integer price;

    public Meal(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }
}
