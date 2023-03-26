package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.relics.WingBoots;
import javassist.*;

public class WingBoots_Patch {
    private static final String ON_PLAYER_END_TURN_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"WingedGreaves\") && !this.grayscale) {" +
            "       this.flash();" +
            "       this.counter--;" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(player, player, new com.megacrit.cardcrawl.powers.DexterityPower(player, 1), 1));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(player, this));" +
            "       if (this.counter == 0) {" +
            "           this.counter = -1;" +
            "           this.grayscale = true;" +
            "       }" +
            "   }" +
            "}";
    private static final String ON_VICTORY_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"WingedGreaves\")) {" +
            "       this.counter = -1;" +
            "       this.grayscale = false;" +
            "   }" +
            "}";
    private static final String AT_BATTLE_START_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"WingedGreaves\")) {" +
            "       this.counter = 3;" +
            "   }" +
            "}";

    @SpirePatch(clz = WingBoots.class, method = SpirePatch.CONSTRUCTOR)
    public static class WingBoots_AddMethods {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException {
            CtClass ctWingBootsClass = ctMethodToPatch.getDeclaringClass();

            CtMethod onPlayerEndTurnMethod = CtNewMethod.make(CtClass.voidType, "onPlayerEndTurn", null, null, ON_PLAYER_END_TURN_METHOD_BODY, ctWingBootsClass);
            ctWingBootsClass.addMethod(onPlayerEndTurnMethod);

            CtMethod onVictoryMethod = CtNewMethod.make(CtClass.voidType, "onVictory", null, null, ON_VICTORY_METHOD_BODY, ctWingBootsClass);
            ctWingBootsClass.addMethod(onVictoryMethod);

            CtMethod atBattleStartMethod = CtNewMethod.make(CtClass.voidType, "atBattleStart", null, null, AT_BATTLE_START_METHOD_BODY, ctWingBootsClass);
            ctWingBootsClass.addMethod(atBattleStartMethod);
        }
    }

    @SpirePatch(clz = WingBoots.class, method = SpirePatch.CONSTRUCTOR)
    public static class WingBoots_SetCounter {
        @SpirePostfixPatch
        public static void patch(WingBoots __instance) {
            __instance.counter = -1;
        }
    }
}
