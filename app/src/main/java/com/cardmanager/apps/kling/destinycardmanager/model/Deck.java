package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by dankli on 2017-03-08.
 */

public class Deck {
    String name = "";
    long id = -1;
    int totalPoints = 0;

    private ArrayList<CharacterSelectionInfo> selectedCharacters = new ArrayList<>();
    private CardSelectionInfo selectedBattlefield = null;
    private ArrayList<CardSelectionInfo> selectedEvents = new ArrayList<>();
    private ArrayList<CardSelectionInfo> selectedUpgrades = new ArrayList<>();
    private ArrayList<CardSelectionInfo> selectedSupport = new ArrayList<>();

    public Deck() { }

    public Deck(String name, long id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addCharacter(CharacterSelectionInfo character) {
        selectedCharacters.add(character);
        updateTotalPoints();
    }

    private void updateTotalPoints() {
        totalPoints = 0;

        for (CharacterSelectionInfo c : selectedCharacters) {
            totalPoints += c.getPoints();
        }
    }

    public int getTotalPoints() { return totalPoints; }

    public ArrayList<CharacterSelectionInfo> getSelectedCharacters() {
        return selectedCharacters;
    }

    public void selectBattlefield(CardSelectionInfo battlefield) {
        selectedBattlefield = battlefield;
    }

    public CardSelectionInfo getSelectedBattlefield() {
        return selectedBattlefield;
    }

    public void addCard(CardSelectionInfo card) {
        switch(card.getCard().getType()) {
            case EVENT: addEvent(card);
                break;
            case UPGRADE: addUpgrade(card);
                break;
            case SUPPORT: addSupport(card);
                break;
            case CHARACTER:
                if (CharacterSelectionInfo.class.isInstance(card)) {
                    addCharacter((CharacterSelectionInfo) card);
                }
                break;
            case BATTLEFIELD: selectBattlefield(card);
                break;
        }
    }


    public void addEvent(CardSelectionInfo event) {
        selectedEvents.add(event);
    }

    public ArrayList<CardSelectionInfo> getSelectedEvents() {
        return selectedEvents;
    }

    public void addUpgrade(CardSelectionInfo upgrade) {
        selectedUpgrades.add(upgrade);
    }

    public ArrayList<CardSelectionInfo> getSelectedUpgrades() {
        return selectedUpgrades;
    }

    public void addSupport(CardSelectionInfo support) {
        selectedSupport.add(support);
    }

    public ArrayList<CardSelectionInfo> getSelectedSupport() {
        return selectedSupport;
    }

    public int getTotalNumberOfSelectedCards() {
        return getNumberOfSelectedCards(selectedEvents) + getNumberOfSelectedCards(selectedUpgrades) + getNumberOfSelectedCards(selectedSupport);
    }

    private int getNumberOfSelectedCards(ArrayList<CardSelectionInfo> selectionInfoList) {
        int result = 0;

        for (CardSelectionInfo c : selectionInfoList) {
            result += c.getCount();
        }

        return result;
    }
}
