package com.warage.Model;

import java.time.LocalDateTime;

public class PlayerProgress {
    private Long id;
    private PlayerProfile player;
    private Level level;
    private Integer highestWaveReached;
    private Integer score;
    private Boolean completed;
    private Boolean unlockedNextLevel;
    private LocalDateTime lastPlayed;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PlayerProfile getPlayer() {
        return player;
    }

    public void setPlayer(PlayerProfile player) {
        this.player = player;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Integer getHighestWaveReached() {
        return highestWaveReached;
    }

    public void setHighestWaveReached(Integer highestWaveReached) {
        this.highestWaveReached = highestWaveReached;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getUnlockedNextLevel() {
        return unlockedNextLevel;
    }

    public void setUnlockedNextLevel(Boolean unlockedNextLevel) {
        this.unlockedNextLevel = unlockedNextLevel;
    }

    public LocalDateTime getLastPlayed() {
        return lastPlayed;
    }

    public void setLastPlayed(LocalDateTime lastPlayed) {
        this.lastPlayed = lastPlayed;
    }
}