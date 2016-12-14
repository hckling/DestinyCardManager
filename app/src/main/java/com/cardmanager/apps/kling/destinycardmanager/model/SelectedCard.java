package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.EventListener;
import java.util.EventObject;

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

    public void select() {
        count ++;

        if (count == 1 && selected != null) {
            selected.notify();
        }
    };
    public void deselect() {
        count --;

        if (count == 0 && deselected != null) {
            deselected.notify();
        }
    };

    public boolean isSelected() { return count > 0; }

    public EventObject selected;
    public EventObject deselected;
}
