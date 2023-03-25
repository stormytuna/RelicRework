package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.relics.FrozenCore;
import javassist.*;
import relicrework.RelicRework;

public class FrozenCore_Patch {
    private static final String AT_TURN_START_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"FrozenCore\")) {" +
            "       this.flash();" +
            "       com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.channelOrb(new com.megacrit.cardcrawl.orbs.Frost());" +
            "   }" +
            "}";

    @SpirePatch(clz = FrozenCore.class, method = SpirePatch.CONSTRUCTOR)
    public static class FrozenCore_AddAtTurnStartMethod {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();

            CtMethod ctAtTurnStartMethod = CtNewMethod.make(CtClass.voidType, "atTurnStart", null, null, AT_TURN_START_METHOD_BODY, ctClass);
            ctClass.addMethod(ctAtTurnStartMethod);
        }
    }

    @SpirePatch(clz = FrozenCore.class, method = "onPlayerEndTurn")
    public static class FrozenCore_RemoveOnPlayerEndTurn {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(FrozenCore __instance) {
            return RelicRework.isEnabled(FrozenCore.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }
}
