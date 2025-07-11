package com.warage.Model;


import lombok.Data;

@Data

public class Enemy {
    private Integer enemyID;
    private String name;
    private String description;
    private Integer baseHealth;
    private Double speed;
    private Integer rewardCoins;
    private Integer rewardExperience;
    private String assetPath;
    private Boolean isInvisible;

}