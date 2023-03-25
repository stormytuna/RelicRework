package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.status.Wound;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MarkOfPain;
import relicrework.RelicRework;

public class MarkOfPain_Patch {
    private static final int NUM_WOUNDS = 3;

    @SpirePatch(clz = MarkOfPain.class, method = "atBattleStart")
    public static class MarkOfPain_ReplaceAtBattleStart {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(MarkOfPain __instance) {
            if (!RelicRework.isEnabled(MarkOfPain.ID)) {
                return SpireReturn.Continue();
            }

            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, __instance));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Wound(), NUM_WOUNDS));
            return SpireReturn.Return();
        }
    }
}
