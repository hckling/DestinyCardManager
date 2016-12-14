package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-11-24.
 */

public class Deck {
    String name;

    ArrayList<SelectedCard> allCards;
    ArrayList<SelectedCard> drawCards = new ArrayList<>();
    ArrayList<SelectedCharacter> characters = new ArrayList<>();
    SelectedCard battlefield = null;

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
        if (characters.size() == 0) {
            return true;
        }

        CardFaction faction = characters.get(0).getCard().getFaction();

        for (int i = 1; i < characters.size(); i++) {
            if (characters.get(i).getCard().faction != faction) {
                return false;
            }
        }

        return true;
    }

    private boolean cardsAreCompatibleWithCharacters() {
        for (int i = 0; i < allCards.size(); i++) {
            if ((allCards.get(i).getCard().getType() == CardType.BATTLEFIELD) || (allCards.get(i).getCard().getType() == CardType.CHARACTER)) {
                continue;
            }

            if (!cardIsCompatibleWithAnyCharacter(allCards.get(i).getCard())) {
                return false;
            }
        }

        return true;
    }

    private boolean cardIsCompatibleWithAnyCharacter(Card card) {
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getCharacterCard().isCompatible(card)) {
                return true;
            }
        }

        return false;
    }

    private int getTotalCharacterPoints() {
        int pointsTotal = 0;

        for (int i = 0; i < characters.size(); i++) {
            pointsTotal += characters.get(i).getPoints();
        }

        return pointsTotal;
    }

    public void setAvailableCards(ArrayList<SelectedCard> availableCards) {
        allCards = availableCards;

        for (SelectedCard card: allCards) {

        }
    }
}
