package com.cardmanager.apps.kling.destinycardmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.cardmanager.apps.kling.destinycardmanager.activities.BuildDeckPagerActivity;
import com.cardmanager.apps.kling.destinycardmanager.activities.SelectCardsActivity;
import com.cardmanager.apps.kling.destinycardmanager.adapters.DeckListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.database.CardDatabase;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSetBuilder;
import com.cardmanager.apps.kling.destinycardmanager.model.Deck;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CardDatabase db;
    ArrayList<CardSet> cardSets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Saved decks");

        db = new CardDatabase(getApplicationContext());
        setContentView(R.layout.activity_main);

        parseCards();
        loadDecks();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadDecks();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem mi = menu.findItem(R.id.mnuManageCollection);
        mi.setOnMenuItemClickListener(v -> {
            Intent intent = new Intent(this, SelectCardsActivity.class);
            startActivity(intent);
            return true;
        });

        mi = menu.findItem(R.id.mnuAddDeck);
        mi.setOnMenuItemClickListener(v -> {
            Intent intent = new Intent(this, BuildDeckPagerActivity.class);
            startActivity(intent);
            return true;
        });


        return true;
    }

    private void loadDecks() {
        // load decks
        ListView lvDecks = (ListView) findViewById(R.id.lvDecks);
        ArrayList<Deck> decks = db.getDecks();

        DeckListAdapter adapter = new DeckListAdapter(this, decks);
        adapter.addDeckDeletedListener(v -> {
            loadDecks();
        });

        lvDecks.setOnItemClickListener((parent, view, position, id) -> {
            Deck d = decks.get(position);

            Intent intent = new Intent(this, BuildDeckPagerActivity.class);
            intent.putExtra("deckId", d.getId());
            startActivity(intent);
        });

        lvDecks.setAdapter(adapter);
    }

    private void parseCards() {
        XmlPullParser parser = getResources().getXml(R.xml.cards);
        try {
            CardSetBuilder.readCardsXml(parser);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
