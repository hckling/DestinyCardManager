package com.cardmanager.apps.kling.destinycardmanager.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.adapters.SelectableCardListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.adapters.SelectableCharacterListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.adapters.SelectableDeckCardListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.database.CardDatabase;
import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSetBuilder;
import com.cardmanager.apps.kling.destinycardmanager.model.CardType;
import com.cardmanager.apps.kling.destinycardmanager.model.CharacterCard;
import com.cardmanager.apps.kling.destinycardmanager.model.Deck;
import com.cardmanager.apps.kling.destinycardmanager.model.SelectedCard;
import com.cardmanager.apps.kling.destinycardmanager.model.SelectedCharacter;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by danie on 2016-12-11.
 */

public class BuildDeckActivity extends Activity {
    private Deck deck = new Deck();
    private ArrayList<CardSet> cardSets = new ArrayList<>();
    private ArrayList<Card> allCards = new ArrayList<>();
    private CardDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_deck);

        parseCards();
        readOwnedCardsFromDb();

        buildHeroSelection();
        buildUpgradeSelection();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //readOwnedCardsFromDb();
        //buildHeroSelection();
        //buildUpgradeSelection();
    }

    private void readOwnedCardsFromDb() {
        db = new CardDatabase(getApplicationContext());
        db.updateCards(cardSets.get(0).getCards());
    }

    private void parseCards() {
        XmlPullParser parser = getApplicationContext().getResources().getXml(R.xml.cards);
        try {
            cardSets = CardSetBuilder.readCardsXml(parser);

            for (int i = 0; i < cardSets.size(); i++) {
                allCards.addAll(cardSets.get(i).getCards());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void buildHeroSelection() {
        ListView lvSelectedCharacters = (ListView) findViewById(R.id.lvSelectedCharacters);

        ArrayList<CharacterCard> availableCharacters = new ArrayList<>();
        for (int i = 0; i < allCards.size(); i++) {
            if ((allCards.get(i).getType() == CardType.CHARACTER) && (allCards.get(i).getOwnedCount() > 0)) {
                availableCharacters.add((CharacterCard) allCards.get(i));
            }
        }

        ArrayList<SelectedCharacter> selectedCharacters = new ArrayList<>();
        for (int i = 0; i < availableCharacters.size(); i++) {
            selectedCharacters.add(new SelectedCharacter(availableCharacters.get(i)));
        }

        SelectableCharacterListAdapter adapter = new SelectableCharacterListAdapter(this, 0, selectedCharacters);
        lvSelectedCharacters.setAdapter(adapter);
    }

    private void buildUpgradeSelection() {
        ListView lvSelectedUpgrades = (ListView) findViewById(R.id.lvSelectedUpgrades);

        ArrayList<Card> availableUpgrades = new ArrayList<>();
        for (int i = 0; i < allCards.size(); i++) {
            if ((allCards.get(i).getType() == CardType.UPGRADE) && (allCards.get(i).getOwnedCount() > 0)) {
                availableUpgrades.add(allCards.get(i));
            }
        }

        ArrayList<SelectedCard> selectedUpgrades = new ArrayList<>();
        for (int i = 0; i < availableUpgrades.size(); i++) {
            selectedUpgrades.add(new SelectedCard(availableUpgrades.get(i)));
        }

        SelectableDeckCardListAdapter adapter = new SelectableDeckCardListAdapter(this, 0, selectedUpgrades);
        lvSelectedUpgrades.setAdapter(adapter);
    }

    public Deck getDeck() { return deck; }
}
