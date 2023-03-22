package relicrework.patches.relics;

import basemod.ReflectionHacks;
import basemod.devcommands.relic.Relic;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.PenNib;
import com.megacrit.cardcrawl.relics.StrangeSpoon;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import relicrework.RelicRework;

import java.util.ArrayList;

public class StrangeSpoon_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(StrangeSpoon.ID);

    @SpirePatch(clz = StrangeSpoon.class, method = SpirePatch.CONSTRUCTOR)
    public static class StrangeSpoon_PostfixCtor {
        @SpirePostfixPatch
        public static void patch(StrangeSpoon __instance) {
            __instance.counter = 0;
        }
    }

    @SpirePatch(clz = StrangeSpoon.class, method = "getUpdatedDescription")
    public static class StrangeSpoon_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(StrangeSpoon __instance) {
            return RelicRework.changeStrangeSpoon ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class UseCardAction_RemoveStrangeSpoonEffect {
        @SpireInsertPatch(locator = Locator.class, localvars = { "spoonProc" })
        public static void patch(UseCardAction __instance, @ByRef boolean[] spoonProc) {
            AbstractPlayer player = AbstractDungeon.player;
            if (RelicRework.changeStrangeSpoon && player.hasRelic("Strange Spoon")) {
                AbstractRelic strangeSpoon = player.getRelic("Strange Spoon");
                // We should discard this card
                if (strangeSpoon.counter == 1) {
                    ReflectionHacks.setPrivate(strangeSpoon, strangeSpoon.getClass(), "pulse", false);
                    AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(player, strangeSpoon));
                    strangeSpoon.flash();

                    strangeSpoon.counter = 0;
                    spoonProc[0] = true;
                    return;
                }

                // We should increment, and not discard this card
                strangeSpoon.beginPulse();
                ReflectionHacks.setPrivate(strangeSpoon, strangeSpoon.getClass(), "pulse", true);
                spoonProc[0] = false;

                strangeSpoon.counter++;
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
            Matcher matcher = new Matcher.FieldAccessMatcher(UseCardAction.class, "exhaustCard");
            int[] lines = LineFinder.findAllInOrder(ctBehavior, matcher);
            return new int[] { lines[1] };
        }
    }
}
