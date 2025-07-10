package com.warage.Model;

public class Level {
    private Integer levelID;
    private String name;
    private String description;
    private Integer startingCoins;
    private Integer rewardMoney;
    private String mapDataPath;
    private String waveConfigPath;

    public Integer getLevelID() {
        return levelID;
    }

    public void setLevelID(Integer levelID) {
        this.levelID = levelID;
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

    public Integer getStartingCoins() {
        return startingCoins;
    }

    public void setStartingCoins(Integer startingCoins) {
        this.startingCoins = startingCoins;
    }

    public Integer getRewardMoney() {
        return rewardMoney;
    }

    public void setRewardMoney(Integer rewardMoney) {
        this.rewardMoney = rewardMoney;
    }

    public String getMapDataPath() {
        return mapDataPath;
    }

    public void setMapDataPath(String mapDataPath) {
        this.mapDataPath = mapDataPath;
    }

    public String getWaveConfigPath() {
        return waveConfigPath;
    }

    public void setWaveConfigPath(String waveConfigPath) {
        this.waveConfigPath = waveConfigPath;
    }
}