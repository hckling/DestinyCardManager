package com.cardmanager.apps.kling.destinycardmanager.model;

import java.text.ParseException;

/**
 * Created by danie on 2016-11-24.
 */

public enum CardRarity {
    STARTER,
    COMMON,
    UNCOMMON,
    RARE,
    LEGENDARY;

    public static CardRarity fromString(String str) throws ParseException {
        if (str.equalsIgnoreCase("STARTER")) {
            return STARTER;
        } else if (str.equalsIgnoreCase("COMMON")) {
            return COMMON;
        } else if (str.equalsIgnoreCase("UNCOMMON")) {
            return UNCOMMON;
        } else if (str.equalsIgnoreCase("RARE")) {
            return RARE;
        } else if (str.equalsIgnoreCase("LEGENDARY")) {
            return LEGENDARY;
        } else {
            throw new ParseException("Unknown rarity: " + str, 0);
        }
    }
}
