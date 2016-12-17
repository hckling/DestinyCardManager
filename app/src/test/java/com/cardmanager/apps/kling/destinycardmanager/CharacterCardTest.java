package com.cardmanager.apps.kling.destinycardmanager;

import com.cardmanager.apps.kling.destinycardmanager.model.Card;
import com.cardmanager.apps.kling.destinycardmanager.model.CardColor;
import com.cardmanager.apps.kling.destinycardmanager.model.CardFaction;
import com.cardmanager.apps.kling.destinycardmanager.model.CardRarity;
import com.cardmanager.apps.kling.destinycardmanager.model.CardSubType;
import com.cardmanager.apps.kling.destinycardmanager.model.CardType;
import com.cardmanager.apps.kling.destinycardmanager.model.CharacterCard;
import com.cardmanager.apps.kling.destinycardmanager.model.DiceCard;
import com.cardmanager.apps.kling.destinycardmanager.model.CharacterSelectionInfo;
import com.cardmanager.apps.kling.destinycardmanager.model.SpecialCompatibility;

import org.junit.Test;

/**
 * Created by danie on 2016-11-24.
 */

public class CharacterCardTest {
    @Test
    public void test_getPointsValue() {
        CharacterCard card = new CharacterCard();
        CharacterSelectionInfo selectedCharacter = new CharacterSelectionInfo(card);

        card.setNormalPointCost(10);
        card.setElitePointCost(13);

        assert(selectedCharacter.getPoints() == 10);

        selectedCharacter.makeElite();
        assert(selectedCharacter.getPoints() == 13);
    }

    @Test
    public void test_isCompatible() {
        CharacterCard finn = new CharacterCard();

        finn.setCardNumber(45);
        finn.setElitePointCost(16);
        finn.setNormalPointCost(13);
        finn.setColor(CardColor.YELLOW);
        finn.setEffect("You can attach any weapon to this character, ignoring play restrictions. \n You can include any red villain weapons and vehicles in your deck.");
        finn.setFaction(CardFaction.HERO);
        finn.setName("Finn");
        finn.setRarity(CardRarity.COMMON);
        finn.setSet("Awakening");
        finn.setType(CardType.CHARACTER);

        finn.addSpecialCompatibility(new SpecialCompatibility() {
            @Override
            public boolean isCompatible(Card card) {
                if (card.getColor() == CardColor.RED && card.getSubType() == CardSubType.WEAPON) {
                    return true;
                } else {
                    return false;
                }
            }
        });

        Card aim = new Card();

        aim.setCardNumber(151);
        aim.setType(CardType.EVENT);
        aim.setEffect("Turn one of your dice to a side showing ranged damage.");
        aim.setRarity(CardRarity.COMMON);
        aim.setName("Aim");
        aim.setFaction(CardFaction.NEUTRAL);
        aim.setColor(CardColor.GREY);
        aim.setSet("Awakening");

        assert(finn.isCompatible(aim));

        DiceCard f11DRifle = new DiceCard();

        f11DRifle.setCardNumber(8);
        f11DRifle.setType(CardType.UPGRADE);
        f11DRifle.setEffect("Redploy. (After a character is defeated, move this card.\nForce an opponent to deal 2 damage to their characters, distributing the damage as they wish.");
        f11DRifle.setRarity(CardRarity.COMMON);
        f11DRifle.setName("F-11D Rifle");
        f11DRifle.setFaction(CardFaction.VILLAIN);
        f11DRifle.setColor(CardColor.RED);
        f11DRifle.setSet("Awakening");

        assert(finn.isCompatible(f11DRifle));

        DiceCard infantryGrenades = new DiceCard();

        infantryGrenades.setCardNumber(17);
        infantryGrenades.setType(CardType.UPGRADE);
        infantryGrenades.setEffect("Deal 2 damage to each of an opponent's characters. Discard this upgrade from play.");
        infantryGrenades.setRarity(CardRarity.COMMON);
        infantryGrenades.setName("Infantry grenades");
        infantryGrenades.setFaction(CardFaction.VILLAIN);
        infantryGrenades.setColor(CardColor.GREY);
        infantryGrenades.setSet("Awakening");

        assert(!finn.isCompatible(infantryGrenades));
    }
}
