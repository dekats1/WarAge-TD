package com.warage.Model;

public class Tower {
    private Integer towerID;
    private String name;
    private String description;
    private Integer baseCost;
    private String assetPath;

    public Integer getTowerID() {
        return towerID;
    }

    public void setTowerID(Integer towerID) {
        this.towerID = towerID;
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

    public Integer getBaseCost() {
        return baseCost;
    }

    public void setBaseCost(Integer baseCost) {
        this.baseCost = baseCost;
    }

    public String getAssetPath() {
        return assetPath;
    }

    public void setAssetPath(String assetPath) {
        this.assetPath = assetPath;
    }
}