package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.relics.LizardTail;
import javassist.*;
import relicrework.RelicRework;

public class LizardTail_Patch {
    private static final String AT_TURN_START_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Lizard Tail\")) {" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       this.flash();" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(player, this));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(player, player, new com.megacrit.cardcrawl.powers.PlatedArmorPower(player, 1), 1));" +
            "   }" +
            "}";

    @SpirePatch(clz = LizardTail.class, method = SpirePatch.CONSTRUCTOR)
    public static class LizardTail_AddMethods {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctLizardTailClass = ctMethodToPatch.getDeclaringClass();

            CtMethod atTurnStartMethod = CtNewMethod.make(CtClass.voidType, "atTurnStart", null, null, AT_TURN_START_METHOD_BODY, ctLizardTailClass);
            ctLizardTailClass.addMethod(atTurnStartMethod);
        }
    }

    @SpirePatch(clz = LizardTail.class, method = "onTrigger")
    public static class LizardTail_RemoveOnTrigger {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(LizardTail __instance) {
            return RelicRework.isEnabled(LizardTail.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }
}
