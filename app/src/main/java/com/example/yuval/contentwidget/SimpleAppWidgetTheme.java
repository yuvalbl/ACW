package com.example.yuval.contentwidget;

import android.graphics.Color;

/**
 * Created by Yuval on 17/09/2016.
 */
public class SimpleAppWidgetTheme {
    private int textColor;
    private int backgroundColor;

    SimpleAppWidgetTheme(String theme) {
        switch (theme) {
            case "Classic Black":
                textColor = Color.WHITE;
                backgroundColor = Color.BLACK;
                break;
            case "Bright White":
                textColor = Color.BLACK;
                backgroundColor = Color.WHITE;
                break;
            case "Blue Sky":
                textColor = Color.WHITE;
                backgroundColor = Color.BLUE;
                break;
            case "Red Ruby":
                textColor = Color.WHITE;
                backgroundColor = Color.RED;
                break;
            case "Green Emerald":
                textColor = Color.BLUE;
                backgroundColor = Color.GREEN;
                break;
        }
    }


    public int getBackgroundColor() {
        return backgroundColor;
    }

    public int getTextColor() {
        return textColor;
    }
}
