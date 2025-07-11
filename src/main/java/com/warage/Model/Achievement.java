package com.warage.Model;


import lombok.Data;

@Data
public class Achievement {
    private Integer achievementID;
    private String name;
    private String description;
    private Integer progress;
    private Integer needToReward;
    private Integer rewardMoney;
    private Integer rewardExperience;


}