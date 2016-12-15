package com.cardmanager.apps.kling.destinycardmanager.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cardmanager.apps.kling.destinycardmanager.R;
import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSubType;
import com.cardmanager.apps.kling.destinycardmanager.model.DiceCard;
import com.cardmanager.apps.kling.destinycardmanager.model.DieValue;
import com.cardmanager.apps.kling.destinycardmanager.model.SelectableCard;

import java.util.List;

/**
 * Created by danie on 2016-12-13.
 */

public class SelectableDeckCardListAdapter extends ArrayAdapter<SelectableCard> {
    private static final int MAX_SELECTABLE = 2;

    public SelectableDeckCardListAdapter(Context context, int resource, List<SelectableCard> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SelectableCard card = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_card_list_item, parent, false);
        }

        final View localConvertView = convertView;

        updateGuiDefault(card, convertView);

        if (card.getCard().hasAction()) {
            showGuiAction(card, convertView);
        } else {
            hideGuiAction(convertView);
        }

        if (card.getCard().hasEffect()) {
            showGuiEffect(card, convertView);
        } else {
            hideGuiEffect(convertView);
        }

        if (card.getCard().hasDice()) {
            showGuiDieResults((DiceCard) card.getCard(), convertView);
        } else {
            hideGuiDieResults(card.getCard(), convertView);
        }

        if (card.getCard().hasSpecialEffect()) {
            showGuiSpecialEffect(card.getCard(), convertView);
        } else {
            hideGuiSpecialEffect(card.getCard(), convertView);
        }

        final LinearLayout root = (LinearLayout) convertView.findViewById(R.id.select_card_list_item_root);
        final Button btnAdd = (Button) convertView.findViewById(R.id.btnAdd);
        final Button btnRemove = (Button) convertView.findViewById(R.id.btnRemove);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card.select();

                btnAdd.setEnabled(card.getCount() < Math.min(card.getCard().getOwnedCount(), MAX_SELECTABLE));
                btnRemove.setEnabled(card.getCount() > 0);

                updateGuiDefault(card, localConvertView);

                if (card.getCount() > 0) {
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

                btnRemove.setEnabled(card.getCount() > 0);
                btnAdd.setEnabled(card.getCount() < Math.min(card.getCard().getOwnedCount(), MAX_SELECTABLE));
                updateGuiDefault(card, localConvertView);

                if (card.getCount() > 0) {
                    root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardBackground));
                } else {
                    root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.lightGrey));
                }
            }
        });

        return convertView;
    }

    private void hideGuiAction(View convertView) {
        LinearLayout llAction = (LinearLayout) convertView.findViewById(R.id.llAction);
        llAction.setVisibility(View.GONE);
    }

    private void showGuiAction(SelectableCard card, View convertView) {
        LinearLayout llAction = (LinearLayout) convertView.findViewById(R.id.llAction);
        llAction.setVisibility(View.VISIBLE);

        TextView tvAction = (TextView) convertView.findViewById(R.id.tvAction);
        tvAction.setText(card.getCard().getAction());
    }

    private void hideGuiDieResults(Card card, View convertView) {
        LinearLayout llDiceResults = (LinearLayout) convertView.findViewById(R.id.llDiceResult);
        llDiceResults.setVisibility(View.GONE);
    }

    private void showGuiDieResults(DiceCard card, View convertView) {
        LinearLayout llDiceResults = (LinearLayout) convertView.findViewById(R.id.llDiceResult);
        llDiceResults.setVisibility(View.VISIBLE);

        for (int i = 1; i <= 6; i++) {
            DieValue result = card.getValue(i);

            String viewName = "tvDiceResult" + String.valueOf(i);

            if (viewExists(convertView, viewName))
            {
                TextView tvDieResultValue = (TextView) getDynamicView(convertView, viewName);
                if (result.getValue() > 0) {
                    if (result.getValueType().isModifier()) {
                        tvDieResultValue.setText("+" + String.valueOf(result.getValue()));
                    } else {
                        tvDieResultValue.setText(String.valueOf(result.getValue()));
                    }
                } else {
                    tvDieResultValue.setText("");
                }
            }

            viewName = "tvDiceResult" + String.valueOf(i) + "cost";
            if (viewExists(convertView, viewName)) {
                TextView tvDieResultCost = (TextView) getDynamicView(convertView, viewName);

                if (result.getCost() > 0) {
                    tvDieResultCost.setText(String.valueOf(result.getCost()));
                } else {
                    tvDieResultCost.setText("");
                }
            }

            viewName = "ivDiceResult" + String.valueOf(i);
            if (viewExists(convertView, viewName)) {
                ImageView ivDieResult = (ImageView) getDynamicView(convertView, viewName);
                ivDieResult.setImageResource(result.getValueType().getImageResourceId());
            }

            viewName = "ivDiceResult" + String.valueOf(i) + "cost";
            if (viewExists(convertView, viewName)) {
                ImageView ivDieResultCost = (ImageView) getDynamicView(convertView, viewName);

                if (result.getCost() > 0) {
                    ivDieResultCost.setVisibility(View.VISIBLE);
                } else {
                    ivDieResultCost.setVisibility(View.INVISIBLE);
                }
            }
        }
    }

    private View getDynamicView(View convertView, String viewName) {
        int viewId = convertView.getResources().getIdentifier(viewName, "id", getContext().getPackageName());
        return convertView.findViewById(viewId);
    }

    private boolean viewExists(View convertView, String viewName) {
        return convertView.getResources().getIdentifier(viewName, "id", getContext().getPackageName()) > 0;
    }

    private void hideGuiSpecialEffect(Card card, View convertView) {
        LinearLayout llSpecialEffect = (LinearLayout) convertView.findViewById(R.id.llSpecialEffect);
        llSpecialEffect.setVisibility(View.GONE);
    }

    private void showGuiSpecialEffect(Card card, View convertView) {
        LinearLayout llSpecialEffect = (LinearLayout) convertView.findViewById(R.id.llSpecialEffect);
        llSpecialEffect.setVisibility(View.VISIBLE);

        TextView tvSpecialEffect = (TextView) convertView.findViewById(R.id.tvSpecialEffect);
        tvSpecialEffect.setText(card.getSpecialEffect());
    }

    private void hideGuiEffect(View convertView) {
        TextView tvEffect = (TextView) convertView.findViewById(R.id.tvEffect);
        tvEffect.setVisibility(View.GONE);
    }

    private void showGuiEffect(SelectableCard card, View convertView) {
        TextView tvEffect = (TextView) convertView.findViewById(R.id.tvEffect);
        tvEffect.setVisibility(View.VISIBLE);
        tvEffect.setText(card.getCard().getEffect());
    }

    private void updateGuiDefault(SelectableCard card, View convertView) {
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvCardName);
        final TextView tvCardCount = (TextView) convertView.findViewById(R.id.tvCardCount);

        // Title
        tvTitle.setText(card.getCard().getName());

        // Selection count
        if (card.getCount() > 0) {
            tvCardCount.setVisibility(View.VISIBLE);
            tvCardCount.setText("(x" + String.valueOf(card.getCount()) + ")");
        } else {
            tvCardCount.setVisibility(View.GONE);
        }

        // Card type
        TextView tvCardType = (TextView) convertView.findViewById(R.id.tvType);
        tvCardType.setText(card.getCard().getType().toString());

        if (card.getCard().getSubType() != CardSubType.NONE) {
            TextView tvCardSubType = (TextView) convertView.findViewById(R.id.tvSubType);
            tvCardSubType.setVisibility(View.VISIBLE);
            tvCardSubType.setText(card.getCard().getSubType().toString());

            TextView tvTypeSeparator = (TextView) convertView.findViewById(R.id.tvTypeSeparator);
            tvTypeSeparator.setVisibility(View.VISIBLE);
        } else {
            TextView tvCardSubType = (TextView) convertView.findViewById(R.id.tvSubType);
            tvCardSubType.setVisibility(View.GONE);

            TextView tvTypeSeparator = (TextView) convertView.findViewById(R.id.tvTypeSeparator);
            tvTypeSeparator.setVisibility(View.GONE);
        }
    }
}
