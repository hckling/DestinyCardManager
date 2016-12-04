package com.cardmanager.apps.kling.destinycardmanager.model;

/**
 * Created by danie on 2016-11-24.
 */

public class DieValue {
    DieValueType valueType;
    int value;
    int cost;

    public DieValue(int value, DieValueType valueType, int cost) {this.value = value; this.valueType = valueType; this.cost = cost; }

    public DieValueType getValueType() {return valueType; }
    public int getValue() { return value; }
    public int getCost() { return cost; }
}
