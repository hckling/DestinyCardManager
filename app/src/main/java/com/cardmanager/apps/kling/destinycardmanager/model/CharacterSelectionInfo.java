package com.cardmanager.apps.kling.destinycardmanager.model;

/**
 * Created by danie on 2016-12-13.
 */

public class CharacterSelectionInfo extends CardSelectionInfo {
    boolean isElite = false;
    private boolean eliteAllowed = true;

    public CharacterSelectionInfo(CharacterCard card) {
        super(card);
    }

    public int getPoints() {
        if (isElite) {
            return getCharacterCard().getElitePointCost();
        } else {
            return getCharacterCard().getNormalPointCost();
        }
    }

    public CharacterCard getCharacterCard() { return (CharacterCard) getCard(); }

    public boolean isEliteAllowed() { return eliteAllowed && getCharacterCard().getCanBeElite(); }
    public void allowElite() { eliteAllowed = true; }
    public void disallowElite() {eliteAllowed = false; }

    public void makeElite() {
        isElite = true;
        raiseSelectionChanged();
    }

    public void makeNormal() {
        isElite = false;
        raiseSelectionChanged();
    }

    public boolean isElite() { return isElite; }

    @Override
    public int getDiceCount() {
        if (isElite()) {
            return 2;
        } else {
            return 1;
        }
    }

    @Override
    public double getMeleeAttackRating() {
        if (isElite())
        {
            return super.getMeleeAttackRating() * 2;
        } else {
            return super.getMeleeAttackRating();
        }
    }

    public double getRangedAttackRating() {
        if (isElite())
        {
            return super.getRangedAttackRating() * 2;
        } else {
            return super.getRangedAttackRating();
        }
    }

    public double getCostRating() {
        if (isElite())
        {
            return super.getCostRating() * 2;
        } else {
            return super.getCostRating();
        }
    }

    public double getIncomeRating() {
        if (isElite())
        {
            return super.getIncomeRating() * 2;
        } else {
            return super.getIncomeRating();
        }
    }

    public double getDefenceRating() {
        if (isElite())
        {
            return super.getDefenceRating() * 2;
        } else {
            return super.getDefenceRating();
        }
    }

    public double getFocusRating() {
        if (isElite())
        {
            return super.getFocusRating() * 2;
        } else {
            return super.getFocusRating();
        }
    }

    public double getDisruptRating() {
        if (isElite())
        {
            return super.getDisruptRating() * 2;
        } else {
            return super.getDisruptRating();
        }
    }

    public double getDiscardRating() {
        if (isElite())
        {
            return super.getDiscardRating() * 2;
        } else {
            return super.getDiscardRating();
        }
    }
}
