package com.cardmanager.apps.kling.destinycardmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.Deck;

import java.util.ArrayList;

/**
 * Created by danie on 2016-12-04.
 */

public class CardDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CardManager";
    private static final int DATABASE_VERSION = 2;
    private final String CARD_NUMBER = "cardNumber";
    private final String OWNED_COUNT = "ownedCount";
    private final String OWNED_CARDS_TABLE_NAME = "cards";
    private final String OWNED_CARDS_TABLE_CREATE =
            "CREATE TABLE " + OWNED_CARDS_TABLE_NAME + " (" +
                    CARD_NUMBER + " INTEGER primary key, " +
                    OWNED_COUNT + " INTEGER);";

    private final static String DECK_TABLE_NAME = "decks";
    private final String ID = "id";
    private final String DECK_NAME = "name";
    private final String BATTLEFIELD_ID = "battlefield";

    private final static String DECK_CHARACTERS_TABLE_NAME = "deck_characters";
    private final String DECK_ID = "deckId";
    private final String IS_ELITE = "isElite";

    private final String DECK_CARDS = "deck_cards";

    private final String DECK_TABLE_CREATE =
            "CREATE TABLE " + DECK_TABLE_NAME + " (" +
                    ID + " INT PRIMARY KEY AUTOINCREMENT, " +
                    DECK_NAME + " VARCHAR(255) NOT NULL, " +
                    "FOREIGN KEY(" + BATTLEFIELD_ID + ") REFERENCES " + OWNED_CARDS_TABLE_NAME + "(" + CARD_NUMBER + ") INT NOT NULL, ";

    private final String DECK_CHARACTERS_TABLE_CREATE =
            "CREATE TABLE " + DECK_CHARACTERS_TABLE_NAME + " (" +
                    "FOREIGN KEY(" + DECK_ID + ") REFERENCES " + DECK_TABLE_NAME + "(" + DECK_ID + ") INT PRIMARY KEY NOT NULL, " +
                    "FOREIGN KEY(" + CARD_NUMBER + ") REFERENCES " + OWNED_CARDS_TABLE_NAME + "(" + CARD_NUMBER + ") INT NOT NULL, " +
                    IS_ELITE + " BOOLEAN NOT NULL);";

    private final String DECK_CARDS_TABLE_CREATE =
            "CREATE TABLE " + DECK_CARDS + " (" +
                    "FOREIGN KEY(" + DECK_ID + ") REFERENCES " + DECK_TABLE_NAME + "(" + DECK_ID + ") INT PRIMARY KEY NOT NULL, " +
                    "FOREIGN KEY(" + CARD_NUMBER + ") REFERENCES " + OWNED_CARDS_TABLE_NAME + "(" + CARD_NUMBER + ") INT NOT NULL);";

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

    public void updateDatabase(ArrayList<Card> cards) {
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

    public void saveDeck(Deck deck) {
        SQLiteDatabase wd = getWritableDatabase();
        ContentValues values = new ContentValues();

        /*values.put();
        values.put(DECK_NAME, deck.getName());
        values.put(BATTLEFIELD_ID, deck.getSelectedBattleField());*/
    }
}
