package com.cardmanager.apps.kling.destinycardmanager.database;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by danie on 2016-12-19.
 */

public class SavedDeck {
    private int id;
    private int battlefield;
    private String name;

    public class SavedCard {
        private int cardNumber;
        private int count;

        public SavedCard(int cardNumber, int count) {
            this.cardNumber = cardNumber;
            this.count = count;
        }

        public int getCardNumber() { return cardNumber; }
        public int getCount() { return count; }
    }

    public class SavedCharacter {
        private int cardNumber;
        private boolean isElite;

        public SavedCharacter(int cardNumber, boolean isElite) {
            this.cardNumber = cardNumber;
            this.isElite = isElite;
        }

        public int getCardNumber() { return cardNumber; }
        public boolean getIsElite() { return isElite; }
    }

    private ArrayList<SavedCard> cards = new ArrayList<>();
    private ArrayList<SavedCharacter> characters = new ArrayList<>();

    public SavedDeck(int id, String name, int battlefield) {
        this.id = id;
        this.name = name;
        this.battlefield = battlefield;
    }

    public ArrayList<SavedCard> getCards() { return cards; }
    public ArrayList<SavedCharacter> getCharacters() { return characters; }

    public void addSavedCharacter(int cardNumber, boolean isElite) {
        characters.add(new SavedCharacter(cardNumber, isElite));
    }

    public void addSavedCard(int cardNumber, int count) {
        cards.add(new SavedCard(cardNumber, count));
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getBattlefield() { return battlefield; }
}
