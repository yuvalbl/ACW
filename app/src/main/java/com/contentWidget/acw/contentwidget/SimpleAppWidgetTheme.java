package com.contentWidget.acw.contentwidget;

/**
 * Represent a theme (layout color set)
 */
public class SimpleAppWidgetTheme {
    private int textColor = 0;
    private int backgroundColor = 0;
    private int buttonColor = 0;

    //constructor, get theme name string
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
                buttonColor = R.color.greenEmeraldButton;
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
