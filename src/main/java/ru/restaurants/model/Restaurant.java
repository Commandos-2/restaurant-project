package ru.restaurants.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "restaurants")
public class Restaurant extends AbstractNamedEntity {

    public Restaurant(Integer id, String name, List<Meal> meals, Integer votes) {
        super(id, name);
    }

    public Restaurant() {
    }
}
