package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by danie on 2016-12-16.
 */

public class CardSelector {
    protected ArrayList<CardSelectionInfo> availableCards = new ArrayList<>();
    protected ArrayList<CardSelectionInfo> selectedCards = new ArrayList<>();
    protected ArrayList<CardSelectionInfo> unavailableCards = new ArrayList<>();

    private ArrayList<SelectionListener> selectionListeners = new ArrayList<>();

    public CardSelector(ArrayList<Card> allCards) {
        for (Card card: allCards) {
            if (card.getOwnedCount() > 0) {
                availableCards.add(new CardSelectionInfo(card));
            }
        }

        for (CardSelectionInfo c: availableCards) {
            c.addSelectionListener(new SelectionListener() {
                @Override
                public void selectionChanged() {
                    cardSelectionChanged();
                }
            });
        }
    }

    public void filterByCharacterSelection(ArrayList<CharacterSelectionInfo> selectedCharacters) {
        availableCards.addAll(unavailableCards);
        unavailableCards.clear();

        int i = 0;

        // Remove all unavailable cards
        while(i < availableCards.size()) {
            boolean isSelectable = checkIfCardIsSelectable(availableCards.get(i), selectedCharacters);

            if (!isSelectable) {
                while(availableCards.get(i).getCount() > 0)
                    availableCards.get(i).deselect();

                unavailableCards.add(availableCards.get(i));
                availableCards.remove(i);
            } else {
                i++;
            }
        }

        sortList(availableCards);
        sortList(selectedCards);
    }

    private void updateSelectedCards() {
        selectedCards.clear();
        for(CardSelectionInfo c: availableCards) {
            if (c.getCount() > 0) {
                selectedCards.add(c);
            }
        }
    }

    private boolean checkIfCardIsSelectable(CardSelectionInfo cardSelectionInfo, ArrayList<CharacterSelectionInfo> selectedCharacters) {
        boolean result = selectedCharacters.size() == 0;

        for(CharacterSelectionInfo character: selectedCharacters) {
            if(character.getCharacterCard().isCompatible(cardSelectionInfo.getCard())) {
                result = true;
            }
        }

        return result;
    }

    private void sortList(ArrayList<CardSelectionInfo> list) {
        list.sort(new Comparator<CardSelectionInfo>() {
            @Override
            public int compare(CardSelectionInfo o1, CardSelectionInfo o2) {
                return o1.getCard().getCardNumber() - o2.getCard().getCardNumber();
            }
        });
    }

    private void cardSelectionChanged() {
        updateSelectedCards();

        for(SelectionListener l: selectionListeners) {
            l.selectionChanged();
        }
    }

    public void addSelectionListener(SelectionListener listener) { selectionListeners.add(listener); };

    public ArrayList<CardSelectionInfo> getSelected() { return selectedCards; }
    public ArrayList<CardSelectionInfo> getAvailable() { return availableCards; }

    public int getSelectedCardCount () {
        int result = 0;

        for (CardSelectionInfo c: selectedCards) {
            result += c.getCount();
        }

        return result;
    }

    public void preventNewSelection() {
        // Remove all cards from availableCards which are not in selectedCards
        int i = 0;
        while(i < availableCards.size()) {
            if (!selectedCards.contains(availableCards.get(i))) {
                unavailableCards.add(availableCards.get(i));
                availableCards.remove(i);
            } else {
                i++;
            }
        }

        for(CardSelectionInfo c: selectedCards) {
            c.makeUnavailableForSelection();
        }
    }

    public void allowNewSelection(ArrayList<CharacterSelectionInfo> selectedCharacters) {
        for(CardSelectionInfo c: selectedCards) {
            c.makeAvailableForSelection();
        }

        for(CardSelectionInfo c: unavailableCards) {
            c.makeAvailableForSelection();
        }

        filterByCharacterSelection(selectedCharacters);
    }
}