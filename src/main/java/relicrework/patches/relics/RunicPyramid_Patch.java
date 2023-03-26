package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardAtEndOfTurnAction;
import com.megacrit.cardcrawl.actions.unique.RetainCardsAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.EquilibriumPower;
import com.megacrit.cardcrawl.powers.RetainCardPower;
import com.megacrit.cardcrawl.relics.RunicPyramid;
import javassist.*;
import relicrework.RelicRework;

public class RunicPyramid_Patch {
    private static final String ON_PLAYER_END_TURN_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Runic Pyramid\")) {" +
            "       com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.unique.RetainCardsAction(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, 3));" +
            "   }" +
            "}";

    @SpirePatch(clz = RunicPyramid.class, method = SpirePatch.CONSTRUCTOR)
    public static class RunicPyramid_AddOnPlayerEndTurnMethod {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException {
            CtClass ctRunicPyramidClass = ctMethodToPatch.getDeclaringClass();

            CtMethod onPlayerEndTurnMethod = CtNewMethod.make(CtClass.voidType, "onPlayerEndTurn", null, null, ON_PLAYER_END_TURN_METHOD_BODY, ctRunicPyramidClass);
            ctRunicPyramidClass.addMethod(onPlayerEndTurnMethod);
        }
    }

    @SpirePatch(clz = RetainCardPower.class, method = "atEndOfTurn")
    public static class RetainCardPower_RemoveRunicPyramidEffect {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(RetainCardPower __instance, boolean isPlayer) {
            if (!RelicRework.isEnabled(RunicPyramid.ID)) {
                return SpireReturn.Continue();
            }

            if (isPlayer && !AbstractDungeon.player.hand.isEmpty() && !AbstractDungeon.player.hasPower(EquilibriumPower.POWER_ID)) {
                AbstractDungeon.actionManager.addToBottom(new RetainCardsAction(__instance.owner, __instance.amount));
            }
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = DiscardAtEndOfTurnAction.class, method = "update")
    public static class DiscardAtEndOfTurnAction_RemoveRunicPyramidEffect {
        @SpireInsertPatch(locator = Locator.class)
        public static void patch(DiscardAtEndOfTurnAction __instance) {
            AbstractPlayer player = AbstractDungeon.player;

            if (!RelicRework.playerHasRelicThatIsEnabled(player, RunicPyramid.ID)) {
                return;
            }

            if (!player.hasPower("Equilibrium")) {
                int tempSize = player.hand.size();
                for (int i = 0; i < tempSize; i++)
                    AbstractDungeon.actionManager.addToTop(new DiscardAction(player, null, player.hand.size(), true, true));
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
            Matcher matcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
            return LineFinder.findInOrder(ctBehavior, matcher);
        }
    }
}
