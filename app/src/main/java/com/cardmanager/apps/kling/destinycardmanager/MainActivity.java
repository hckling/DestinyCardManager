package com.cardmanager.apps.kling.destinycardmanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;

import com.cardmanager.apps.kling.destinycardmanager.activities.BuildDeckPagerActivity;
import com.cardmanager.apps.kling.destinycardmanager.activities.SelectCardsActivity;
import com.cardmanager.apps.kling.destinycardmanager.adapters.DeckListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.database.CardDatabase;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;
import com.cardmanager.apps.kling.destinycardmanager.model.Deck;
import com.cardmanager.apps.kling.destinycardmanager.model.DeckBuilder;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    CardDatabase db;
    ArrayList<CardSet> cardSets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new CardDatabase(getApplicationContext());
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.btnManageCollection);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), SelectCardsActivity.class);
            startActivity(intent);
        });

        Button newDeckButton = (Button) findViewById(R.id.btnNewDeck);
        newDeckButton.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), BuildDeckPagerActivity.class);
            startActivity(intent);
        });

        loadDecks();
    }

    protected void onStart() {
        super.onStart();
        loadDecks();
    }

    private void loadDecks() {
        // load decks
        ListView lvDecks = (ListView) findViewById(R.id.lvDecks);
        ArrayList<Deck> decks = db.getDecks();

        DeckListAdapter adapter = new DeckListAdapter(this.getApplicationContext(), decks);
        lvDecks.setAdapter(adapter);
    }
}
