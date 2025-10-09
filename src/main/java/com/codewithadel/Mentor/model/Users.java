package com.codewithadel.Mentor.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "users",
        uniqueConstraints = @UniqueConstraint(name = "uk_users_username", columnNames = "username")
)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // works well with H2
    @Column(updatable = false, nullable = false)
    private Long id;

    @Column(nullable = false, unique = true, length = 64)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    protected Users() {
    } // JPA needs it

    public Users(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }
public Long getId() {
    return id;
}

public String getUsername() {
    return username;
}

public void setUsername(String username) {
    this.username = username;
}

public String getPasswordHash() {
    return passwordHash;
}

public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
}

    @Override
    public String toString() {

    return "User{id=" + id + ", username='" + username + "'}";
    }
}
