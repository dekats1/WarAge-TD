package com.warage.Model;

import lombok.Data;

@Data

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
}