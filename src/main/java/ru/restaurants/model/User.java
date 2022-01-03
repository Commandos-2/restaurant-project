package ru.restaurants.model;

import java.time.LocalDateTime;

public class User extends AbstractNamedEntity {

    private final String password;
    private final String email;
    private final Role role;
    private final LocalDateTime dateTime;
    private Restaurant restaurant;

    public User(Integer id, String name, String password, String email, Role role, LocalDateTime dateTime) {
        super(id, name);
        this.password = password;
        this.email = email;
        this.role = role;
        this.dateTime = dateTime;
        this.restaurant = null;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }
}
