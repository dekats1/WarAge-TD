package com.warage.Model;

import lombok.Data;

import java.time.LocalDateTime;
@Data

public class PlayerProgress {
    private Long progressID;
    private PlayerProfile player;
    private Level level;
    private Integer highestWaveReached;
    private Integer score;
    private Boolean completed;
    private Boolean unlockedNextLevel;
    private LocalDateTime lastPlayed;
}