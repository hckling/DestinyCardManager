package com.cardmanager.apps.kling.destinycardmanager.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.adapters.SelectableCardListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.database.CardDatabase;
import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSetBuilder;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by danie on 2016-11-27.
 */

public class SelectCardsActivity extends AppCompatActivity {
    private ListView lv;
    private ListView listview;
    private CardDatabase db;

    private ArrayList<CardSet> cardSets;
    SelectableCardListAdapter listAdapter;

    @Override
    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);

        setTitle("Manage collection");

        parseCards();
        readOwnedCardsFromDb();

        setContentView(R.layout.activity_select_cards);
        listview = (ListView) findViewById(R.id.lvAllCards);
        listAdapter = new SelectableCardListAdapter(this, cardSets.get(0).getCards());
        listview.setAdapter(listAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.collection_menu, menu);

        MenuItem mi = menu.findItem(R.id.mnuAddFullCollection);
        mi.setOnMenuItemClickListener(v -> {
            // Show warning dialog
            warnUserCollectionWillBeReset();

            return true;
        });

        return true;
    }

    private void warnUserCollectionWillBeReset() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setCancelable(true);
        builder.setTitle("Confirm collection change");
        builder.setMessage("Are you sure? All existing changes to the collection will be changed!");
        builder.setPositiveButton(android.R.string.yes, (dialog, which) -> setCollectionToFullSet());
        builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void setCollectionToFullSet() {
        for(CardSet cs : cardSets) {
            for (Card c : cs.getCards()) {
                c.setOwnedCount(2);
            }
        }

        listAdapter.notifyDataSetChanged();
    }

    @Override
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
