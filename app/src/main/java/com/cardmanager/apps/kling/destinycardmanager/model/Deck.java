package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-11-24.
 */

public class Deck {
    private String name;
    private int deckCardCount = 0;
    private final int MAX_DECK_SIZE = 30;

    private ArrayList<Card> allBattlefields = new ArrayList<>();
    private ArrayList<CharacterCard> allCharacters = new ArrayList<>();
    private ArrayList<Card> allUpgrades = new ArrayList<>();
    private ArrayList<Card> allSupport = new ArrayList<>();
    private ArrayList<Card> allEvents = new ArrayList<>();

    CharacterSelector characterSelector;
    CardSelector upgradeSelector;
    CardSelector supportSelector;
    CardSelector eventSelector;
    CardSelector battlefieldSelector;

    private ArrayList<CardSelectionInfo> selectableBattlefields = new ArrayList<>();
    public ArrayList<SelectionListener> availableCardsChanged = new ArrayList<>();
    private ArrayList<SelectionListener> deckChangedListener = new ArrayList<>();

    public String getName() {return name; }
    public void setName(String name) { this.name = name; }

    private void cardSelectionChanged() {
        boolean wasAtMaxSize = deckCardCount == MAX_DECK_SIZE;

        deckCardCount = 0;

        deckCardCount += upgradeSelector.getSelectedCardCount();
        deckCardCount += eventSelector.getSelectedCardCount();
        deckCardCount += supportSelector.getSelectedCardCount();

        raiseDeckChanged();

        if (deckCardCount == MAX_DECK_SIZE) {
            upgradeSelector.preventNewSelection();
            eventSelector.preventNewSelection();
            supportSelector.preventNewSelection();

            raiseAvailableCardsChanged();
        } else if (wasAtMaxSize) {
            upgradeSelector.allowNewSelection(characterSelector.getSelectedCharacters());
            eventSelector.allowNewSelection(characterSelector.getSelectedCharacters());
            supportSelector.allowNewSelection(characterSelector.getSelectedCharacters());

            raiseAvailableCardsChanged();
        }
    }

    public ArrayList<CardSelectionInfo> getAllSelectedCards() {
        ArrayList<CardSelectionInfo> result = new ArrayList<>();

        result.addAll(characterSelector.getSelectedCharacters());
        result.addAll(upgradeSelector.getSelected());
        result.addAll(supportSelector.getSelected());
        result.addAll(eventSelector.getSelected());

        return result;
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
        characterSelector = new CharacterSelector(allCharacters);
        characterSelector.addSelectionChangedListener(new SelectionListener() {
            @Override
            public void selectionChanged() {
                characterSelectionChanged();
            }
        });

        upgradeSelector = new CardSelector(allUpgrades);
        upgradeSelector.addSelectionListener(new SelectionListener() {
            @Override
            public void selectionChanged() {
                cardSelectionChanged();
            }
        });

        supportSelector = new CardSelector(allSupport);
        supportSelector.addSelectionListener(new SelectionListener() {
            @Override
            public void selectionChanged() {
                cardSelectionChanged();
            }
        });

        eventSelector = new CardSelector(allEvents);
        eventSelector.addSelectionListener(new SelectionListener() {
            @Override
            public void selectionChanged() {
                cardSelectionChanged();
            }
        });

        battlefieldSelector = new CardSelector(allBattlefields);
    }

    public void characterSelectionChanged() {
        characterSelector.filterBySelection();
        upgradeSelector.filterByCharacterSelection(characterSelector.getSelectedCharacters());
        supportSelector.filterByCharacterSelection(characterSelector.getSelectedCharacters());
        eventSelector.filterByCharacterSelection(characterSelector.getSelectedCharacters());

        raiseAvailableCardsChanged();
        raiseDeckChanged();
    }

    private void raiseDeckChanged() {
        for (SelectionListener listener: deckChangedListener) {
            listener.selectionChanged();
        }
    }

    private void raiseAvailableCardsChanged() {
        for (SelectionListener listener: availableCardsChanged) {
            listener.selectionChanged();
        }
    }

    public ArrayList<CharacterSelectionInfo> getAvailableCharacters() { return characterSelector.getAvailable(); }
    public ArrayList<CardSelectionInfo> getAvailableUpgrades() { return upgradeSelector.getAvailable(); }
    public ArrayList<CardSelectionInfo> getAvailableSupport() { return supportSelector.getAvailable(); }
    public ArrayList<CardSelectionInfo> getAvailableEvents() { return eventSelector.getAvailable(); }
    public ArrayList<CardSelectionInfo> getAvailableBattlefields() { return battlefieldSelector.getAvailable(); }

    public int getTotalCharacterPoints() { return characterSelector.getTotalCharacterPoints(); }

    public void addAvailableCardsChangedListener(SelectionListener listener) { availableCardsChanged.add(listener); }
    public void addDeckChangedListener(SelectionListener listener) { deckChangedListener.add(listener); }

    public String getFaction() {
        if (characterSelector.getSelectedCharacters().size() > 0) {
            return characterSelector.getSelectedCharacters().get(0).getCharacterCard().getFaction().toString();
        } else {
            return "-";
        }
    }
    public int getDeckCardCount() { return deckCardCount; }

    public int getDiceCount() {
        int result = 0;

        for (CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getDiceCount();
        }

        return result;
    }

    public double getMeleeAttackRating() {
        double result = 0;

        for(CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getMeleeAttackRating();
        }

        return result;
    }

    public double getRangedAttackRating() {
        double result = 0;

        for(CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getRangedAttackRating();
        }

        return result;
    }

    public double getCostRating() {
        double result = 0;

        for(CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getCostRating();
        }

        return result;
    }

    public double getIncomeRating() {
        double result = 0;

        for(CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getIncomeRating();
        }

        return result;
    }

    public double getDefenceRating() {
        double result = 0;

        for (CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getDefenceRating();
        }

        return result;
    }

    public double getFocusRating() {
        double result = 0;

        for (CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getFocusRating();
        }

        return result;
    }

    public double getDisruptRating() {
        double result = 0;

        for (CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getDisruptRating();
        }

        return result;
    }

    public double getDiscardRating() {
        double result = 0;

        for (CardSelectionInfo c: getAllSelectedCards()) {
            result += c.getDiscardRating();
        }

        return result;
    }
}
