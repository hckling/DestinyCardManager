package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.EventListener;
import java.util.EventObject;

/**
 * Created by danie on 2016-12-13.
 */

public class SelectableCard {
    Card card;
    int count = 0;
    boolean isAvailable = true;

    public SelectableCard(Card card) { this.card = card; }

    public Card getCard() {
        return card;
    }

    public int getCount() {
        return count;
    }

    public void select() {
        count ++;

        if (count == 1 && selectionChanged != null) {
            selectionChanged.selectionStateChanged();
        }
    };
    public void deselect() {
        count --;

        if (count == 0 && selectionChanged != null) {
            selectionChanged.selectionStateChanged();
        }
    };

    public boolean isSelected() { return count > 0; }

    public SelectionListener selectionChanged;

    public boolean isAvailableForSelection() { return isAvailable; }
    public void makeUnavailableForSelection() { isAvailable = false; }
    public void makeAvailableForSelection () { isAvailable = true; }
}
