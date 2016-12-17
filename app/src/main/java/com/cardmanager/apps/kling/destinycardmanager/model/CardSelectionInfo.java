package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-12-13.
 */

public class CardSelectionInfo {
    private Card card;
    private int count = 0;
    private boolean isAvailable = true;
    private ArrayList<SelectionListener> selectionListeners = new ArrayList<>();
    private ArrayList<SelectionListener> allowSelectionChangedListeners = new ArrayList<>();

    public CardSelectionInfo(Card card) { this.card = card; }

    public Card getCard() {
        return card;
    }

    public int getCount() {
        return count;
    }

    public void select() {
        count ++;

        raiseSelectionChanged();
    }

    protected void raiseSelectionChanged() {
        for (SelectionListener l: selectionListeners) {
            l.selectionChanged();
        }
    }

    ;
    public void deselect() {
        if (count > 0)
            count--;

        raiseSelectionChanged();
    };

    public boolean isSelected() { return count > 0; }

    public void addSelectionListener(SelectionListener l) { selectionListeners.add(l); };

    public boolean isAvailableForSelection() { return isAvailable; }

    public void makeUnavailableForSelection() {
        isAvailable = false;
        raiseAllowSelectionChanged();
    }
    public void makeAvailableForSelection () {
        isAvailable = true;
        raiseAllowSelectionChanged();
    }

    public void raiseAllowSelectionChanged() {
        for (SelectionListener l: allowSelectionChangedListeners) {
            l.selectionChanged();
        }
    }

    public void addAllowSelectionChangedListener(SelectionListener l) { allowSelectionChangedListeners.add(l); }

    public int getDiceCount() {
        if (card.hasDice()) {
            return 1;
        } else {
            return 0;
        }
    }

    public double getMeleeAttackRating() {
        return card.getMeleeAttackRating();
    }

    public double getRangedAttackRating() {
        return card.getRangedAttackRating();
    }

    public double getCostRating() {
        return card.getCostRating();
    }

    public double getIncomeRating() {
        return card.getIncomeRating();
    }

    public double getDefenceRating() {
        return card.getIncomeRating();
    }

    public double getFocusRating() {
        return card.getFocusRating();
    }

    public double getDisruptRating() {
        return card.getDisruptRating();
    }

    public double getDiscardRating() {
        return card.getDiscardRating();
    }
}
