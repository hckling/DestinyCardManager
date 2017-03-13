package com.cardmanager.apps.kling.destinycardmanager.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.activities.BuildDeckPagerActivity;
import com.cardmanager.apps.kling.destinycardmanager.database.CardDatabase;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSelectionInfo;
import com.cardmanager.apps.kling.destinycardmanager.model.CharacterSelectionInfo;
import com.cardmanager.apps.kling.destinycardmanager.model.Deck;

import java.util.ArrayList;

public class DeckListAdapter extends ArrayAdapter<Deck> {

    private DeckSelectionListener deckSelectionListener;

    public DeckListAdapter(Context context, ArrayList<Deck> objects) {
        super(context, 0, objects);
    }

    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        final Deck deck = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.deck_list_item, parent, false);
        }

        TextView tvDeckName = (TextView) convertView.findViewById(R.id.tvDeckName);

        tvDeckName.setText(deck.getName());

        showCharacters(convertView, deck.getSelectedCharacters());

        TextView tvPoints = (TextView) convertView.findViewById(R.id.txtTotalPoints);
        tvPoints.setText(String.valueOf(deck.getTotalPoints()));

        TextView tvCards = (TextView) convertView.findViewById(R.id.txtNumberOfCards);
        tvCards.setText(String.valueOf(deck.getTotalNumberOfSelectedCards()));

        ImageButton tvDeleteButton = (ImageButton) convertView.findViewById(R.id.imgBtnDeleteDeck);

        tvDeleteButton.setOnClickListener((v -> {
            confirmDeletion(v, deck);
        }));

        return convertView;
    }

    private void showCharacters(View v, ArrayList<CharacterSelectionInfo> characters) {
        TextView character1 = (TextView) v.findViewById(R.id.tvCharacter1);
        character1.setVisibility(View.GONE);

        TextView character2 = (TextView) v.findViewById(R.id.tvCharacter2);
        character2.setVisibility(View.GONE);

        TextView character3 = (TextView) v.findViewById(R.id.tvCharacter3);
        character3.setVisibility(View.GONE);

        TextView character4 = (TextView) v.findViewById(R.id.tvCharacter4);
        character4.setVisibility(View.GONE);

        TextView character5 = (TextView) v.findViewById(R.id.tvCharacter5);
        character5.setVisibility(View.GONE);

        TextView character6 = (TextView) v.findViewById(R.id.tvCharacter6);
        character6.setVisibility(View.GONE);


        if (characters.size() > 0) {
            character1.setText(characters.get(0).getCard().getName());
            character1.setVisibility(View.VISIBLE);
        }
        if (characters.size() > 1) {
            character2.setText(characters.get(1).getCard().getName());
            character2.setVisibility(View.VISIBLE);
        }
        if (characters.size() > 2) {
            character3.setText(characters.get(2).getCard().getName());
            character3.setVisibility(View.VISIBLE);
        }

        if (characters.size() > 3) {
            character4.setText(characters.get(3).getCard().getName());
            character4.setVisibility(View.VISIBLE);
        }

        if (characters.size() > 4) {
            character5.setText(characters.get(4).getCard().getName());
            character5.setVisibility(View.VISIBLE);
        }

        if (characters.size() > 5) {
            character6.setText(characters.get(5).getCard().getName());
            character6.setVisibility(View.VISIBLE);
        }
    }

    private void confirmDeletion(View v, Deck deck) {
        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());

        builder.setCancelable(true);
        builder.setTitle("Confirm deletion");
        builder.setMessage("Are you sure you want to delete " + deck.getName() + "?");
        builder.setPositiveButton(android.R.string.yes,
                (dialog, which) -> {
                    deleteDeck(deck);
                });

        builder.setNegativeButton(android.R.string.no, (dialog, which) -> {
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void deleteDeck(Deck deck) {
        CardDatabase db = new CardDatabase(getContext());
        db.deleteDeck(deck);
    }

    public void addDeckSelectionListener(DeckSelectionListener listener) {
        this.deckSelectionListener = listener;
    }
}
