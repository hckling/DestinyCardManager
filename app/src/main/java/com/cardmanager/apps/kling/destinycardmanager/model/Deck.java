package com.cardmanager.apps.kling.destinycardmanager.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.EventListener;

/**
 * Created by danie on 2016-11-24.
 */

public class Deck {
    private String name;

    private final static int MAX_POINTS = 30;

    private ArrayList<Card> allBattlefields = new ArrayList<>();
    private ArrayList<CharacterCard> allCharacters = new ArrayList<>();
    private ArrayList<Card> allUpgrades = new ArrayList<>();
    private ArrayList<Card> allSupport = new ArrayList<>();
    private ArrayList<Card> allEvents = new ArrayList<>();

    private ArrayList<SelectableCharacter> selectableCharacters = new ArrayList<>();
    private ArrayList<SelectableCharacter> nonSelectableCharacters = new ArrayList<>();

    private ArrayList<SelectableCard> selectableUpgrades = new ArrayList<>();
    private ArrayList<SelectableCard> selectableSupport = new ArrayList<>();
    private ArrayList<SelectableCard> selectableEvents = new ArrayList<>();
    private ArrayList<SelectableCard> selectableBattlefields = new ArrayList<>();

    public ArrayList<SelectionListener> availableCardsChanged = new ArrayList<>();

    private int totalCharacterPoints = 0;

    public String getName() {return name; }
    public void setName(String name) { this.name = name; }

    public boolean isValid() {
        if (selectableBattlefields.size() < 1) {
            return false;
        }

        if (selectableCharacters.size() < 1) {
            return false;
        }

        if (getTotalCharacterPoints() > MAX_POINTS) {
            return false;
        }

        if (!charactersAreCompatible()) {
            return false;
        }

        return true;
    }

    private boolean charactersAreCompatible() {
        if (selectableCharacters.size() == 0) {
            return true;
        }

        CardFaction faction = selectableCharacters.get(0).getCard().getFaction();

        for (int i = 1; i < selectableCharacters.size(); i++) {
            if (selectableCharacters.get(i).getCount() > 0) {
                if (selectableCharacters.get(i).getCard().faction != faction) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean cardIsCompatibleWithAnyCharacter(Card card) {
        for (int i = 0; i < selectableCharacters.size(); i++) {
            if (selectableCharacters.get(i).getCount() > 0) {
                if (selectableCharacters.get(i).getCharacterCard().isCompatible(card)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void setAvailableCards(ArrayList<Card> availableCards) {
        // Split the cards by type (maybe some other class should do this...)
        for (Card card: availableCards) {
            if (card.getType() == CardType.CHARACTER) {
                allCharacters.add((CharacterCard) card);
            } else if (card.getType() == CardType.UPGRADE) {
                allUpgrades.add(card);
            } else if (card.getType() == CardType.EVENT) {
                allEvents.add(card);
            } else if (card.getType() == CardType.SUPPORT) {
                allSupport.add(card);
            } else if (card.getType() == CardType.BATTLEFIELD) {
                allBattlefields.add(card);
            }
        }

        // Create selectable cards from the cards in the user's deck
        buildSelectableCharacters();
        buildSelectableUpgrades();
        buildSelectableSupport();
        buildSelectableEvents();
        buildSelectableBattlefields();
    }

    private void buildSelectableBattlefields() {
        for (Card card: allBattlefields) {
            SelectableCard selectableCard = new SelectableCard(card);
            selectableCard.selectionChanged = new SelectionListener() {
                @Override
                public void selectionStateChanged() {
                    battlefieldSelectionChanged();
                }
            };
            selectableBattlefields.add(new SelectableCard(card));
        }
    }

    private void buildSelectableEvents() {
        for (Card card: allEvents) {
            SelectableCard selectableCard = new SelectableCard(card);
            selectableCard.selectionChanged = new SelectionListener() {
                @Override
                public void selectionStateChanged() {
                    cardSelectionChanged();
                }
            };
            selectableEvents.add(selectableCard);
        }
    }

    private void buildSelectableSupport() {
        for (Card card: allSupport) {
            SelectableCard selectableCard = new SelectableCard(card);
            selectableCard.selectionChanged = new SelectionListener() {
                @Override
                public void selectionStateChanged() {
                    cardSelectionChanged();
                }
            };

            selectableSupport.add(selectableCard);
        }
    }

    private void buildSelectableUpgrades() {
        for (Card card: allUpgrades) {
            SelectableCard selectableCard = new SelectableCard(card);
            selectableCard.selectionChanged = new SelectionListener() {
                @Override
                public void selectionStateChanged() {
                    cardSelectionChanged();
                }
            };
            selectableUpgrades.add(selectableCard);
        }
    }

    private void battlefieldSelectionChanged() {
        // TODO: Do something with this
    }

    private void cardSelectionChanged() {
        // TODO: Do something clever
    }

    private void buildSelectableCharacters() {
        for (CharacterCard card: allCharacters) {
            SelectableCharacter selectableCharacter = new SelectableCharacter(card);

            selectableCharacter.selectionChanged = new SelectionListener() {
                @Override
                public void selectionStateChanged() {
                    characterSelectionChanged();
                }
            };

            selectableCharacters.add(selectableCharacter);
        }
    }

    public void characterSelectionChanged() {
        // When the character selection has changed, the available cards will change as well.
        ArrayList<SelectableCharacter> selectedCharacters = new ArrayList<>();

        totalCharacterPoints = 0;

        // Find all currently selected characters
        for(SelectableCharacter character: selectableCharacters) {
            if (character.getCount() > 0) {
                totalCharacterPoints += character.getPoints();
                selectedCharacters.add(character);
            }
        }

        selectableCharacters.addAll(nonSelectableCharacters);
        nonSelectableCharacters.clear();

        int remainingPoints = MAX_POINTS - totalCharacterPoints;

        // If we have any selected characters
        if (selectedCharacters.size() > 0) {
            // Add any compatible characters to the list. A character is compatible if it matches
            // the selected character faction and its points cost is not higher than the remaining
            // points total.
            for (SelectableCharacter character: selectableCharacters) {
                if (selectedCharacters.contains(character)) {
                    continue;
                }

                for(SelectableCharacter selectedCharacter: selectedCharacters) {
                    if ((character.getCharacterCard().isCompatible(selectedCharacter.getCard()) && (character.getCharacterCard().getNormalPointCost() <= remainingPoints))) {
                        character.makeAvailableForSelection();
                    } else {
                        character.makeUnavailableForSelection();
                    }
                }
            }

            // TODO: filter other cards

        } else {
            for (SelectableCharacter character: selectableCharacters) {
                character.makeAvailableForSelection();
            }

            // TODO: Allow all cards
        }

        int i = 0;

        while (i < selectableCharacters.size()) {
            if (!selectableCharacters.get(i).isAvailableForSelection()) {
                nonSelectableCharacters.add(selectableCharacters.get(i));
                selectableCharacters.remove(selectableCharacters.get(i));
            } else {
                i++;
            }
        }

        for (SelectionListener listener: availableCardsChanged) {
            listener.selectionStateChanged();
        }
    }

    public ArrayList<SelectableCharacter> getAvailableCharacters() { return selectableCharacters; }
    public ArrayList<SelectableCard> getAvailableUpgrades() { return selectableUpgrades; }
    public ArrayList<SelectableCard> getAvailableSupport() { return selectableSupport; }
    public ArrayList<SelectableCard> getAvailableEvents() { return selectableEvents; }
    public ArrayList<SelectableCard> getAvailableBattlefields() { return selectableBattlefields; }

    public int getTotalCharacterPoints() { return totalCharacterPoints; }

    public void addAvailableCardsChangedListener(SelectionListener listener) { availableCardsChanged.add(listener); }
}
