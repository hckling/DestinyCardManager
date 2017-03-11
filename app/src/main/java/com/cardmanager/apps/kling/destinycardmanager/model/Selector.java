package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-12-18.
 */

public interface Selector<T> {
    public void filterByCharacterSelection(ArrayList<CharacterSelectionInfo> selectedCharacters);

    ArrayList<T> getAvailable();
    ArrayList<T> getSelected();

    void addSelectionChangedListener(SelectionListener listener);
    int getSelectionCount();

    void maxCardsReached();
    void maxCardsNotReached(ArrayList<CharacterSelectionInfo> selectedCharacters);

    void select(T item);
}
