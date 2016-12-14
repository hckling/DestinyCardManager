package com.cardmanager.apps.kling.destinycardmanager.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.ListFragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cardmanager.apps.kling.destinycardmanager.R;
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
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by danie on 2016-12-14.
 */

public class BuildDeckPagerActivity extends FragmentActivity {
    protected static final int NUMBER_OF_PAGES = 4;
    protected static final int CHARACTER_PAGE = 0;
    protected static final int UPGRADE_PAGE = 1;
    protected static final int SUPPORT_PAGE = 2;
    protected static final int EVENT_PAGE = 3;

    Deck deck = new Deck();
    ArrayList<SelectedCard> allAvailableCards = new ArrayList<>();
    ArrayList<Card> allCards = new ArrayList<>();

    ViewPager pager;
    DeckBuildingPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_deck);

        parseCards();
        readOwnedCardsFromDb();

        deck.setAvailableCards(allAvailableCards);

        adapter = new DeckBuildingPagerAdapter(getSupportFragmentManager(), allAvailableCards);

        pager = (ViewPager) findViewById(R.id.vpMain);
        pager.setAdapter(adapter);
    }

    private void readOwnedCardsFromDb() {
        CardDatabase db = new CardDatabase(getApplicationContext());
        db.updateCards(allCards);
    }

    private void parseCards() {
        XmlPullParser parser = getApplicationContext().getResources().getXml(R.xml.cards);
        try {
            ArrayList<CardSet> cardSets = CardSetBuilder.readCardsXml(parser);

            for (int i = 0; i < cardSets.size(); i++) {
                CardSet set = cardSets.get(i);
                allCards.addAll(set.getCards());
            }

            for (int i = 0; i < allCards.size(); i++) {
                if (allCards.get(i).getType() == CardType.CHARACTER) {
                    allAvailableCards.add(new SelectedCharacter((CharacterCard) allCards.get(i)));
                } else {
                    allAvailableCards.add(new SelectedCard(allCards.get(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static class SelectCardsFragment extends ListFragment {
        int pageNumber;
        ArrayList<SelectedCharacter> characterCards = new ArrayList<>();
        ArrayList<SelectedCard> upgradeCards = new ArrayList<>();
        ArrayList<SelectedCard> supportCards = new ArrayList<>();
        ArrayList<SelectedCard> eventCards = new ArrayList<>();
        private ArrayList<SelectedCard> battlefieldCards = new ArrayList<>();


        public void setCards(ArrayList<SelectedCard> cards) {
            for (SelectedCard card: cards) {
                switch(card.getCard().getType()) {
                    case CHARACTER: characterCards.add((SelectedCharacter) card);
                        break;
                    case EVENT: eventCards.add(card);
                        break;
                    case UPGRADE: upgradeCards.add(card);
                        break;
                    case SUPPORT: supportCards.add(card);
                        break;
                    case BATTLEFIELD: battlefieldCards.add(card);
                }
            }
        }

        static SelectCardsFragment newInstance(int pageNumber) {
            SelectCardsFragment f = new SelectCardsFragment();

            Bundle args = new Bundle();
            args.putInt("pageNumber", pageNumber);
            f.setArguments(args);

            return f;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            pageNumber = getArguments() != null ? getArguments().getInt("pageNumber") : 1;
        }

        /**
         * The Fragment's UI is just a simple text view showing its
         * instance number.
         */
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View view;

            switch(pageNumber) {
                case CHARACTER_PAGE: view = createSelectCharacterView(inflater, container, savedInstanceState);
                    break;
                case UPGRADE_PAGE: view = createSelectUpgradesView(inflater, container, savedInstanceState);
                    break;
                case SUPPORT_PAGE: view = createSelectSupportsView(inflater, container, savedInstanceState);
                    break;
                case EVENT_PAGE: view = createSelectEventsView(inflater, container, savedInstanceState);
                    break;
                default: view = null;
            }

            return view;
        }

        private View createSelectEventsView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.select_deck_cards, container, false);
            TextView tvHeader = (TextView) v.findViewById(R.id.tvCardsTitle);
            tvHeader.setText("Select events");
            return v;
        }

        private View createSelectSupportsView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.select_deck_cards, container, false);
            TextView tvHeader = (TextView) v.findViewById(R.id.tvCardsTitle);
            tvHeader.setText("Select supports");
            return v;
        }

        private View createSelectUpgradesView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.select_deck_cards, container, false);
            TextView tvHeader = (TextView) v.findViewById(R.id.tvCardsTitle);
            tvHeader.setText("Select upgrades");

            return v;
        }

        private View createSelectCharacterView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.select_deck_cards, container, false);
            TextView tvHeader = (TextView) v.findViewById(R.id.tvCardsTitle);
            tvHeader.setText("Select characters");

            return v;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            switch (pageNumber) {
                case CHARACTER_PAGE:
                    setListAdapter(new SelectableCharacterListAdapter(getActivity(),
                            R.layout.select_character_list_item, characterCards));
                    break;
                case UPGRADE_PAGE:
                    setListAdapter(new SelectableDeckCardListAdapter(getActivity(),
                            R.layout.select_card_list_item, upgradeCards));
                    break;
                case SUPPORT_PAGE:
                    setListAdapter(new SelectableDeckCardListAdapter(getActivity(),
                            R.layout.select_card_list_item, supportCards));
                    break;
                case EVENT_PAGE:
                    setListAdapter(new SelectableDeckCardListAdapter(getActivity(),
                            R.layout.select_card_list_item, eventCards));
                    break;
            }

        }

        @Override
        public void onListItemClick(ListView l, View v, int position, long id) {
            Log.i("FragmentList", "Item clicked: " + id);
        }
    }

    public static class DeckBuildingPagerAdapter extends FragmentPagerAdapter {
        ArrayList<SelectedCard> availableCards;

        public DeckBuildingPagerAdapter(FragmentManager fm, ArrayList<SelectedCard> availableCards) {
            super(fm);

            this.availableCards = availableCards;
        }

        @Override
        public Fragment getItem(int position) {
            SelectCardsFragment fragment = SelectCardsFragment.newInstance(position);
            fragment.setCards(availableCards);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }
    }

}
