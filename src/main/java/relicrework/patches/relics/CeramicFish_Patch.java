package relicrework.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.CeramicFish;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StoreRelic;
import relicrework.RelicRework;

import java.util.ArrayList;

public class CeramicFish_Patch {
    private static final int FLAT_DISCOUNT = 20;
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(CeramicFish.ID);

    @SpirePatch(clz = CeramicFish.class, method = "onObtainCard")
    public static class CeramicFish_RemoveOnObtainCard {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(CeramicFish __instance, AbstractCard card) {
            return RelicRework.changeCeramicFish ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CeramicFish.class, method = "getUpdatedDescription")
    public static class CeramicFish_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(CeramicFish __instance) {
            return RelicRework.changeCeramicFish ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "init")
    public static class ShopScreen_ApplyDiscount {
        @SpirePostfixPatch
        public static void patch(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
            if (RelicRework.changeCeramicFish && AbstractDungeon.player.hasRelic(CeramicFish.ID)) {
                // Why is this even a private field :(
                ArrayList<StoreRelic> relics =  ReflectionHacks.getPrivate(__instance, __instance.getClass(), "relics");
                for (StoreRelic relic : relics) {
                    int discountedPrice = relic.price - FLAT_DISCOUNT;
                    relic.price = Math.max(discountedPrice, 0);
                }
                ReflectionHacks.setPrivate(__instance, __instance.getClass(), "relics", relics);

                for (AbstractCard coloredCard : __instance.coloredCards) {
                    int discountedPrice = coloredCard.price - FLAT_DISCOUNT;
                    coloredCard.price = Math.max(discountedPrice, 0);
                }
                for (AbstractCard colorlessCard : __instance.colorlessCards) {
                    int discountedPrice = colorlessCard.price - FLAT_DISCOUNT;
                    colorlessCard.price = Math.max(discountedPrice, 0);
                }
            }
        }
    }
}
