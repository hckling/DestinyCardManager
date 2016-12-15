package com.cardmanager.apps.kling.destinycardmanager.model;

import com.cardmanager.apps.kling.destinycardmanager.R;

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

    public int getImageResourceId() {
        switch (this) {
            case BLANK: return R.drawable.blank_small;
            case DISCARD: return R.drawable.discard_small;
            case DISRUPT: return R.drawable.disrupt_small;
            case FOCUS: return R.drawable.focus_small;
            case MELEE_DAMAGE: return R.drawable.melee_small;
            case MELEE_DAMAGE_MODIFIER: return R.drawable.melee_small;
            case RANGED_DAMAGE: return R.drawable.ranged_small;
            case RANGED_DAMAGE_MODIFIER: return R.drawable.ranged_small;
            case RESOURCE: return R.drawable.resource_small;
            case RESOURCE_MODIFIER: return R.drawable.resource_small;
            case SHIELD: return R.drawable.shield_small;
            case SPECIAL: return R.drawable.special_small;
            default: return R.drawable.blank_small;
        }
    }

    public boolean isModifier() {
        if (this == RESOURCE_MODIFIER || this == MELEE_DAMAGE_MODIFIER || this == RANGED_DAMAGE_MODIFIER) {
            return true;
        } else {
            return false;
        }
    }
}
