package com.cardmanager.apps.kling.destinycardmanager.model;

/**
 * Created by danie on 2016-12-13.
 */

public class SelectedCard {
    Card card;
    int count = 0;

    public SelectedCard(Card card) { this.card = card; }

    public Card getCard() {
        return card;
    }

    public int getCount() {
        return count;
    }

    public void select() { count ++; };
    public void deselect() { count --; };

    public boolean isSelected() { return count > 0; }
}
