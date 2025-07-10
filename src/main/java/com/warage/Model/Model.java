package com.warage.Model;


public class Model {
    private static Model model;


    public synchronized static Model getInstance() {
        if (model == null) {
            model = new Model();
        }
        return model;
    }

}
