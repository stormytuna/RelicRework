package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.OldCoin;
import javassist.*;
import relicrework.RelicRework;

public class OldCoin_Patch {
    private static final String ON_GAIN_GOLD_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Old Coin\")) {" +
            "       this.flash();" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(player, this));" +
            "       com.megacrit.cardcrawl.core.CardCrawlGame.goldGained += 5;" +
            "       player.gold += 5;" +
            "   }" +
            "}";

    @SpirePatch(clz = OldCoin.class, method = SpirePatch.CONSTRUCTOR)
    public static class OldCoint_AddOnGainGold {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();

            CtMethod onGainGold = CtNewMethod.make(CtClass.voidType, "onGainGold", null, null, ON_GAIN_GOLD_METHOD_BODY, ctClass);
            ctClass.addMethod(onGainGold);
        }
    }

    @SpirePatch(clz = OldCoin.class, method = "onEquip")
    public static class OldCoin_ReplaceOnEquip {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(OldCoin __instance) {
            if (!RelicRework.isEnabled(OldCoin.ID)) {
                return SpireReturn.Continue();
            }

            CardCrawlGame.sound.play("GOLD_GAIN");
            AbstractDungeon.player.gainGold(200);
            return SpireReturn.Return();
        }
    }
}
