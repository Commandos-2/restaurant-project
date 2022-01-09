package ru.restaurants.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
public class User extends AbstractNamedEntity implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "password", nullable = false)
    @NotBlank
    @Size(min = 5, max = 32)
    private String password;

    @Column(name = "email", nullable = false, unique = true)
    @Email
    @NotBlank
    @Size(max = 100)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "registered", nullable = false, columnDefinition = "timestamp default now()", updatable = false)
    @NotNull
    private Date registered=new Date();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "restaurant_id")
    private Restaurant restaurant;

    public User(Integer id, String name, String password, String email, Role role, Date registered) {
        super(id, name);
        this.password = password;
        this.email = email;
        this.role = role;
        this.registered = registered;
        this.restaurant = null;
    }

    public User(Integer id, String name, String password, String email, Role role) {
        this(id,name,password,email,role,new Date());
    }

    public User(User user) {
        this(user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getRole());
    }

    public User() {
    }

    public Date getRegistered() {
        return registered;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", registered=" + registered +
                ", restaurant=" + restaurant +
                '}';
    }
}
