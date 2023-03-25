package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.Ectoplasm;
import com.megacrit.cardcrawl.shop.ShopScreen;
import relicrework.RelicRework;

import java.util.ArrayList;

public class Ectoplasm_Patch {
    private static final float SHOP_PRICE_MULTIPLIER = 1.5F;

    @SpirePatch(clz = AbstractPlayer.class, method = "gainGold")
    public static class AbstractPlayer_RemoveOriginalEffect {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(AbstractPlayer __instance, int amount) {
            if (__instance.hasRelic(Ectoplasm.ID) && RelicRework.isEnabled(Ectoplasm.ID)) {
                CardCrawlGame.goldGained += amount;
                __instance.gold += amount;
                for (AbstractRelic r : __instance.relics)
                    r.onGainGold();

                return SpireReturn.Return();
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ShopScreen.class, method = "init")
    public static class ShopScreen_ApplyPriceIncreases {
        @SpirePostfixPatch
        public static void patch(ShopScreen __instance, ArrayList<AbstractCard> coloredCards, ArrayList<AbstractCard> colorlessCards) {
            if (RelicRework.isEnabled(Ectoplasm.ID) && AbstractDungeon.player.hasRelic(Ectoplasm.ID)) {
                __instance.applyDiscount(SHOP_PRICE_MULTIPLIER, true);
            }
        }
    }
}
