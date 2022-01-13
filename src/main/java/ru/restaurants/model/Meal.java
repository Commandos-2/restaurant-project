package ru.restaurants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.validator.constraints.Range;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "meals")
public class Meal extends AbstractNamedEntity {

    @Column(name = "price", nullable = false)
    @Range(min = 1, max = 10000)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference()
    @NotNull
    private Restaurant restaurant;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDateTime registered = LocalDateTime.now();

    public Meal(Integer id, String name, Integer price) {
        super(id, name);
        this.price = price;
    }

    public Meal(Integer id, String name, Integer price, Restaurant restaurant) {
        super(id, name);
        this.price = price;
        this.restaurant = restaurant;
    }

    public Meal(Meal meal) {
        super(meal.getId(), meal.getName());
        this.price = meal.getPrice();
        this.registered = meal.registered;
    }

    public Meal() {
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", restaurant=" + restaurant +
                ", registered=" + registered +
                '}';
    }
}
