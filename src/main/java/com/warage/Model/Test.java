package com.warage.Model;

public class Test {
    private String name;
    private String description;
    private boolean isReady;
    private String date;
    private String progress;

    public Test(String name, String description, boolean isReady, String date, String progress) {
        this.name = name;
        this.description = description;
        this.isReady = isReady;
        this.date = date;
        this.progress = progress;
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

    public boolean isReady() {
        return isReady;
    }

    public void setReady(boolean ready) {
        isReady = ready;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }
}
