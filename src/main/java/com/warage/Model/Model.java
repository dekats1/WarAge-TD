package com.warage.Model;


public class Model {
    private static Model model;
    private PlayerProfile player;

    public synchronized static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

    public PlayerProfile getPlayer() {return player;}

    public void setPlayer(PlayerProfile player) {this.player = player;}
}
