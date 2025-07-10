package com.warage.Model;

public class TowerUpgrade {
    private Integer upgradeID;
    private Tower tower;
    private Integer upgradePath;
    private Integer upgradeLevel;
    private Integer cost;
    private Integer damage;
    private Double range;
    private Double attackSpeed;
    private String specialEffectDescription;
    private Boolean canDetectInvisible;

    public Integer getUpgradeID() {
        return upgradeID;
    }

    public void setUpgradeID(Integer upgradeID) {
        this.upgradeID = upgradeID;
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public Integer getUpgradePath() {
        return upgradePath;
    }

    public void setUpgradePath(Integer upgradePath) {
        this.upgradePath = upgradePath;
    }

    public Integer getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(Integer upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public Integer getCost() {
        return cost;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public Integer getDamage() {
        return damage;
    }

    public void setDamage(Integer damage) {
        this.damage = damage;
    }

    public Double getRange() {
        return range;
    }

    public void setRange(Double range) {
        this.range = range;
    }

    public Double getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(Double attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public String getSpecialEffectDescription() {
        return specialEffectDescription;
    }

    public void setSpecialEffectDescription(String specialEffectDescription) {
        this.specialEffectDescription = specialEffectDescription;
    }

    public Boolean getCanDetectInvisible() {
        return canDetectInvisible;
    }

    public void setCanDetectInvisible(Boolean canDetectInvisible) {
        this.canDetectInvisible = canDetectInvisible;
    }
}