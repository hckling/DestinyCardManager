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
import com.cardmanager.apps.kling.destinycardmanager.model.DiceCard;
import com.cardmanager.apps.kling.destinycardmanager.model.DieValue;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSelectionInfo;
import com.cardmanager.apps.kling.destinycardmanager.model.CharacterSelectionInfo;

import java.util.List;

/**
 * Created by danie on 2016-12-11.
 */

public class SelectableCharacterListAdapter extends ArrayAdapter<CharacterSelectionInfo> {
    public SelectableCharacterListAdapter(Context context, int resource, List<CharacterSelectionInfo> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CharacterSelectionInfo card = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_character_list_item, parent, false);
        }

        final LinearLayout root = (LinearLayout) convertView.findViewById(R.id.select_character_list_item_root);

        TextView tvCharacterName = (TextView) convertView.findViewById(R.id.tvCharacterName);

        final TextView tvElite = (TextView) convertView.findViewById(R.id.tvElite);
        final TextView tvPoints = (TextView) convertView.findViewById(R.id.tvPoints);

        tvCharacterName.setText(card.getCard().getName());

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

        if (card.isElite()) {
            tvElite.setVisibility(View.VISIBLE);
        } else {
            tvElite.setVisibility(View.GONE);
        }

        final Button btnAddAndPromote = (Button) convertView.findViewById(R.id.btnAdd);
        final Button btnRemove = (Button) convertView.findViewById(R.id.btnRemove);

        btnAddAndPromote.setEnabled(true);

        if (card.isSelected()) {
            if (card.isElite()) {
                btnAddAndPromote.setText("▼");
            } else {
                btnAddAndPromote.setText("▲");

                if (card.isEliteAllowed()) {
                    btnAddAndPromote.setEnabled(true);
                } else {
                    btnAddAndPromote.setEnabled(false);
                }
            }
        } else {
            btnAddAndPromote.setEnabled(true);
            btnAddAndPromote.setText("+");
        }

        if (card.isSelected()) {
            root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardSelected));
        } else {
            root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
        }

        btnRemove.setEnabled(card.isSelected());

        btnAddAndPromote.setOnClickListener(v -> {
            if (card.isSelected()) {
                if (card.isElite()) {
                    card.makeNormal();
                    btnAddAndPromote.setText("▲");
                } else {
                    if (card.isEliteAllowed()) {
                        card.makeElite();
                        btnAddAndPromote.setText("▼");
                    }
                }
            } else {
                card.select();

                if ((card.getCard().getOwnedCount() > 1) && (card.isEliteAllowed())) {
                    btnAddAndPromote.setText("▲");
                } else {
                    btnAddAndPromote.setEnabled(false);
                }

            }

            tvPoints.setText(String.valueOf(card.getPoints()));

            if (card.isSelected()) {
                root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardSelected));
            } else {
                root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            }

            if (card.isElite()) {
                tvElite.setVisibility(View.VISIBLE);
            } else {
                tvElite.setVisibility(View.GONE);
            }
        });

        btnRemove.setOnClickListener(v -> {
            card.deselect();
            btnAddAndPromote.setText("+");
            btnAddAndPromote.setEnabled(true);

            if (card.isSelected()) {
                root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.cardSelected));
            } else {
                root.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
            }
        });

        return convertView;
    }

    private void hideGuiAction(View convertView) {
        LinearLayout llAction = (LinearLayout) convertView.findViewById(R.id.llAction);
        llAction.setVisibility(View.GONE);
    }

    private void showGuiAction(CardSelectionInfo card, View convertView) {
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
                    tvDieResultCost.setVisibility(View.VISIBLE);
                    tvDieResultCost.setText(String.valueOf(result.getCost()));
                } else {
                    tvDieResultCost.setVisibility(View.GONE);
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

    private void showGuiEffect(CharacterSelectionInfo card, View convertView) {
        TextView tvEffect = (TextView) convertView.findViewById(R.id.tvEffect);
        tvEffect.setVisibility(View.VISIBLE);
        tvEffect.setText(card.getCard().getEffect());
    }

    private void updateGuiDefault(CharacterSelectionInfo card, View convertView) {
        ImageView ivCharacterImage = (ImageView) convertView.findViewById(R.id.ivCharacterImage);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvCharacterName);
        final TextView tvElite = (TextView) convertView.findViewById(R.id.tvElite);
        TextView tvFaction = (TextView) convertView.findViewById(R.id.tvFaction);
        final TextView tvPoints = (TextView) convertView.findViewById(R.id.tvPoints);


        // Image
        ivCharacterImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.card_001 + card.getCard().getCardNumber() - 1));
                // Title
        tvTitle.setText(card.getCard().getName());
        tvFaction.setText(String.valueOf(card.getCard().getFaction()));
        tvPoints.setText(String.valueOf(card.getPoints()));

        // Selection count
        if (card.isElite()) {
            tvElite.setVisibility(View.VISIBLE);
        } else {
            tvElite.setVisibility(View.GONE);
        }
    }
}
