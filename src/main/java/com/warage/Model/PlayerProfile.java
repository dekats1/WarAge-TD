package com.warage.Model;

import java.time.LocalDateTime;

public class PlayerProfile {

    private Long id;
    private String username;
    private String email;
    private String hashedPassword;
    private int money;
    private int experience;
    private LocalDateTime lastLogin;
    private int maxDamage;
    private int endlessHighestWave;
    private int endlessHighScore;

    public PlayerProfile(Long id, String username, String email, String hashedPassword, int coins, int experience, LocalDateTime lastLogin,int maxDamage, int endlessHighestWave, int endlessHighScore) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.hashedPassword = hashedPassword;
        this.money = coins;
        this.experience = experience;
        this.lastLogin = lastLogin;
        this.maxDamage = maxDamage;
        this.endlessHighestWave = endlessHighestWave;
        this.endlessHighScore = endlessHighScore;
    }

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

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
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
                ", email='" + email + '\'' +
                ", hashedPassword='" + hashedPassword + '\'' +
                ", coins=" + money +
                ", experience=" + experience +
                ", lastLogin=" + lastLogin +
                '}';
    }

    public int getMaxDamage() {
        return maxDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public int getEndlessHighestWave() {
        return endlessHighestWave;
    }

    public void setEndlessHighestWave(int endlessHighestWave) {
        this.endlessHighestWave = endlessHighestWave;
    }

    public int getEndlessHighScore() {
        return endlessHighScore;
    }

    public void setEndlessHighScore(int endlessHighScore) {
        this.endlessHighScore = endlessHighScore;
    }
}