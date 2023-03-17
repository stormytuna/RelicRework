package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.Pear;
import javassist.*;
import relicrework.RelicRework;

public class Pear_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings("Pear");
    private static final String ON_PLAYER_HEAL_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.changePear) { " +
            "       $1++; " +
            "   } " +
            "   return $1;" +
            "}";

    @SpirePatch(clz = Pear.class, method = SpirePatch.CONSTRUCTOR)
    public static class Pear_AddOnPlayerHeal {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();

            CtMethod onPlayerHeal = CtNewMethod.make(CtClass.intType, "onPlayerHeal", new CtClass[] { CtClass.intType }, null, ON_PLAYER_HEAL_METHOD_BODY, ctClass );
            ctClass.addMethod(onPlayerHeal);
        }
    }

    @SpirePatch(clz = Pear.class, method = "getUpdatedDescription")
    public static class Pear_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(Pear __instance) {
            return RelicRework.changePear ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
