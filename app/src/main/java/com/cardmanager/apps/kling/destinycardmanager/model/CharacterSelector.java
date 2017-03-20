package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by danie on 2016-12-16.
 */

public class CharacterSelector implements Selector<CharacterSelectionInfo> {
    public final static int MAX_POINTS = 30;

    private int totalCharacterPoints = 0;

    private ArrayList<CharacterSelectionInfo> availableCharacters = new ArrayList<>();
    private ArrayList<CharacterSelectionInfo> selectedCharacters = new ArrayList<>();
    private ArrayList<CharacterSelectionInfo> unavailableCharacters = new ArrayList<>();
    private ArrayList<SelectionListener> selectionChangedListeners = new ArrayList<>();

    public CharacterSelector(ArrayList<CharacterCard> allCards) {
        for (CharacterCard card: allCards) {
            if (card.isUnique()) {
                if (card.getOwnedCount() > 0) {
                    availableCharacters.add(new CharacterSelectionInfo(card));
                }
            } else {
                for (int i = 0; i < card.getOwnedCount(); i++) {
                    availableCharacters.add(new CharacterSelectionInfo(card));
                }
            }
        }

        for(CharacterSelectionInfo c: availableCharacters) {
            c.addSelectionListener(() -> raiseSelectionChanged());
        }
    }

    private void raiseSelectionChanged() {
        for(SelectionListener l: selectionChangedListeners) {
            l.selectionChanged();
        }
    }

    public void filterByCharacterSelection(ArrayList<CharacterSelectionInfo> selectedCharacters) {
        // When the character selection has changed, the available cards will change as well.

        this.selectedCharacters.clear();

        totalCharacterPoints = 0;

        // Find all currently selected characters
        for(CharacterSelectionInfo character: availableCharacters) {
            if (character.getCount() > 0) {
                totalCharacterPoints += character.getPoints();
                this.selectedCharacters.add(character);
            }
        }

        availableCharacters.addAll(unavailableCharacters);
        unavailableCharacters.clear();

        int remainingPoints = MAX_POINTS - totalCharacterPoints;

        // If we have any selected characters
        if (this.selectedCharacters.size() > 0) {
            for (CharacterSelectionInfo character: availableCharacters) {
                // Check if the character can be elited, based on points cost
                checkIfEliteIsAllowed(remainingPoints, character);

                // Already selected characters are always available in the list
                if (this.selectedCharacters.contains(character)) {
                    continue;
                }

                // Remove any other characters which are either too expensive or don't match the
                // faction of the already selected characters
                for(CharacterSelectionInfo selectedCharacter: this.selectedCharacters) {
                    if ((character.getCharacterCard().isCompatible(selectedCharacter.getCard()) && (character.getCharacterCard().getNormalPointCost() <= remainingPoints))) {
                        character.makeAvailableForSelection();
                    } else {
                        character.makeUnavailableForSelection();
                    }
                }
            }
        } else {
            availableCharacters.addAll(unavailableCharacters);

            for(CharacterSelectionInfo c: availableCharacters) {
                c.makeAvailableForSelection();
            }
        }

        // Now actually move all characters marked as unavailable to the unavailableForSelection list
        filterUnavailableCharacters();
        sortList(availableCharacters);
        sortList(this.selectedCharacters);
    }

    private void sortList(ArrayList<CharacterSelectionInfo> list) {
        Collections.sort(list, (o1, o2) -> o1.getCard().getCardNumber() - o2.getCard().getCardNumber());
    }

    private void checkIfEliteIsAllowed(int remainingPoints, CharacterSelectionInfo character) {
        if ((character.getCharacterCard().getElitePointCost() - character.getCharacterCard().getNormalPointCost()) <= remainingPoints) {
            character.allowElite();
        } else {
            character.disallowElite();
        }
    }

    private void filterUnavailableCharacters() {
        int i = 0;

        while (i < availableCharacters.size()) {
            if (!availableCharacters.get(i).isAvailableForSelection()) {
                unavailableCharacters.add(availableCharacters.get(i));
                availableCharacters.remove(i);
            } else {
                i++;
            }
        }
    }

    public ArrayList<CharacterSelectionInfo> getAvailable() { return availableCharacters; }
    public ArrayList<CharacterSelectionInfo> getSelected() { return selectedCharacters; }
    public int getSelectionCount() { return selectedCharacters.size(); }

    @Override
    public void maxCardsReached() {
        // OK - Managed internally
    }

    @Override
    public void maxCardsNotReached(ArrayList<CharacterSelectionInfo> selectedCharacters) {
        // OK - Managed internally
    }

    @Override
    public void select(CharacterSelectionInfo item) {
        for (int i = 0; i < availableCharacters.size(); i++) {
            CharacterSelectionInfo c = availableCharacters.get(i);

            if (c.getCard().getCardNumber() == item.getCard().getCardNumber() && c.getCount() == 0) {
                c.select();
                if (item.isElite())
                    c.makeElite();
                break;
            }
        }
    }

    public void addSelectionChangedListener(SelectionListener listener) { selectionChangedListeners.add(listener); }
}
