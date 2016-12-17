package com.cardmanager.apps.kling.destinycardmanager.model;

/**
 * Created by danie on 2016-12-13.
 */

public class CharacterSelectionInfo extends CardSelectionInfo {
    boolean isElite = false;
    private boolean eliteAllowed = true;

    public CharacterSelectionInfo(CharacterCard card) {
        super(card);
    }

    public int getPoints() {
        if (isElite) {
            return getCharacterCard().getElitePointCost();
        } else {
            return getCharacterCard().getNormalPointCost();
        }
    }

    public CharacterCard getCharacterCard() { return (CharacterCard) getCard(); }

    public boolean isEliteAllowed() { return eliteAllowed && getCharacterCard().getCanBeElite(); }
    public void allowElite() { eliteAllowed = true; }
    public void disallowElite() {eliteAllowed = false; }

    public void makeElite() {
        isElite = true;
        raiseSelectionChanged();
    }

    public void makeNormal() {
        isElite = false;
        raiseSelectionChanged();
    }

    public boolean isElite() { return isElite; }

    @Override
    public int getDiceCount() {
        if (isElite()) {
            return 2;
        } else {
            return 1;
        }
    }
}
