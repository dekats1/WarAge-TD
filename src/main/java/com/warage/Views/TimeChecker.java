package com.warage.Views;

import com.warage.Utils.ImageLoaderUtil;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.time.LocalTime;

public class TimeChecker {
    private static final LocalTime startTime = LocalTime.of(22,0);
    private static final LocalTime endTime = LocalTime.of(6,0);

    public static void setBackgroundImage(ImageView imageView){
        LocalTime now = LocalTime.now();
        boolean isNight;
        if (startTime.isBefore(endTime)) {
            isNight = now.isAfter(startTime) && now.isBefore(endTime);
        } else {
            isNight = now.isAfter(startTime) || now.isBefore(endTime);
        }
        if(isNight){
            imageView.setImage(ImageLoaderUtil.loadImage("/Image/MainMenuNightBackground.png"));
        }else{
            imageView.setImage(ImageLoaderUtil.loadImage("/Image/MainMenuBackground.png"));
        }
    }

    public static boolean setBackgroundImageOnMain(ImageView backgroundImage) {
        LocalTime now = LocalTime.now();
        boolean isNight;
        if (startTime.isBefore(endTime)) {
            isNight = now.isAfter(startTime) && now.isBefore(endTime);
        } else {
            isNight = now.isAfter(startTime) || now.isBefore(endTime);
        }
        if(isNight){
            backgroundImage.setImage(ImageLoaderUtil.loadImage("/Image/MenuNight.png"));
        }else{
            backgroundImage.setImage(ImageLoaderUtil.loadImage("/Image/MenuDay.png"));
        }
        return isNight;
    }
}
