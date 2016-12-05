package com.cardmanager.apps.kling.destinycardmanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cardmanager.apps.kling.destinycardmanager.model.Card;

import java.util.ArrayList;

/**
 * Created by danie on 2016-12-04.
 */

public class CardDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "CardManager";
    private static final int DATABASE_VERSION = 2;
    private static final String CARD_NUMBER = "cardNumber";
    private static final String OWNED_COUNT = "ownedCount";
    private static final String OWNED_CARDS_TABLE_NAME = "cards";
    private static final String DICTIONARY_TABLE_CREATE =
            "CREATE TABLE " + OWNED_CARDS_TABLE_NAME + " (" +
                    CARD_NUMBER + " INTEGER primary key, " +
                    OWNED_COUNT + " INTEGER);";

    public CardDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DICTIONARY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void updateCards(ArrayList<Card> cards) {
        SQLiteDatabase rd = getReadableDatabase();
        String query = "SELECT " + OWNED_COUNT + " FROM " + OWNED_CARDS_TABLE_NAME + " WHERE " + CARD_NUMBER + "=?";

        for (Card card: cards) {
            Cursor cursor = rd.rawQuery(query, new String[] {String.valueOf(card.getCardNumber())});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                int ownedCount = cursor.getInt(cursor.getColumnIndex(OWNED_COUNT));
                card.setOwnedCount(ownedCount);
            }
        }

        rd.close();
    }

    public void updateDatabase(ArrayList<Card> cards) {
        SQLiteDatabase wd = getWritableDatabase();
        ContentValues values = new ContentValues();

        wd.beginTransaction();

        try {
            for (int i = 0; i < cards.size(); i++) {
                values.clear();

                values.put(CARD_NUMBER, cards.get(i).getCardNumber());
                values.put(OWNED_COUNT, cards.get(i).getOwnedCount());
                long result = wd.insertWithOnConflict(OWNED_CARDS_TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
            }
        } finally {
            wd.endTransaction();
        }

        wd.close();
    }
}
