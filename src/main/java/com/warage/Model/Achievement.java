package com.warage.Model;


public class Achievement {

    private Integer achievementID;
    private String name;
    private String description;
    private Integer progress;
    private Integer needToReward;
    private Integer rewardMoney;
    private Integer rewardExperience;

    public Integer getAchievementID() {
        return achievementID;
    }

    public void setAchievementID(Integer achievementID) {
        this.achievementID = achievementID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProgress() {
        return progress;
    }

    public void setProgress(Integer progress) {
        this.progress = progress;
    }

    public Integer getNeedToReward() {
        return needToReward;
    }

    public void setNeedToReward(Integer needToReward) {
        this.needToReward = needToReward;
    }

    public Integer getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(Integer rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public Integer getRewardExperience() {
        return rewardExperience;
    }

    public void setRewardExperience(Integer rewardExperience) {
        this.rewardExperience = rewardExperience;
    }
}