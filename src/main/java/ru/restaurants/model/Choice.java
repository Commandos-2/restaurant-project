package ru.restaurants.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "choice")
public class Choice extends AbstractBaseEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id",nullable = false)
    private Restaurant restaurant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()")
    @NotNull
    private LocalDateTime registered=LocalDateTime.now();

    public Choice(Integer id, Restaurant restaurant, User user, LocalDateTime registered) {
        super(id);
        this.restaurant = restaurant;
        this.user = user;
        this.registered = registered;
    }

    public Choice(Choice choice) {
        this.restaurant = choice.getRestaurant();
        this.user = choice.user;
        this.registered = choice.registered;
        this.id=choice.getId();
    }

    public Choice(Integer id) {
        super(id);
    }

    public Choice(Restaurant restaurant, User user) {
        this.restaurant = restaurant;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Choice() {
    }

    public LocalDateTime getRegistered() {
        return registered;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void setRegistered(LocalDateTime registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "id=" + id +
                ", restaurant=" + restaurant +
                ", user=" + user +
                ", registered=" + registered +
                '}';
    }
}
