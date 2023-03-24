package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.DollysMirror;
import javassist.*;
import relicrework.RelicRework;

public class DollysMirror_Patch {
    private static final String ON_PLAY_CARD_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"DollysMirror\") && !this.grayscale) {" +
            "       this.flash();" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, this));" +
            "       this.grayscale = true;" +
            "       com.megacrit.cardcrawl.cards.AbstractCard cardCopy = $1.makeSameInstanceOf();" +
            "       cardCopy.current_x = $1.current_x;" +
            "       cardCopy.current_y = $1.current_y;" +
            "       cardCopy.target_x = com.megacrit.cardcrawl.core.Settings.WIDTH / 2.0F - 300.0F * com.megacrit.cardcrawl.core.Settings.scale;" +
            "       cardCopy.target_y = com.megacrit.cardcrawl.core.Settings.HEIGHT / 2.0F;" +
            "       if ($2 != null) {" +
            "           cardCopy.calculateCardDamage($2);" +
            "       }" +
            "       cardCopy.purgeOnUse = true;" +
            "       com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addCardQueueItem(new com.megacrit.cardcrawl.cards.CardQueueItem(cardCopy, $2, $1.energyOnUse, true, true), true);" +
            "   }" +
            "}";
    private static final String ON_VICTORY_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"DollysMirror\")) {" +
            "       this.grayscale = false;" +
            "   }" +
            "}";

    @SpirePatch(clz = DollysMirror.class, method = SpirePatch.CONSTRUCTOR)
    public static class DollysMirror_AddMethods {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool classPool = ctClass.getClassPool();
            CtClass ctAbstractCardClass = classPool.get(AbstractCard.class.getName());
            CtClass ctAbstractMonsterClass = classPool.get(AbstractMonster.class.getName());

            CtMethod onPlayCard = CtNewMethod.make(CtClass.voidType, "onPlayCard", new CtClass[] { ctAbstractCardClass, ctAbstractMonsterClass }, null, ON_PLAY_CARD_METHOD_BODY, ctClass);
            ctClass.addMethod(onPlayCard);

            CtMethod onVictory = CtNewMethod.make(CtClass.voidType, "onVictory", null, null, ON_VICTORY_METHOD_BODY, ctClass);
            ctClass.addMethod(onVictory);
        }
    }

    @SpirePatch(clz = DollysMirror.class, method = "onEquip")
    public static class DollysMirror_RemoveOnEquip {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(DollysMirror __instance) {
            return RelicRework.isEnabled(DollysMirror.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }
}
