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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.adapters.SelectableCharacterListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.adapters.SelectableDeckCardListAdapter;
import com.cardmanager.apps.kling.destinycardmanager.database.CardDatabase;
import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSetBuilder;
import com.cardmanager.apps.kling.destinycardmanager.model.Deck;
import com.cardmanager.apps.kling.destinycardmanager.model.SelectionListener;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
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
    ArrayList<Card> allCards = new ArrayList<>();

    ViewPager pager;
    DeckBuildingPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_build_deck);

        parseCards();
        readOwnedCardsFromDb();

        deck.setAvailableCards(allCards);

        adapter = new DeckBuildingPagerAdapter(getSupportFragmentManager(), deck);

        pager = (ViewPager) findViewById(R.id.vpMain);
        pager.setAdapter(adapter);
    }

    private void readOwnedCardsFromDb() {
        CardDatabase db = new CardDatabase(getApplicationContext());
        db.updateCards(allCards);

        // Remove all cards we do not yet own
        int i = 0;
        while(i < allCards.size()) {
            if (allCards.get(i).getOwnedCount() == 0) {
                allCards.remove(i);
            } else {
                i++;
            }
        }
    }

    private void parseCards() {
        XmlPullParser parser = getApplicationContext().getResources().getXml(R.xml.cards);
        try {
            ArrayList<CardSet> cardSets = CardSetBuilder.readCardsXml(parser);

            for (int i = 0; i < cardSets.size(); i++) {
                CardSet set = cardSets.get(i);
                allCards.addAll(set.getCards());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static class SelectCardsFragment extends ListFragment {
        int pageNumber;
        Deck deck;

        public void setDeck(Deck deck) {
            this.deck = deck;

            deck.addAvailableCardsChangedListener(new SelectionListener() {
                @Override
                public void selectionStateChanged() {
                    ListAdapter la = getListAdapter();

                    if (la instanceof SelectableCharacterListAdapter) {
                        ((SelectableCharacterListAdapter)la).notifyDataSetChanged();
                    } else if (la instanceof SelectableDeckCardListAdapter) {
                        ((SelectableDeckCardListAdapter)la).notifyDataSetChanged();
                    }
                }
            });
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
                            R.layout.select_character_list_item, deck.getAvailableCharacters()));
                    break;
                case UPGRADE_PAGE:
                    setListAdapter(new SelectableDeckCardListAdapter(getActivity(),
                            R.layout.select_card_list_item, deck.getAvailableUpgrades()));
                    break;
                case SUPPORT_PAGE:
                    setListAdapter(new SelectableDeckCardListAdapter(getActivity(),
                            R.layout.select_card_list_item, deck.getAvailableSupport()));
                    break;
                case EVENT_PAGE:
                    setListAdapter(new SelectableDeckCardListAdapter(getActivity(),
                            R.layout.select_card_list_item, deck.getAvailableEvents()));
                    break;
            }

        }
    }

    public static class DeckBuildingPagerAdapter extends FragmentPagerAdapter {
        Deck deck;

        public DeckBuildingPagerAdapter(FragmentManager fm, Deck deck) {
            super(fm);

            this.deck = deck;
        }

        @Override
        public Fragment getItem(int position) {
            SelectCardsFragment fragment = SelectCardsFragment.newInstance(position);
            fragment.setDeck(deck);
            return fragment;
        }

        @Override
        public int getCount() {
            return NUMBER_OF_PAGES;
        }
    }

}
