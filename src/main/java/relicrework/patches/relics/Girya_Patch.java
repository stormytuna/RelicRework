package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.relics.Girya;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import javassist.*;
import relicrework.RelicRework;

import java.util.ArrayList;

public class Girya_Patch {
    private static final String ON_PLAYER_END_TURN_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Girya\") && !this.grayscale) {" +
            "       this.flash();" +
            "       this.counter--;" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(player, player, new com.megacrit.cardcrawl.powers.StrengthPower(player, 1), 1));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(player, this));" +
            "       if (this.counter == 0) {" +
            "           this.counter = -1;" +
            "           this.grayscale = true;" +
            "       }" +
            "   }" +
            "}";
    private static final String ON_VICTORY_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Girya\")) {" +
            "       this.counter = -1;" +
            "       this.grayscale = false;" +
            "   }" +
            "}";
    private static final int COUNTER_STARTING_VALUE = 3;

    @SpirePatch(clz = Girya.class, method = SpirePatch.CONSTRUCTOR)
    public static class Girya_RawPatch {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException {
            CtClass ctGiryaClass = ctMethodToPatch.getDeclaringClass();

            CtMethod onPlayerEndTurnMethod = CtNewMethod.make(CtClass.voidType, "onPlayerEndTurn", null, null, ON_PLAYER_END_TURN_METHOD_BODY, ctGiryaClass);
            ctGiryaClass.addMethod(onPlayerEndTurnMethod);

            CtMethod onVictoryMethod = CtNewMethod.make(CtClass.voidType, "onVictory", null, null, ON_VICTORY_METHOD_BODY, ctGiryaClass);
            ctGiryaClass.addMethod(onVictoryMethod);
        }
    }

    @SpirePatch(clz = Girya.class, method = "atBattleStart")
    public static class Girya_ReplaceAtBattleStart {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(Girya __instance) {
            if (!RelicRework.isEnabled(Girya.ID)) {
                return SpireReturn.Continue();
            }

            __instance.counter = COUNTER_STARTING_VALUE;
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = Girya.class, method = "addCampfireOption")
    public static class Girya_RemoveAddCampfireOption {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(Girya __instance, ArrayList<AbstractCampfireOption> options) {
            return RelicRework.isEnabled(Girya.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = Girya.class, method = SpirePatch.CONSTRUCTOR)
    public static class Girya_ResetCounterField {
        @SpirePostfixPatch
        public static void patch(Girya __instance) {
            if (!RelicRework.isEnabled(Girya.ID)) {
                return;
            }

            __instance.counter = -1;
        }
    }
}
