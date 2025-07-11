package com.warage.Model;

import lombok.Data;

@Data

public class Level {
    private Integer levelID;
    private String name;
    private String description;
    private Integer startingCoins;
    private Integer rewardMoney;
    private String mapDataPath;
    private String waveConfigPath;

}