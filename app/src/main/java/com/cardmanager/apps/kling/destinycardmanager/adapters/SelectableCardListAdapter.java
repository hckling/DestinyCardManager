package com.cardmanager.apps.kling.destinycardmanager.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSet;

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
        Card card = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.card_list_item, parent, false);
        }

        TextView tvCardName = (TextView) convertView.findViewById(R.id.textCardTitle);

        tvCardName.setText(card.getName());

        return convertView;
    }
}
