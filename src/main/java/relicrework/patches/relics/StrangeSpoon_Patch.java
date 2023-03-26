package relicrework.patches.relics;

import com.evacipated.cardcrawl.mod.stslib.actions.common.MoveCardsAction;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.StrangeSpoon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import relicrework.RelicRework;

public class StrangeSpoon_Patch {
    @SpirePatch(clz = StrangeSpoon.class, method = SpirePatch.CONSTRUCTOR)
    public static class StrangeSpoon_SetCounter {
        @SpirePostfixPatch
        public static void patch(StrangeSpoon __instance) {
            if (!RelicRework.isEnabled(StrangeSpoon.ID)) {
                return;
            }

            __instance.counter = 0;
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardAction_RemoveStrangeSpoonEffect {
        @SpireInsertPatch(locator = Locator.class, localvars = {"spoonProc"})
        public static void patch(UseCardAction __instance, @ByRef boolean[] spoonProc) {
            AbstractPlayer player = AbstractDungeon.player;

            if (!RelicRework.playerHasRelicThatIsEnabled(player, StrangeSpoon.ID)) {
                return;
            }

            spoonProc[0] = false;

            AbstractRelic strangeSpoon = player.getRelic("Strange Spoon");
            // We should let our player move a card to their discard pile
            if (strangeSpoon.counter == 2) {
                AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(player, strangeSpoon));
                strangeSpoon.flash();
                strangeSpoon.counter = 0;
                AbstractDungeon.actionManager.addToBottom(new MoveCardsAction(player.discardPile, player.exhaustPile));
                return;
            }

            // We should increment, and not discard this card
            strangeSpoon.beginPulse();
            strangeSpoon.counter++;
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
            Matcher matcher = new Matcher.FieldAccessMatcher(UseCardAction.class, "exhaustCard");
            int[] lines = LineFinder.findAllInOrder(ctBehavior, matcher);
            return new int[]{lines[1]};
        }
    }
}
