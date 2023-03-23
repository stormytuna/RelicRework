package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.DeadBranch;
import relicrework.RelicRework;

public class DeadBranch_Patch {
    @SpirePatch(clz = DeadBranch.class, method = "onExhaust")
    public static class DeadBranch_ReplaceOnExhaust {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(DeadBranch __instance, AbstractCard card) {
            if (!RelicRework.isEnabled(DeadBranch.ID) || AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                return SpireReturn.Continue();
            }

            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, __instance));

            AbstractCard newCard;
            do {
                newCard = AbstractDungeon.returnTrulyRandomCardInCombat().makeCopy();
            } while (!newCard.exhaust);
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(newCard, false));

            return SpireReturn.Return();
        }
    }
}
