package com.warage.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data

public class PlayerProfile {

    private Long playerID;
    private String username;
    private String email;
    private String hashedPassword;
    private int money;
    private int experience;
    private LocalDateTime lastLogin;
    private int maxDamage;
    private int endlessHighestWave;
    private int endlessHighScore;


}