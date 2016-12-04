package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-11-25.
 */

public class CardSet {
    String name = "";
    ArrayList<Card> cards = new ArrayList<Card>();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public ArrayList<Card> getCards() { return cards; }

    public Card getCard(String cardName) {
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i).getName().equals(cardName)) {
                return cards.get(i);
            }
        }

        return null;
    }
}
