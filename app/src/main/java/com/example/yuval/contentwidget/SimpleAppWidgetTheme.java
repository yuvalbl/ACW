package com.example.yuval.contentwidget;

import android.graphics.Color;

/**
 * Created by Yuval on 17/09/2016.
 */
public class SimpleAppWidgetTheme {
    private int textColor;
    private int backgroundColor;
    private int buttonColor;

    SimpleAppWidgetTheme(String theme) {
        switch (theme) {
            case "Classic Black":
                textColor = R.color.classicBlackText;
                backgroundColor = R.color.classicBlackBackground;
                break;
            case "Bright White":
                textColor = R.color.brightWhiteText;
                backgroundColor = R.color.brightWhiteBackground;
                buttonColor = R.color.brightWhiteButton;
                break;
            case "Blue Sky":
                textColor = R.color.blueSkyText;
                backgroundColor = R.color.blueSkyBackground;
                break;
            case "Red Ruby":
                textColor = R.color.redRubyText;
                backgroundColor = R.color.redRubyBackground;
                break;
            case "Green Emerald":
                textColor = R.color.greenEmeraldText;
                backgroundColor = R.color.greenEmeraldBackground;
                break;
        }
    }


    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public int getButtonColor() {
        return buttonColor;
    }
}
