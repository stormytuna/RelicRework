package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.CeramicFish;
import com.megacrit.cardcrawl.shop.ShopScreen;
import relicrework.RelicRework;

import java.util.ArrayList;

public class CeramicFish_Patch {
    private static final int FLAT_DISCOUNT = 20;
    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings("CeramicFish");

    @SpirePatch(clz = CeramicFish.class, method = "onObtainCard")
    public static class CeramicFish_PrefixOnObtainCard {
        @SpirePrefixPatch
        public static SpireReturn patch(CeramicFish __instance, AbstractCard card) {
            if (RelicRework.changeCeramicFish) {
                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CeramicFish.class, method = "getUpdatedDescription")
    public static class CeramicFish_PrefixGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(CeramicFish __instance) {
            if (RelicRework.changeCeramicFish) {
                return SpireReturn.Return(relicStrings.DESCRIPTIONS[0]);
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "init")
    public static class ShopScreen_PostfixInit {
        @SpirePostfixPatch
        public static void patch(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
            if (RelicRework.changeCeramicFish) {
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
