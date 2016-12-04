package com.cardmanager.apps.kling.destinycardmanager.model;

import java.text.ParseException;

/**
 * Created by danie on 2016-11-23.
 */

public enum CardFaction {
    HERO,
    VILLAIN,
    NEUTRAL;

    public static CardFaction fromString(String str) throws ParseException {
        if (str.equalsIgnoreCase("hero")) {
            return HERO;
        } else if (str.equalsIgnoreCase("VILLAIN")) {
            return VILLAIN;
        } else if (str.equalsIgnoreCase("NEUTRAL")) {
            return NEUTRAL;
        } else {
            throw new ParseException("Unknown faction: " + str, 0);
        }
    }
}
