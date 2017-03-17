package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by danie on 2016-12-19.
 */

public class BattlefieldSelector implements Selector<CardSelectionInfo> {
    CardSelectionInfo selectedBattlefield;
    ArrayList<CardSelectionInfo> availableBattlefields = new ArrayList<>();
    ArrayList<CardSelectionInfo> unavailableBattlefields = new ArrayList<>();
    ArrayList<SelectionListener> listeners = new ArrayList<>();

    public BattlefieldSelector(ArrayList<Card> allBattlefields) {
        for (Card c: allBattlefields) {
            if (c.getOwnedCount() > 0) {
                availableBattlefields.add(new CardSelectionInfo(c));
            }
        }

        for (CardSelectionInfo c: availableBattlefields) {
            c.addSelectionListener(new SelectionListener() {
                @Override
                public void selectionChanged() {
                    battlefieldSelected();
                }
            });
        }
    }

    private void battlefieldSelected() {
        selectedBattlefield = null;

        for(CardSelectionInfo c: availableBattlefields) {
            if (c.getCount() > 0) {
                selectedBattlefield = c;
                break;
            }
        }

        if (selectedBattlefield != null) {
            int i = 0;
            while (i < availableBattlefields.size()) {
                if (availableBattlefields.get(i) != selectedBattlefield) {
                    unavailableBattlefields.add(availableBattlefields.get(i));
                    availableBattlefields.remove(i);
                } else {
                    i++;
                }
            }
        } else {
            availableBattlefields.addAll(unavailableBattlefields);
            unavailableBattlefields.clear();
        }

        availableBattlefields.sort(new Comparator<CardSelectionInfo>() {
            @Override
            public int compare(CardSelectionInfo o1, CardSelectionInfo o2) {
                return o1.getCard().getCardNumber() - o2.getCard().getCardNumber();
            }
        });

        raiseSelectionChanged();
    }

    private void raiseSelectionChanged() {
        for (SelectionListener l: listeners) {
            l.selectionChanged();
        }
    }

    @Override
    public void filterByCharacterSelection(ArrayList<CharacterSelectionInfo> selectedCharacters) {
        // Not relevant for Battlefields
    }

    @Override
    public ArrayList<CardSelectionInfo> getAvailable() {
        return availableBattlefields;
    }

    @Override
    public ArrayList<CardSelectionInfo> getSelected() {
        ArrayList<CardSelectionInfo> result = new ArrayList<CardSelectionInfo>();
        result.add(selectedBattlefield);

        return result;
    }

    @Override
    public void addSelectionChangedListener(SelectionListener listener) {
        listeners.add(listener);
    }

    @Override
    public int getSelectionCount() {
        if (selectedBattlefield != null) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void maxCardsReached() {
        // Not relevant for battlefields
    }

    @Override
    public void maxCardsNotReached(ArrayList<CharacterSelectionInfo> selectedCharacters) {
        // Not relevant for battlefields
    }

    @Override
    public void select(CardSelectionInfo item) {
        CardSelectionInfo csi = findCard(item.getCard().getCardNumber());

        if (csi != null)
            csi.select();
    }

    private CardSelectionInfo findCard(int cardNumber) {
        for (int i = 0; i < availableBattlefields.size(); i++) {
            if (availableBattlefields.get(i).getCard().getCardNumber() == cardNumber) {
                return availableBattlefields.get(i);
            }
        }

        return null;
    }
}
