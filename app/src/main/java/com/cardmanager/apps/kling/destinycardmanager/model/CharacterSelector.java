package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by danie on 2016-12-16.
 */

public class CharacterSelector {
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
            c.addSelectionListener(new SelectionListener() {
                @Override
                public void selectionChanged() {
                    raiseSelectionChanged();
                }
            });
        }
    }

    private void raiseSelectionChanged() {
        for(SelectionListener l: selectionChangedListeners) {
            l.selectionChanged();
        }
    }

    public void filterBySelection() {
        // When the character selection has changed, the available cards will change as well.

        selectedCharacters.clear();

        totalCharacterPoints = 0;

        // Find all currently selected characters
        for(CharacterSelectionInfo character: availableCharacters) {
            if (character.getCount() > 0) {
                totalCharacterPoints += character.getPoints();
                selectedCharacters.add(character);
            }
        }

        availableCharacters.addAll(unavailableCharacters);
        unavailableCharacters.clear();

        int remainingPoints = MAX_POINTS - totalCharacterPoints;

        // If we have any selected characters
        if (selectedCharacters.size() > 0) {
            for (CharacterSelectionInfo character: availableCharacters) {
                // Check if the character can be elited, based on points cost
                checkIfEliteIsAllowed(remainingPoints, character);

                // Already selected characters are always available in the list
                if (selectedCharacters.contains(character)) {
                    continue;
                }

                // Remove any other characters which are either too expensive or don't match the
                // faction of the already selected characters
                for(CharacterSelectionInfo selectedCharacter: selectedCharacters) {
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
        sortList(selectedCharacters);
    }

    private void sortList(ArrayList<CharacterSelectionInfo> list) {
        list.sort(new Comparator<CharacterSelectionInfo>() {
            @Override
            public int compare(CharacterSelectionInfo o1, CharacterSelectionInfo o2) {
                return o1.getCard().getCardNumber() - o2.getCard().getCardNumber();
            }
        });
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
    public ArrayList<CharacterSelectionInfo> getSelectedCharacters() { return selectedCharacters; }
    public int getTotalCharacterPoints() { return totalCharacterPoints; }

    public void addSelectionChangedListener(SelectionListener listener) { selectionChangedListeners.add(listener); }
}
