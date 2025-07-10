package com.warage.Model;

import java.time.LocalDateTime;

public class PlayerAchievement {
    private Long playerAchievementID;
    private PlayerProfile player;
    private Achievement achievement;
    private LocalDateTime dateAchieved;

    public Long getPlayerAchievementID() {
        return playerAchievementID;
    }

    public void setPlayerAchievementID(Long playerAchievementID) {
        this.playerAchievementID = playerAchievementID;
    }

    public PlayerProfile getPlayer() {
        return player;
    }

    public void setPlayer(PlayerProfile player) {
        this.player = player;
    }

    public Achievement getAchievement() {
        return achievement;
    }

    public void setAchievement(Achievement achievement) {
        this.achievement = achievement;
    }

    public LocalDateTime getDateAchieved() {
        return dateAchieved;
    }

    public void setDateAchieved(LocalDateTime dateAchieved) {
        this.dateAchieved = dateAchieved;
    }
}