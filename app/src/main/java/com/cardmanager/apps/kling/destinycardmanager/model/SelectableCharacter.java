package com.cardmanager.apps.kling.destinycardmanager.model;

/**
 * Created by danie on 2016-12-13.
 */

public class SelectableCharacter extends SelectableCard {
    boolean isElite = false;

    public SelectableCharacter(CharacterCard card) {
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

    public void makeElite() {
        isElite = true;
        if (selectionChanged != null) {
            selectionChanged.selectionStateChanged();
        }
    }

    public void makeNormal() {
        isElite = false;

        if (selectionChanged != null) {
            selectionChanged.selectionStateChanged();
        }
    }

    public boolean isElite() { return isElite; }
}
