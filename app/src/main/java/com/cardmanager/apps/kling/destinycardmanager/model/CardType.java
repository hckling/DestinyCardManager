package com.cardmanager.apps.kling.destinycardmanager.model;

import java.text.ParseException;

/**
 * Created by danie on 2016-11-23.
 */

public enum CardType {
    CHARACTER,
    BATTLEFIELD,
    EVENT,
    UPGRADE,
    SUPPORT;

    public static CardType fromString(String str) throws ParseException {
        if (str.equalsIgnoreCase("Character")) {
            return CHARACTER;
        } else if (str.equalsIgnoreCase("Battlefield")) {
            return BATTLEFIELD;
        } else if (str.equalsIgnoreCase("Event")) {
            return EVENT;
        } else if (str.equalsIgnoreCase("Upgrade")) {
            return UPGRADE;
        } else if (str.equalsIgnoreCase("Support")) {
            return SUPPORT;
        } else {
            throw new ParseException("Uknown card type: " + str, 0);
        }
    }
}
