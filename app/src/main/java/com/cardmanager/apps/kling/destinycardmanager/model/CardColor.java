package com.cardmanager.apps.kling.destinycardmanager.model;

import java.text.ParseException;

/**
 * Created by danie on 2016-11-23.
 */

public enum CardColor {
    BLUE,
    RED,
    YELLOW,
    GREY;

    public static CardColor fromString(String str) throws ParseException {
        if (str.equalsIgnoreCase("blue")) {
            return BLUE;
        } else if (str.equalsIgnoreCase("red")) {
            return RED;
        } else if (str.equalsIgnoreCase("yellow")) {
            return YELLOW;
        } else if (str.equalsIgnoreCase("grey")) {
            return GREY;
        } else {
            throw new ParseException("Unknown color value: " + str, 0);
        }
    }
}
