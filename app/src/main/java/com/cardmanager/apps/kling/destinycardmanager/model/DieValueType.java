package com.cardmanager.apps.kling.destinycardmanager.model;

import java.text.ParseException;

/**
 * Created by danie on 2016-11-24.
 */

public enum DieValueType {
    BLANK,
    MELEE_DAMAGE,
    MELEE_DAMAGE_MODIFIER,
    RANGED_DAMAGE,
    RANGED_DAMAGE_MODIFIER,
    SHIELD,
    DISRUPT,
    DISCARD,
    SPECIAL,
    RESOURCE,
    RESOURCE_MODIFIER,
    FOCUS;

    public static DieValueType fromString(String type) throws ParseException {
        if (type.equalsIgnoreCase("blank")) {
            return BLANK;
        } else if (type.equalsIgnoreCase("meleeDamage")) {
            return MELEE_DAMAGE;
        } else if (type.equalsIgnoreCase("meleeDamageModifier")) {
            return MELEE_DAMAGE_MODIFIER;
        } else if (type.equalsIgnoreCase("rangedDamage")) {
            return RANGED_DAMAGE;
        } else if (type.equalsIgnoreCase("rangedDamageModifier")) {
            return RANGED_DAMAGE_MODIFIER;
        } else if (type.equalsIgnoreCase("shield")) {
            return SHIELD;
        } else if (type.equalsIgnoreCase("disrupt")) {
            return DISRUPT;
        } else if (type.equalsIgnoreCase("discard")) {
            return DISCARD;
        } else if (type.equalsIgnoreCase("special")) {
            return SPECIAL;
        } else if (type.equalsIgnoreCase("resource")) {
            return RESOURCE;
        } else if (type.equalsIgnoreCase("resourceModifier")) {
            return RESOURCE_MODIFIER;
        } else if (type.equalsIgnoreCase("focus")) {
            return FOCUS;
        } else {
            throw new ParseException("Unknown dice result: " + type, 0);
        }
    }
}
