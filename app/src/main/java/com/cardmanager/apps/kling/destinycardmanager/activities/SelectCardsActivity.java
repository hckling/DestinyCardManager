package com.cardmanager.apps.kling.destinycardmanager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.adapters.SelectableCardListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.database.CardDatabase;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSetBuilder;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by danie on 2016-11-27.
 */

public class SelectCardsActivity extends Activity {
    private ListView lv;
    private ListView listview;
    private CardDatabase db;

    private ArrayList<CardSet> cardSets;

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        parseCards();
        readOwnedCardsFromDb();

        setContentView(R.layout.activity_select_cards);
        listview = (ListView) findViewById(R.id.lvAllCards);
        SelectableCardListAdapter adapter = new SelectableCardListAdapter(this, cardSets.get(0).getCards());
        listview.setAdapter(adapter);
    }

    public void onStop() {
        super.onStop();
        db.saveCardCollection(cardSets.get(0).getCards());
    }

    private void readOwnedCardsFromDb() {
        db = new CardDatabase(getApplicationContext());
        db.updateCards(cardSets.get(0).getCards());
    }

    private void parseCards() {
        XmlPullParser parser = getApplicationContext().getResources().getXml(R.xml.cards);
        try {
            cardSets = CardSetBuilder.readCardsXml(parser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
