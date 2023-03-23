package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.CeramicFish;
import com.megacrit.cardcrawl.relics.SnakeRing;
import relicrework.RelicRework;

public class SnakeRing_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(SnakeRing.ID);
    private static final int ADDITIONAL_DRAW = 1;
    private static final int ADDITIONAL_ENERGY = 1;

    @SpirePatch(clz = SnakeRing.class, method = "atBattleStart")
    public static class SnakeRing_ReplaceAtBattleStart {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(SnakeRing __instance) {
            if (!RelicRework.isEnabled(SnakeRing.ID)) {
                return SpireReturn.Continue();
            }

            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, __instance));
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, ADDITIONAL_DRAW));
            AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ADDITIONAL_ENERGY));
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = SnakeRing.class, method = "getUpdatedDescription")
    public static class SnakeRing_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(SnakeRing __instance) {
            return RelicRework.isEnabled(SnakeRing.ID) ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
