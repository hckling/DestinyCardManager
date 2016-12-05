package com.cardmanager.apps.kling.destinycardmanager.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.adapters.SelectableCardListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.database.CardDatabase;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSetBuilder;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.lang.reflect.Array;
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

        db = new CardDatabase(getApplicationContext());

        XmlPullParser parser = getApplicationContext().getResources().getXml(R.xml.cards);
        try {
            cardSets = CardSetBuilder.readCardsXml(parser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        db.updateCards(cardSets.get(0).getCards());

        setContentView(R.layout.activity_select_cards);
        listview = (ListView) findViewById(R.id.lvAllCards);
        SelectableCardListAdapter adapter = new SelectableCardListAdapter(this, cardSets.get(0).getCards());
        listview.setAdapter(adapter);

        Button btn = (Button) findViewById(R.id.btnSave);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateDatabase(cardSets.get(0).getCards());
            }
        });
    }
}
