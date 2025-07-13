package com.warage.Model;

import lombok.Data;

@Data
public class Model {
    private static Model model;
    private PlayerProfile player;
    private String version;
    private String jwtToken;     // НОВОЕ ПОЛЕ
    private String refreshToken; // НОВОЕ ПОЛЕ

    public synchronized static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }
}