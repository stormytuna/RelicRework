package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.Strawberry;
import javassist.*;
import relicrework.RelicRework;

public class Strawberry_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(Strawberry.ID);
    private static final String ON_VICTORY_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.changeStrawberry) {" +
            "       this.flash();" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       this.addToTop(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(player, this));" +
            "       if (player.currentHealth > 0) {" +
            "           player.heal(1);" +
            "       }" +
            "   }" +
            "}";

    @SpirePatch(clz = Strawberry.class, method = SpirePatch.CONSTRUCTOR)
    public static class Strawberry_AddOnVictoryMethod {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();

            CtMethod onVictory = CtNewMethod.make(CtClass.voidType, "onVictory", null, null, ON_VICTORY_METHOD_BODY, ctClass);
            ctClass.addMethod(onVictory);
        }
    }

    @SpirePatch(clz = Strawberry.class, method = "getUpdatedDescription")
    public static class Strawberry_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(Strawberry __instance) {
            return RelicRework.changeStrawberry ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
