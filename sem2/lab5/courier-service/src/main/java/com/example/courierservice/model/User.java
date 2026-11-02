package com.example.courierservice.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "app_users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    private String fullName;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @OneToMany(mappedBy = "createdBy")
    private List<DeliveryOrder> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<LoginHistory> loginHistory = new ArrayList<>();

    public User() {}

    public User(String username, String fullName, String email,
                String password, String role) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<DeliveryOrder> getOrders() { return orders; }
    public void setOrders(List<DeliveryOrder> orders) { this.orders = orders; }

    public List<LoginHistory> getLoginHistory() { return loginHistory; }
    public void setLoginHistory(List<LoginHistory> loginHistory) { this.loginHistory = loginHistory; }
}