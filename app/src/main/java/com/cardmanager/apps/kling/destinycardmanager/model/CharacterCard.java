package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.ArrayList;

/**
 * Created by danie on 2016-11-23.
 */

public class CharacterCard extends DiceCard {
    ArrayList<SpecialCompatibility> specialCompatibilities = new ArrayList<SpecialCompatibility>();

    int normalPointCost;
    int elitePointCost;

    public boolean isCompatible(Card card) {
        boolean cardIsCompatible = false;

        if (card.getType() == CardType.CHARACTER) {
            cardIsCompatible = card.getFaction() == getFaction();
        } else {
            cardIsCompatible = isColorCompatible(card.getColor()) && isFactionCompatible(card.getFaction());

            for (int i = 0; i < specialCompatibilities.size(); i++) {
                if (specialCompatibilities.get(i).isCompatible(card)) {
                    cardIsCompatible = true;
                }
            }
        }

        return cardIsCompatible;
    }

    public void addSpecialCompatibility(SpecialCompatibility compatibility) { specialCompatibilities.add(compatibility); }

    private boolean isColorCompatible(CardColor color) { return (this.color == color) || (color == CardColor.GREY); }
    private boolean isFactionCompatible(CardFaction faction) { return (this.faction == faction) || (faction == CardFaction.NEUTRAL); }

    public void setNormalPointCost(int normalPointCost) { this.normalPointCost = normalPointCost; }
    public int getNormalPointCost() {return this.normalPointCost; }

    public void setElitePointCost(int elitePointCost) { this.elitePointCost = elitePointCost; }
    public int getElitePointCost() { return this.elitePointCost; }

    public boolean getCanBeElite() {
        return elitePointCost > 0;
    }

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
                    break;
            }
        }

        return result;
    }
}
