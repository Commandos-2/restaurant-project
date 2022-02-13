package ru.restaurants.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
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
    private Date registered = new Date();

    @Transient
    @JsonIgnore
    private LocalDate dateLastChoice;

    public User(Integer id, String name, String password, String email, Role role, Date registered, LocalDate dateLastChoice) {
        super(id, name);
        this.password = password;
        this.email = email;
        this.role = role;
        this.registered = registered;
        this.dateLastChoice = dateLastChoice;
    }

    public User(Integer id, String name, String password, String email, Role role, LocalDate dateLastChoice) {
        this(id, name, password, email, role, new Date(), dateLastChoice);
    }

    public User(Integer id, String name, String password, String email, LocalDate dateLastChoice) {
        this(id, name, password, email, null, dateLastChoice);
    }

    public User(User user) {
        this(user.getId(), user.getName(), user.getPassword(), user.getEmail(), user.getRole(), user.getRegistered(), user.dateLastChoice);
    }

    public User() {
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDate getDateLastChoice() {
        return dateLastChoice;
    }

    public void setDateLastChoice(LocalDate dateLastChoice) {
        this.dateLastChoice = dateLastChoice;
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
                ", dateLastChoice=" + dateLastChoice +
                '}';
    }
}
