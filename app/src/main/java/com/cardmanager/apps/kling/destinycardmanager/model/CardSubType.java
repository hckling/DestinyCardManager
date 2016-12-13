package com.cardmanager.apps.kling.destinycardmanager.model;

/**
 * Created by danie on 2016-11-25.
 */

public enum CardSubType {
    NONE,
    WEAPON,
    VEHICLE,
    DROID,
    ABILITY,
    EQUIPMENT;

    public static CardSubType fromString(String str) {
        if (str != null) {
            if (str.equalsIgnoreCase("weapon")) {
                return WEAPON;
            } else if (str.equalsIgnoreCase("vehicle")) {
                return VEHICLE;
            } else if (str.equalsIgnoreCase("droid")) {
                return DROID;
            } else if (str.equalsIgnoreCase("ability")) {
                return ABILITY;
            } else if (str.equalsIgnoreCase("equipment")) {
                return EQUIPMENT;
            } else {
                return NONE;
            }
        } else {
            return NONE;
        }
    }

    @Override
    public String toString() {
        switch (this) {
            case ABILITY: return "Ability";
            case DROID: return "Droid";
            case EQUIPMENT: return "Equipment";
            case NONE: return "";
            case VEHICLE: return "Vehicle";
            case WEAPON: return "Weapon";
            default: return "Unknown";
        }
    }
}
