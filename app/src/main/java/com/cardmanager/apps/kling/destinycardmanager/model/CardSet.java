package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Class representing a card set, such as the "Awakening" set.
 */

public class CardSet {
    private String name = "";
    private ArrayList<Card> cards = new ArrayList<>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ArrayList<Card> getCards() { return cards; }

    protected void addCard(Card card) {
        card.setSet(name);
        cards.add(card);
    }

    public Card getCard(String cardName) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getName().equals(cardName)) {
                return cards.get(i);
            }
        }

        return null;
    }
}
