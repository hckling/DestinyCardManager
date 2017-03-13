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
        TextView tvCharacters = (TextView) convertView.findViewById(R.id.tvCharacters);

        tvDeckName.setText(deck.getName());
        String characters = "";
        for (CharacterSelectionInfo csi : deck.getSelectedCharacters()) {
            characters += csi.getCharacterCard().getName() + "\n";
        }
        tvCharacters.setText(characters);

        TextView tvPoints = (TextView) convertView.findViewById(R.id.txtTotalPoints);
        tvPoints.setText(String.valueOf(deck.getTotalPoints()));

        ImageButton tvDeleteButton = (ImageButton) convertView.findViewById(R.id.imgBtnDeleteDeck);

        tvDeleteButton.setOnClickListener((v -> {
            confirmDeletion(v, deck);
        }));

        return convertView;
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
