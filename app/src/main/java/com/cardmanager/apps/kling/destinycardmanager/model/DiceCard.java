package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-11-24.
 */

public class DiceCard extends Card {

    public DiceCard() {
        dieValues = new ArrayList<>();
    }

    public DieValue getValue(int side) { return dieValues.get(side - 1); }
    public void addDiceValue(int value, DieValueType dieValueType, int cost) {dieValues.add(new DieValue(value, dieValueType, cost)); }
    public String getSpecialEffect() { return specialEffect; }
    public void setSpecialEffect(String specialEffect) { this.specialEffect = specialEffect; }
}
