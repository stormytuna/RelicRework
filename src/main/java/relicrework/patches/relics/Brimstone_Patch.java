package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import com.megacrit.cardcrawl.relics.Brimstone;
import relicrework.RelicRework;

public class Brimstone_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(Brimstone.ID);
    private static final int VIGOR_AMOUNT = 3;

    @SpirePatch(clz = Brimstone.class, method = "atTurnStart")
    public static class Brimstone_ReplaceAtTurnStart {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(Brimstone __instance) {
            if (!RelicRework.isEnabled(Brimstone.ID)) {
                return SpireReturn.Continue();
            }

            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, __instance));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new VigorPower(AbstractDungeon.player, VIGOR_AMOUNT)));
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = Brimstone.class, method = "getUpdatedDescription")
    public static class Brimstone_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(Brimstone __instance) {
            return RelicRework.isEnabled(Brimstone.ID) ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
