package com.cardmanager.apps.kling.destinycardmanager.model;

import java.util.EventListener;

/**
 * Class representing a basic card. The base class for all cards.
 */
public class Card {
    protected int cardNumber;
    protected String set;
    protected String name;
    protected String effect;
    protected String action;
    protected String quote;
    protected String claim;
    protected CardColor color;
    protected CardFaction faction;
    protected CardType type;
    protected CardSubType subType = CardSubType.NONE;
    protected CardRarity rarity;
    protected int cost;
    protected String restriction;

    protected int ownedCount = 0;

    public int getOwnedCount() {
        return ownedCount;
    }

    public void setOwnedCount(int ownedCount) {
        this.ownedCount = ownedCount;
    }

    public void increaseOwnedCount() { ownedCount++; }
    public void decreaseOwnedCount() { ownedCount--; }

    public String getClaim() {
        return claim;
    }

    public void setClaim(String claim) {
        this.claim = claim;
    }

    public String getRestriction() {
        return restriction;
    }

    public void setRestriction(String restriction) {
        this.restriction = restriction;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public CardSubType getSubType() {
        return subType;
    }

    public void setSubType(CardSubType subType) {
        this.subType = subType;
    }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public int getCardNumber() {return cardNumber; }
    public void setCardNumber(int cardNumber) { this.cardNumber = cardNumber; }

    public String getName() { return name; }
    public void setName(String name) {this.name = name; }

    public String getEffect() { return effect; }
    public void setEffect(String effect) {this.effect = effect; }

    public CardColor getColor () { return color; }
    public void setColor(CardColor color) {this.color = color; }

    public CardFaction getFaction() { return faction; }
    public void setFaction(CardFaction faction) { this.faction = faction; }

    public CardType getType() { return type; }
    public void setType(CardType type) { this.type = type; }

    public String getSet() { return set; }
    public void setSet(String set) { this.set = set; }

    public CardRarity getRarity() {return rarity; }
    public void setRarity(CardRarity rarity) { this.rarity = rarity; }

    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }
}
