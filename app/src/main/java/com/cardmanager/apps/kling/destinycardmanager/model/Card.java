package com.cardmanager.apps.kling.destinycardmanager.model;

/**
 * Created by danie on 2016-11-23.
 */
public class Card {
    int cardNumber;
    String set;
    String name;
    String effect;
    String action;
    String quote;
    String claim;
    CardColor color;
    CardFaction faction;
    CardType type;
    CardSubType subType = CardSubType.NONE;
    CardRarity rarity;
    int cost;
    String restriction;

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
