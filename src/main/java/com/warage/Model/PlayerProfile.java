// src/main/java/com/warage/Model/PlayerProfile.java
package com.warage.Model;

import java.time.LocalDateTime;

public class PlayerProfile {

    private Long id;
    private String username;
    private String email; // <-- ДОБАВЛЕНО
    private String hashedPassword; // <-- ДОБАВЛЕНО
    private int coins;
    private int experience;
    private LocalDateTime lastLogin;

    public PlayerProfile() {
        // Дефолтный конструктор для Jackson
    }

    // Обновленный конструктор, если вы его используете
    public PlayerProfile(Long id, String username, String email, String hashedPassword, int coins, int experience, LocalDateTime lastLogin) {
        this.id = id;
        this.username = username;
        this.email = email; // <-- ДОБАВЛЕНО
        this.hashedPassword = hashedPassword; // <-- ДОБАВЛЕНО
        this.coins = coins;
        this.experience = experience;
        this.lastLogin = lastLogin;
    }

    // --- Геттеры и Сеттеры (обязательны для Jackson) ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    // <-- ДОБАВЛЕНЫ ГЕТТЕРЫ И СЕТТЕРЫ ДЛЯ EMAIL И HASHEDPASSWORD
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    // <-- КОНЕЦ ДОБАВЛЕННЫХ ГЕТТЕРОВ И СЕТТЕРОВ

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    @Override
    public String toString() {
        return "PlayerProfile{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' + // <-- ОБНОВЛЕНО
                ", hashedPassword='" + hashedPassword + '\'' + // <-- ОБНОВЛЕНО
                ", coins=" + coins +
                ", experience=" + experience +
                ", lastLogin=" + lastLogin +
                '}';
    }
}