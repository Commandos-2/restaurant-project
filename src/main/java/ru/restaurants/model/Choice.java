package ru.restaurants.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "choice")
public class Choice extends AbstractBaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id", nullable = false)
    @JsonBackReference(value = "restaurant")
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference(value = "user")
    private User user;

    @Column(name = "date", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDate date = LocalDate.now();

    public Choice(Integer id, Restaurant restaurant, User user, LocalDate date) {
        super(id);
        this.restaurant = restaurant;
        this.user = user;
        this.date = date;
    }

    public Choice(Restaurant restaurant, User user) {
        this(null, restaurant, user, LocalDate.now());
    }

    public Choice(Choice choice) {
        this.restaurant = choice.getRestaurant();
        this.user = choice.user;
        this.date = choice.date;
        this.id = choice.getId();
    }

    public Choice() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate registered) {
        this.date = registered;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "id=" + id +
                ", date=" + date +
                '}';
    }
}
