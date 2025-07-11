package com.warage.Model;

import lombok.Data;

import java.time.LocalDateTime;
@Data

public class PlayerAchievement {
    private Long playerAchievementID;
    private PlayerProfile player;
    private Achievement achievement;
    private LocalDateTime dateAchieved;


}