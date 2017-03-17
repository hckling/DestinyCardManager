package com.cardmanager.apps.kling.destinycardmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSelectionInfo;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSetBuilder;
import com.cardmanager.apps.kling.destinycardmanager.model.CharacterCard;
import com.cardmanager.apps.kling.destinycardmanager.model.CharacterSelectionInfo;
import com.cardmanager.apps.kling.destinycardmanager.model.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danie on 2016-12-04.
 */

public class CardDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CardManager";
    private static final int DATABASE_VERSION = 5;
    private final String CARD_NUMBER = "cardNumber";
    private final String OWNED_COUNT = "ownedCount";
    private final String OWNED_CARDS_TABLE_NAME = "cards";
    private final String OWNED_CARDS_TABLE_CREATE =
            "CREATE TABLE " + OWNED_CARDS_TABLE_NAME + " (" +
                    CARD_NUMBER + " INTEGER PRIMARY KEY ASC, " +
                    OWNED_COUNT + " INT NOT NULL);";

    private final static String DECK_TABLE_NAME = "decks";
    private final String ID = "id";
    private final String DECK_NAME = "name";
    private final String BATTLEFIELD_ID = "battlefield";

    private final static String DECK_CHARACTERS_TABLE_NAME = "deck_characters";
    private final String DECK_ID = "deckId";
    private final String IS_ELITE = "isElite";

    private final static String DECK_CARDS_TABLE_NAME = "deck_cards";
    private final static String CARD_COUNT = "count";

    private final String DECK_TABLE_CREATE =
            "CREATE TABLE " + DECK_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY ASC, " +
                    DECK_NAME + " VARCHAR(255) NOT NULL, " +
                    BATTLEFIELD_ID + " INT NOT NULL, " +
                    "FOREIGN KEY(" + BATTLEFIELD_ID + ") REFERENCES " + OWNED_CARDS_TABLE_NAME + "(" + CARD_NUMBER + "));";

    private final String DECK_CHARACTERS_TABLE_CREATE =
            "CREATE TABLE " + DECK_CHARACTERS_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY ASC, " +
                    DECK_ID + " INT NOT NULL, " +
                    CARD_NUMBER + " INT NOT NULL, " +
                    IS_ELITE + " BOOLEAN NOT NULL, " +
                    CARD_COUNT + " INT NOT NULL DEFAULT 1, " +
                    "FOREIGN KEY(" + DECK_ID + ") REFERENCES " + DECK_TABLE_NAME + "(" + DECK_ID + "), " +
                    "FOREIGN KEY(" + CARD_NUMBER + ") REFERENCES " + OWNED_CARDS_TABLE_NAME + "(" + CARD_NUMBER + "));";

    private final String DECK_CARDS_TABLE_CREATE =
            "CREATE TABLE " + DECK_CARDS_TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY ASC, " +
                    DECK_ID + " INT NOT NULL, " +
                    CARD_NUMBER + " INT NOT NULL, " +
                    CARD_COUNT + " INT NOT NULL, " +
                    "FOREIGN KEY(" + DECK_ID + ") REFERENCES " + DECK_TABLE_NAME + "(" + DECK_ID + "), " +
                    "FOREIGN KEY(" + CARD_NUMBER + ") REFERENCES " + OWNED_CARDS_TABLE_NAME + "(" + CARD_NUMBER + "));";

    public CardDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(OWNED_CARDS_TABLE_CREATE);
        db.execSQL(DECK_TABLE_CREATE);
        db.execSQL(DECK_CHARACTERS_TABLE_CREATE);
        db.execSQL(DECK_CARDS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            db.execSQL("ALTER TABLE " + DECK_CARDS_TABLE_NAME + " ADD COLUMN " + CARD_COUNT + " INT NOT NULL DEFAULT 0");
        }
    }

    public void updateCards(ArrayList<Card> cards) {
        SQLiteDatabase rd = getReadableDatabase();
        String query = "SELECT " + CARD_NUMBER + ", " + OWNED_COUNT + " FROM " + OWNED_CARDS_TABLE_NAME + " WHERE " + CARD_NUMBER + "=?";

        try {
            for (Card card : cards) {
                Cursor cursor = rd.rawQuery(query, new String[]{String.valueOf(card.getCardNumber())});

                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    int cardNumber = cursor.getInt(cursor.getColumnIndex(CARD_NUMBER));
                    int ownedCount = cursor.getInt(cursor.getColumnIndex(OWNED_COUNT));
                    card.setOwnedCount(ownedCount);
                }
            }
        } finally {
            rd.close();
        }
    }

    public void saveCardCollection(ArrayList<Card> cards) {
        SQLiteDatabase wd = getWritableDatabase();
        ContentValues values = new ContentValues();

        //wd.beginTransaction();

        try {
            for (int i = 0; i < cards.size(); i++) {
                values.clear();

                values.put(CARD_NUMBER, cards.get(i).getCardNumber());
                values.put(OWNED_COUNT, cards.get(i).getOwnedCount());
                long result = wd.insertWithOnConflict(OWNED_CARDS_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                if (result == -1)
                {
                    result = 0;
                }
            }
        } finally {
            //wd.endTransaction();
            wd.close();
        }
    }

    public void deleteDeck(Deck deck) {
        SQLiteDatabase wd = getWritableDatabase();
        wd.beginTransaction();

        try {
            wd.delete(DECK_CHARACTERS_TABLE_NAME, DECK_ID + "=" + deck.getId(), null);
            wd.delete(DECK_CARDS_TABLE_NAME, DECK_ID + "=" + deck.getId(), null);
            wd.delete(DECK_TABLE_NAME, ID + "=" + deck.getId(), null);

            wd.setTransactionSuccessful();
        } catch (Exception e) {
            // error
        } finally {
            wd.endTransaction();
            wd.close();
        }
    }

    public void saveDeck(Deck deck) {
        SQLiteDatabase wd = getWritableDatabase();
        ContentValues values = new ContentValues();

        wd.beginTransaction();
        try {
            values.put(DECK_NAME, deck.getName());
            values.put(BATTLEFIELD_ID, deck.getSelectedBattlefield().getCard().getCardNumber());

            long result = wd.insertWithOnConflict(DECK_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            deck.setId(result);

            saveCharacterSelection(deck, wd);
            saveCardSelection(deck, wd);

            wd.setTransactionSuccessful();
        } catch (Exception e) {
            // error
        } finally {
            wd.endTransaction();
            wd.close();
        }
    }

    private void saveCharacterSelection(Deck deck, SQLiteDatabase wd) {
        for (int i = 0; i < deck.getSelectedCharacters().size(); i++) {
            ContentValues values = new ContentValues();

            values.put(DECK_ID, deck.getId());
            values.put(CARD_NUMBER, deck.getSelectedCharacters().get(i).getCard().getCardNumber());
            values.put(IS_ELITE, deck.getSelectedCharacters().get(i).isElite());
            values.put(CARD_COUNT, deck.getSelectedCharacters().get(i).getCount());

            long result = wd.insertWithOnConflict(DECK_CHARACTERS_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    private void saveCardSelection(Deck deck, SQLiteDatabase wd) {
        ArrayList<CardSelectionInfo> allCards = new ArrayList<>();

        allCards.add(deck.getSelectedBattlefield());
        allCards.addAll(deck.getSelectedEvents());
        allCards.addAll(deck.getSelectedSupport());
        allCards.addAll(deck.getSelectedUpgrades());

        for (int i = 0; i < allCards.size(); i++) {
            ContentValues values = new ContentValues();

            values.put(DECK_ID, deck.getId());
            values.put(CARD_NUMBER, allCards.get(i).getCard().getCardNumber());
            values.put(CARD_COUNT, allCards.get(i).getCount());

            long result = wd.insertWithOnConflict(DECK_CARDS_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public ArrayList<Deck> getDecks() {
        SQLiteDatabase rd = getReadableDatabase();
        String query = "SELECT " + ID + ", " + DECK_NAME + " FROM " + DECK_TABLE_NAME + ";";

        ArrayList<Deck> decks = new ArrayList<>();

        try {
            Cursor cursor = rd.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                while(!cursor.isAfterLast()) {
                    String deckName = cursor.getString(cursor.getColumnIndex(DECK_NAME));
                    long id = cursor.getLong(cursor.getColumnIndex(ID));

                    Deck d = new Deck(deckName, id);

                    getCharacters(d, rd);
                    getCards(d, rd);

                    decks.add(d);

                    cursor.moveToNext();
                }
            }
        } finally {
            rd.close();
        }

        return decks;
    }

    private void getCharacters(Deck d, SQLiteDatabase rd) {
        String query = "SELECT " + CARD_NUMBER + ", " + CARD_COUNT + ", " + IS_ELITE + " FROM " + DECK_CHARACTERS_TABLE_NAME + " WHERE " + DECK_ID + "=" + d.getId();

        Cursor c = rd.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()) {
            int cardNumber = c.getInt(c.getColumnIndex(CARD_NUMBER));
            boolean isElite = c.getShort(c.getColumnIndex(IS_ELITE)) > 0;
            int count = c.getInt(c.getColumnIndex(CARD_COUNT));

            CharacterCard card = (CharacterCard) CardSetBuilder.getCard(cardNumber);

            CharacterSelectionInfo selectionInfo = new CharacterSelectionInfo(card);

            for (int i = 0; i < count; i++)
                selectionInfo.select();

            if (isElite)
                selectionInfo.makeElite();

            d.addCharacter(selectionInfo);

            c.moveToNext();
        }
    }

    private void getCards(Deck d, SQLiteDatabase rd) {
        String query = "SELECT " + CARD_NUMBER + ", " + CARD_COUNT + " FROM " + DECK_CARDS_TABLE_NAME + " WHERE " + DECK_ID + "=" + d.getId();

        Cursor c = rd.rawQuery(query, null);

        c.moveToFirst();

        while(!c.isAfterLast()) {
            int cardNumber = c.getInt(c.getColumnIndex(CARD_NUMBER));
            int count = c.getShort(c.getColumnIndex(CARD_COUNT));

            Card card = CardSetBuilder.getCard(cardNumber);
            CardSelectionInfo cardSelectionInfo = new CardSelectionInfo(card);

            for(int i = 0; i < count; i++) {
                cardSelectionInfo.select();
            }

            d.addCard(cardSelectionInfo);

            c.moveToNext();
        }
    }

    public Deck getDeck(long deckId) {
        SQLiteDatabase rd = getReadableDatabase();
        String query = "SELECT " + ID + ", " + DECK_NAME + " FROM " + DECK_TABLE_NAME + " WHERE ID=" + deckId;

        try {
            Cursor c = rd.rawQuery(query, null);

            if (c.getCount() != 1) {
                // Something is wrong...
                return null;
            } else {
                c.moveToFirst();

                String deckName = c.getString(c.getColumnIndex(DECK_NAME));
                long id = c.getLong(c.getColumnIndex(ID));

                Deck d = new Deck(deckName, id);

                getCharacters(d, rd);
                getCards(d, rd);

                return d;
            }
        } finally {
            rd.close();
        }
    }

    public void updateDeck(Deck deck) {
        SQLiteDatabase wd = getWritableDatabase();

        wd.beginTransaction();

        try {
            // Delete all existing cards and characters from the deck
            emptyDeck(deck, wd);
            // Save the current deck
            saveCurrentCards(deck, wd);
            wd.setTransactionSuccessful();
        } catch(Exception e) {
            // Something went wrong
        } finally {
            wd.endTransaction();
            wd.close();
        }
    }

    private void saveCurrentCards(Deck deck, SQLiteDatabase wd) {
        saveCharacterSelection(deck, wd);
        saveCardSelection(deck, wd);
    }

    private void emptyDeck(Deck deck, SQLiteDatabase wd) {
        long result = wd.delete(DECK_CHARACTERS_TABLE_NAME, DECK_ID + "=" + deck.getId(), null);
        result = wd.delete(DECK_CARDS_TABLE_NAME, DECK_ID + "=" + deck.getId(), null);
    }
}
