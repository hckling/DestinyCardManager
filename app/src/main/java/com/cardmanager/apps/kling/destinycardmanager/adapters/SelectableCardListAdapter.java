package com.cardmanager.apps.kling.destinycardmanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.model.Card;

import java.util.ArrayList;

/**
 * Created by danie on 2016-11-27.
 */

public class SelectableCardListAdapter extends ArrayAdapter<Card> {
    public SelectableCardListAdapter(Context context, ArrayList<Card> cards)
    {
        super(context, 0, cards);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Card card = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_list_item, parent, false);
        }

        TextView tvCardName = (TextView) convertView.findViewById(R.id.textCardTitle);
        TextView tvSetName = (TextView) convertView.findViewById(R.id.textCardSet);
        final TextView tvCardCount = (TextView) convertView.findViewById(R.id.textCardCount);

        tvCardName.setText(card.getName());
        tvSetName.setText(card.getSet());
        tvCardCount.setText(String.valueOf(card.getOwnedCount()));

        Button add = (Button) convertView.findViewById(R.id.btnAdd);
        Button remove = (Button) convertView.findViewById(R.id.btnRemove);

        remove.setEnabled(card.getOwnedCount() > 0);

        add.setOnClickListener(v -> {
            card.increaseOwnedCount();
            tvCardCount.setText(String.valueOf(card.getOwnedCount()));
            remove.setEnabled(card.getOwnedCount() > 0);
        });

        remove.setOnClickListener(v -> {
            card.decreaseOwnedCount();
            tvCardCount.setText(String.valueOf(card.getOwnedCount()));
            remove.setEnabled(card.getOwnedCount() > 0);
        });

        return convertView;
    }
}
