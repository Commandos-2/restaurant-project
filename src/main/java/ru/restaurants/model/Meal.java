package ru.restaurants.model;

import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "meals")
public class Meal extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 10000)
    private Integer price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @NotNull
    private Restaurant restaurant;

    public Meal(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }

    public Meal() {
    }
}
