package com.cardmanager.apps.kling.destinycardmanager.model;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * The CardSetBuilder reads the XML file and creates the card sets into memory.
 */

public class CardSetBuilder {
    private static ArrayList<CardSet> cardSets;

    public static ArrayList<CardSet> readCardsXml(XmlPullParser parser) throws IOException, ParseException {
        if (cardSets == null) {
            try {
                cardSets = new ArrayList<>();

                String name;
                int attributes = 0;

                do {
                    name = parser.getName();

                    if (name != null) {
                        if (name.equals("set")) {
                            cardSets.add(parseSet(parser));
                        }
                    }

                    parser.next();
                } while (parser.getEventType() != XmlPullParser.END_DOCUMENT);

                return cardSets;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        } else {
            return cardSets;
        }

        return null;
    }

    public static Card getCard(int cardNumber) {
        for(CardSet cs: cardSets) {
            for (Card c : cs.getCards()) {
                if (c.getCardNumber() == cardNumber)
                {
                    return c;
                }
            }
        }

        return null;
    }

    private static CardSet parseSet(XmlPullParser parser) throws IOException, XmlPullParserException, ParseException {
        CardSet cardSet = new CardSet();
        cardSet.setName(parser.getAttributeValue(null, "name"));

        do {
            parser.next();
            if ((parser.getEventType() == XmlPullParser.START_TAG) && (parser.getName().equals("card"))) {
                cardSet.addCard(parseCard(parser));
            }
        } while (!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equals("set"))));

        return cardSet;
    }

    private static Card parseCard(XmlPullParser parser) throws IOException, XmlPullParserException, ParseException {
        switch(CardType.fromString(parser.getAttributeValue(null, "type"))) {
            case CHARACTER: return parseCharacterCard(parser);
            case BATTLEFIELD: return parseBattlefieldCard(parser);
            case EVENT: return parseEventCard(parser);
            case SUPPORT: return parseSupportCard(parser);
            case UPGRADE: return parseUpgradeCard(parser);
        }

        return null;
    }

    private static Card parseCharacterCard(XmlPullParser parser) throws ParseException, IOException, XmlPullParserException {
        CharacterCard card = new CharacterCard();
        card.setType(CardType.CHARACTER);
        parseCommonCardProperties(card, parser);

        card.setNormalPointCost(readIntAttribute(parser, "normalCost"));
        card.setElitePointCost(readIntAttribute(parser, "eliteCost"));

        do {
            parser.next();

            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equalsIgnoreCase("restriction")) {
                    parseRestriction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("action")) {
                    parseAction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("effect")) {
                    parseEffect(card, parser);
                } else if (parser.getName().equalsIgnoreCase("specialEffect")) {
                    parseSpecialEffect(card, parser);
                } else if (parser.getName().equalsIgnoreCase("dice")) {
                    parseDice(card, parser);
                } else if (parser.getName().equalsIgnoreCase("quote")) {
                    parseQuote(card, parser);
                }
            }
        } while (!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equalsIgnoreCase("card"))));

        return card;
    }

    private static int readIntAttribute(XmlPullParser parser, String attributeName) {
        String valueStr = parser.getAttributeValue(null, attributeName);
        if (valueStr != null) {
            return Integer.valueOf(valueStr);
        } else {
            return 0;
        }
    }

    private static void parseQuote(Card card, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.next();

        if (parser.getEventType() == XmlPullParser.TEXT) {
            card.setQuote(parser.getText());
        }
    }

    private static void parseDice(DiceCard card, XmlPullParser parser) throws XmlPullParserException, ParseException, IOException {
        do{
            if ((parser.getEventType() == XmlPullParser.START_TAG) && parser.getName().equalsIgnoreCase("diceResult")) {
                int value = Integer.valueOf(parser.getAttributeValue(null, "value"));
                DieValueType type = DieValueType.fromString(parser.getAttributeValue(null, "type"));

                int cost = 0;

                String costStr = parser.getAttributeValue(null, "cost");
                if (costStr != null) {
                    cost = Integer.valueOf(costStr);
                }

                card.addDiceValue(value, type, cost);
            }

            parser.next();
        } while(!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equalsIgnoreCase("dice"))));
    }

    private static void parseEffect(Card card, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.next();

        if (parser.getEventType() == XmlPullParser.TEXT) {
            card.setEffect(parser.getText());
        }
    }

    private static void parseAction(Card card, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.next();

        if (parser.getEventType() == XmlPullParser.TEXT) {
            card.setAction(parser.getText());
        }
    }

    private static void parseRestriction(Card card, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.next();

        if (parser.getEventType() == XmlPullParser.TEXT) {
            card.setRestriction(parser.getText());
        }
    }

    private static Card parseBattlefieldCard(XmlPullParser parser) throws ParseException, IOException, XmlPullParserException {
        Card card = new Card();
        card.setType(CardType.BATTLEFIELD);
        parseCommonCardProperties(card, parser);

        do {
            parser.next();

            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equalsIgnoreCase("restriction")) {
                    parseRestriction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("claim")) {
                    parseClaim(card, parser);
                }
            }
        } while (!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equalsIgnoreCase("card"))));

        return card;
    }

    private static void parseClaim(Card card, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.next();

        if (parser.getEventType() == XmlPullParser.TEXT) {
            card.setClaim(parser.getText());
        }
    }

    private static Card parseEventCard(XmlPullParser parser) throws ParseException, IOException, XmlPullParserException {
        Card card = new Card();
        card.setType(CardType.EVENT);
        parseCommonCardProperties(card, parser);

        do {
            parser.next();

            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equalsIgnoreCase("restriction")) {
                    parseRestriction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("action")) {
                    parseAction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("effect")) {
                    parseEffect(card, parser);
                } else if (parser.getName().equalsIgnoreCase("quote")) {
                    parseQuote(card, parser);
                }
            }
        } while (!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equalsIgnoreCase("card"))));

        return card;
    }

    private static Card parseSupportCard(XmlPullParser parser) throws ParseException, IOException, XmlPullParserException {
        DiceCard card = new DiceCard();
        card.setType(CardType.SUPPORT);

        parseCommonCardProperties(card, parser);

        card.setSubType(CardSubType.fromString(parser.getAttributeValue(null, "subtype")));


        do {
            parser.next();

            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equalsIgnoreCase("dice")) {
                    parseDice(card, parser);
                } if (parser.getName().equalsIgnoreCase("restriction")) {
                    parseRestriction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("action")) {
                    parseAction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("effect")) {
                    parseEffect(card, parser);
                } else if (parser.getName().equalsIgnoreCase("specialEffect")) {
                    parseSpecialEffect(card, parser);
                } else if (parser.getName().equalsIgnoreCase("quote")) {
                    parseQuote(card, parser);
                }
            }
        } while (!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equalsIgnoreCase("card"))));

        return card;
    }


    private static void parseSpecialEffect(DiceCard card, XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.next();

        if (parser.getEventType() == XmlPullParser.TEXT) {
            card.setSpecialEffect(parser.getText());
        }
    }

    private static Card parseUpgradeCard(XmlPullParser parser) throws ParseException, IOException, XmlPullParserException {
        DiceCard card = new DiceCard();
        card.setType(CardType.UPGRADE);

        parseCommonCardProperties(card, parser);

        card.setSubType(CardSubType.fromString(parser.getAttributeValue(null, "subtype")));

        do {
            parser.next();

            if (parser.getEventType() == XmlPullParser.START_TAG) {
                if (parser.getName().equalsIgnoreCase("dice")) {
                    parseDice(card, parser);
                } if (parser.getName().equalsIgnoreCase("restriction")) {
                    parseRestriction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("action")) {
                    parseAction(card, parser);
                } else if (parser.getName().equalsIgnoreCase("effect")) {
                    parseEffect(card, parser);
                } else if (parser.getName().equalsIgnoreCase("specialEffect")) {
                    parseSpecialEffect(card, parser);
                } else if (parser.getName().equalsIgnoreCase("quote")) {
                    parseQuote(card, parser);
                }
            }
        } while (!((parser.getEventType() == XmlPullParser.END_TAG) && (parser.getName().equalsIgnoreCase("card"))));

        return card;
    }

    private static void parseCommonCardProperties(Card card, XmlPullParser parser) throws ParseException {
        card.setName(parser.getAttributeValue(null, "name"));
        card.setColor(CardColor.fromString(parser.getAttributeValue(null, "color")));
        card.setFaction(CardFaction.fromString(parser.getAttributeValue(null, "faction")));
        card.setRarity(CardRarity.fromString(parser.getAttributeValue(null, "rarity")));
        card.setCardNumber(Integer.valueOf(parser.getAttributeValue(null, "cardNr")));

        String costStr = parser.getAttributeValue(null, "cost");

        if (costStr != null) {
            card.setCost(Integer.valueOf(costStr));
        }

        card.setUnique(Boolean.valueOf(parser.getAttributeValue(null, "isUnique")));
    }
}
