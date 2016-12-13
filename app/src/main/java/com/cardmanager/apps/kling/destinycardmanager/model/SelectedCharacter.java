package com.cardmanager.apps.kling.destinycardmanager.model;

/**
 * Created by danie on 2016-12-13.
 */

public class SelectedCharacter extends SelectedCard {
    boolean isElite = false;

    public SelectedCharacter(CharacterCard card) {
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

    public void makeElite() { isElite = true; }
    public void makeNormal() { isElite = false; }

    public boolean isElite() { return isElite; }
}
