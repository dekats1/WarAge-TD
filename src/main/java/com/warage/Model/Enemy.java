package com.warage.Model;



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

    public Integer getEnemyID() {
        return enemyID;
    }

    public void setEnemyID(Integer enemyID) {
        this.enemyID = enemyID;
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

    public Integer getBaseHealth() {
        return baseHealth;
    }

    public void setBaseHealth(Integer baseHealth) {
        this.baseHealth = baseHealth;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public Integer getRewardCoins() {
        return rewardCoins;
    }

    public void setRewardCoins(Integer rewardCoins) {
        this.rewardCoins = rewardCoins;
    }

    public Integer getRewardExperience() {
        return rewardExperience;
    }

    public void setRewardExperience(Integer rewardExperience) {
        this.rewardExperience = rewardExperience;
    }

    public String getAssetPath() {
        return assetPath;
    }

    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
    }

    public Boolean getInvisible() {
        return isInvisible;
    }

    public void setInvisible(Boolean invisible) {
        isInvisible = invisible;
    }
}