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

    @Override
    public double getMeleeAttackRating() {
        double result = 0;

        for(DieValue d: dieValues) {
            switch(d.getValueType()) {
                case MELEE_DAMAGE:
                    result += d.getValue();
                case MELEE_DAMAGE_MODIFIER:
                    result += d.getValue() / 2;
                default:
                    result += 0;
            }
        }

        return result;
    }

    @Override
    public double getRangedAttackRating() {
        double result = 0;

        for(DieValue d: dieValues) {
            switch(d.getValueType()) {
                case RANGED_DAMAGE:
                    result += d.getValue();
                    break;
                case RANGED_DAMAGE_MODIFIER:
                    result += d.getValue() / 2;
                    break;
                default:
                    result += 0;
                    break;
            }
        }

        return result;
    }

    @Override
    public double getCostRating() {
        double result = getCost();

        for(DieValue d: dieValues) {
            result += d.getCost();
        }

        return result;
    }

    @Override
    public double getIncomeRating() {
        double result = 0;

        for(DieValue d: dieValues) {
            switch(d.getValueType()) {
                case RESOURCE:
                    result += d.getValue();
                    break;
                case RESOURCE_MODIFIER:
                    result += d.getValue() / 2;
                    break;
                default:
                    result += 0;
                    break;
            }
        }

        return result;
    }

    @Override
    public double getDefenceRating() {
        double result = 0;

        for(DieValue d: dieValues) {
            switch(d.getValueType()) {
                case SHIELD:
                    result += d.getValue();
                    break;
                default:
                    result += 0;
                    break;
            }
        }

        return result;
    }

    @Override
    public double getFocusRating() {
        double result = 0;

        for(DieValue d: dieValues) {
            switch(d.getValueType()) {
                case FOCUS:
                    result += d.getValue();
                    break;
                default:
                    result += 0;
                    break;
            }
        }

        return result;
    }

    @Override
    public double getDisruptRating() {
        double result = 0;

        for(DieValue d: dieValues) {
            switch(d.getValueType()) {
                case DISRUPT:
                    result += d.getValue();
                    break;
                default:
                    result += 0;
                    break;
            }
        }

        return result;
    }

    @Override
    public double getDiscardRating() {
        double result = 0;

        for(DieValue d: dieValues) {
            switch(d.getValueType()) {
                case DISCARD:
                    result += d.getValue();
                    break;
                default:
                    result += 0;
                    break;
            }
        }

        return result;
    }
}
