package com.cardmanager.apps.kling.destinycardmanager.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.CharacterCard;
import com.cardmanager.apps.kling.destinycardmanager.model.SelectedCharacter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danie on 2016-12-11.
 */

public class SelectableCharacterListAdapter extends ArrayAdapter<SelectedCharacter> {
    public SelectableCharacterListAdapter(Context context, int resource, List<SelectedCharacter> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SelectedCharacter card = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_character_list_item, parent, false);
        }

        final LinearLayout root = (LinearLayout) convertView.findViewById(R.id.select_character_list_item_root);

        TextView tvCharacterName = (TextView) convertView.findViewById(R.id.tvCharacterName);
        TextView tvCharacterRule = (TextView) convertView.findViewById(R.id.tvCharacterRule);
        TextView tvFaction = (TextView) convertView.findViewById(R.id.tvFaction);
        final TextView tvPoints = (TextView) convertView.findViewById(R.id.tvPoints);

        tvCharacterName.setText(card.getCard().getName());
        tvCharacterRule.setText(card.getCard().getEffect());
        tvFaction.setText(String.valueOf(card.getCard().getFaction()));
        tvPoints.setText(String.valueOf(card.getPoints()));

        final Button btnAdd = (Button) convertView.findViewById(R.id.btnAdd);
        final Button btnRemove = (Button) convertView.findViewById(R.id.btnRemove);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card.isSelected()) {
                    if (card.isElite()) {
                        card.makeNormal();
                        btnAdd.setText("▲");
                    } else {
                        card.makeElite();
                        btnAdd.setText("▼");
                    }
                } else {
                    card.select();

                    if ((card.getCard().getOwnedCount() > 1) && (card.getCharacterCard().getCanBeElite())) {
                        btnAdd.setText("▲");
                    } else {
                        btnAdd.setEnabled(false);
                    }

                }

                tvPoints.setText(String.valueOf(card.getPoints()));

                if (card.isSelected()) {
                    root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardBackground));
                } else {
                    root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                }
            }
        });

        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.deselect();
                btnAdd.setText("+");
                btnAdd.setEnabled(true);

                if (card.isSelected()) {
                    root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardBackground));
                } else {
                    root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                }
            }
        });

        return convertView;
    }
}
