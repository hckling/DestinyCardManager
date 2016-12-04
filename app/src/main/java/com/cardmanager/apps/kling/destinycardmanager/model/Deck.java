package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-11-24.
 */

public class Deck {
    String name;

    ArrayList<Card> cards = new ArrayList<Card>();
    ArrayList<Card> drawCards = new ArrayList<Card>();
    ArrayList<CharacterCard> characters = new ArrayList<CharacterCard>();
    Card battlefield = null;

    public String getName() {return name; }
    public void setName(String name) { this.name = name; }

    public boolean isValid() {
        if (battlefield == null) {
            return false;
        }

        if (characters.size() < 1) {
            return false;
        }

        if (getTotalCharacterPoints() > 30) {
            return false;
        }

        if (!charactersAreCompatible()) {
            return false;
        }

        if (!cardsAreCompatibleWithCharacters()) {
            return false;
        }

        return true;
    }

    private boolean charactersAreCompatible() {
        CardFaction faction = characters.get(0).getFaction();

        for (int i = 1; i < characters.size(); i++) {
            if (characters.get(i).faction != faction) {
                return false;
            }
        }

        return true;
    }

    private boolean cardsAreCompatibleWithCharacters() {
        for (int i = 0; i < cards.size(); i++) {
            if ((cards.get(i).getType() == CardType.BATTLEFIELD) || (cards.get(i).getType() == CardType.CHARACTER)) {
                continue;
            }

            if (!cardIsCompatibleWithAnyCharacter(cards.get(i))) {
                return false;
            }
        }

        return true;
    }

    private boolean cardIsCompatibleWithAnyCharacter(Card card) {
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).isCompatible(card)) {
                return true;
            }
        }

        return false;
    }

    private int getTotalCharacterPoints() {
        int pointsTotal = 0;

        for (int i = 0; i < characters.size(); i++) {
            pointsTotal += characters.get(i).getPointsValue();
        }

        return pointsTotal;
    }
}
