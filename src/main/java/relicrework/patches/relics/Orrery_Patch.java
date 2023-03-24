package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.Orrery;
import relicrework.RelicRework;

import java.util.ArrayList;

public class Orrery_Patch {
    private static final int NUM_CARDS = 2; // Opening the combat reward screen adds another card reward
    private static final float UPGRADE_CHANCE = 0.50F;

    @SpirePatch(clz = Orrery.class, method = "onEquip")
    public static class Orrery_ReplaceOnEquip {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(Orrery __instance) {
            if (!RelicRework.isEnabled(Orrery.ID)) {
                return SpireReturn.Continue();
            }

            for (int i = 0; i < NUM_CARDS; i++) {
                AbstractDungeon.getCurrRoom().addCardToRewards();
            }
            AbstractDungeon.combatRewardScreen.open(__instance.DESCRIPTIONS[1]);
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "getRewardCards")
    public static class Orrery_UpgradeRewardCards {
        @SpirePostfixPatch
        public static ArrayList<AbstractCard> patch(ArrayList<AbstractCard> __result) {
            for (AbstractCard card : __result) {
                if (AbstractDungeon.cardRng.randomBoolean(UPGRADE_CHANCE) && card.canUpgrade()) {
                    card.upgrade();
                }
            }

            return __result;
        }
    }
}
