package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-11-24.
 */

public class Deck {
    private String name;
    private int deckCardCount = 0;
    public static final int MAX_DECK_SIZE = 30;

    private ArrayList<Card> allBattlefields = new ArrayList<>();
    private ArrayList<CharacterCard> allCharacters = new ArrayList<>();
    private ArrayList<Card> allUpgrades = new ArrayList<>();
    private ArrayList<Card> allSupport = new ArrayList<>();
    private ArrayList<Card> allEvents = new ArrayList<>();

    Selector<CharacterSelectionInfo> characterSelector;
    Selector<CardSelectionInfo> upgradeSelector;
    Selector<CardSelectionInfo> supportSelector;
    Selector<CardSelectionInfo> eventSelector;
    Selector<CardSelectionInfo> battlefieldSelector;

    private ArrayList<CardSelectionInfo> selectableBattlefields = new ArrayList<>();
    public ArrayList<SelectionListener> availableCardsChanged = new ArrayList<>();
    private ArrayList<SelectionListener> deckChangedListener = new ArrayList<>();

    public Card getSelectedBattlefield() {
        if (battlefieldSelector.getSelectionCount() > 0) {
            return battlefieldSelector.getSelected().get(0).getCard();
        } else {
            return null;
        }
    }

    public String getName() {return name; }
    public void setName(String name) { this.name = name; }

    private void cardSelectionChanged() {
        boolean wasAtMaxSize = deckCardCount == MAX_DECK_SIZE;

        deckCardCount = 0;

        deckCardCount += upgradeSelector.getSelectionCount();
        deckCardCount += eventSelector.getSelectionCount();
        deckCardCount += supportSelector.getSelectionCount();

        raiseDeckChanged();

        if (deckCardCount == MAX_DECK_SIZE) {
            upgradeSelector.maxCardsReached();
            eventSelector.maxCardsReached();
            supportSelector.maxCardsReached();

            raiseAvailableCardsChanged();
        } else if (wasAtMaxSize) {
            upgradeSelector.maxCardsNotReached(characterSelector.getSelected());
            eventSelector.maxCardsNotReached(characterSelector.getSelected());
            supportSelector.maxCardsNotReached(characterSelector.getSelected());

            raiseAvailableCardsChanged();
        }
    }

    public ArrayList<CardSelectionInfo> getAllSelectedCards() {
        ArrayList<CardSelectionInfo> result = new ArrayList<>();

        result.addAll(characterSelector.getSelected());
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
        upgradeSelector.addSelectionChangedListener(new SelectionListener() {
            @Override
            public void selectionChanged() {
                cardSelectionChanged();
            }
        });

        supportSelector = new CardSelector(allSupport);
        supportSelector.addSelectionChangedListener(new SelectionListener() {
            @Override
            public void selectionChanged() {
                cardSelectionChanged();
            }
        });

        eventSelector = new CardSelector(allEvents);
        eventSelector.addSelectionChangedListener(new SelectionListener() {
            @Override
            public void selectionChanged() {
                cardSelectionChanged();
            }
        });

        battlefieldSelector = new BattlefieldSelector(allBattlefields);
        battlefieldSelector.addSelectionChangedListener(new SelectionListener() {
            @Override
            public void selectionChanged() {
                battlefieldSelectionChanged();
            }
        });
    }

    private void battlefieldSelectionChanged() {
        raiseAvailableCardsChanged();
        raiseDeckChanged();
    }

    public void characterSelectionChanged() {
        characterSelector.filterByCharacterSelection(null);
        upgradeSelector.filterByCharacterSelection(characterSelector.getSelected());
        supportSelector.filterByCharacterSelection(characterSelector.getSelected());
        eventSelector.filterByCharacterSelection(characterSelector.getSelected());

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

    public int getTotalCharacterPoints() {
        int result = 0;

        for(CharacterSelectionInfo c: characterSelector.getSelected()) {
            result += c.getPoints();
        }

        return result;
    }

    public void addAvailableCardsChangedListener(SelectionListener listener) { availableCardsChanged.add(listener); }
    public void addDeckChangedListener(SelectionListener listener) { deckChangedListener.add(listener); }

    public String getFaction() {
        if (characterSelector.getSelected().size() > 0) {
            return characterSelector.getSelected().get(0).getCharacterCard().getFaction().toString();
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

    public boolean isValid() {
        return (getSelectedBattlefield() != null) && (characterSelector.getSelectionCount() > 0);
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
