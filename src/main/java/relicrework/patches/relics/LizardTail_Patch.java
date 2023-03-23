package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostAction;
import com.megacrit.cardcrawl.actions.common.ReduceCostForTurnAction;
import com.megacrit.cardcrawl.actions.unique.ForethoughtAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.LizardTail;
import javassist.*;
import relicrework.RelicRework;

public class LizardTail_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(LizardTail.ID);
    private static final String ON_USE_CARD_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.changeLizardTail && !this.grayscale) {" +
            "       this.counter++;" +
            "       this.beginPulse();" +
            "       if (this.counter >= 6) {" +
            "           this.flash();" +
            "           this.counter = -1;" +
            "           this.grayscale = true;" +
            "           this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(com.megacrit.cardcrawl.dungeons.AbstractDungeon.player, this));" +
            "           this.addToBot(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(1));" +
            "       }" +
            "   }" +
            "}";
    private static final String AT_TURN_START_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.changeLizardTail) {" +
            "       this.counter = 0;" +
            "       this.grayscale = false;" +
            "       this.beginPulse();" +
            "   }" +
            "}";
    @SpirePatch(clz = LizardTail.class, method = SpirePatch.CONSTRUCTOR)
    public static class LizardTail_AddMethods {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool classPool = ctClass.getClassPool();
            CtClass ctAbstractCard = classPool.get(AbstractCard.class.getName());
            CtClass ctUseCardAction = classPool.get(UseCardAction.class.getName());

            CtMethod onUseCardMethod = CtNewMethod.make(CtClass.voidType, "onUseCard", new CtClass[] { ctAbstractCard, ctUseCardAction }, null, ON_USE_CARD_METHOD_BODY, ctClass);
            ctClass.addMethod(onUseCardMethod);

            CtMethod atTurnStartMethod = CtNewMethod.make(CtClass.voidType, "atTurnStart", null, null, AT_TURN_START_METHOD_BODY, ctClass);
            ctClass.addMethod(atTurnStartMethod);
        }
    }

    @SpirePatch(clz = LizardTail.class, method = "onTrigger")
    public static class LizardTail_RemoveOnTrigger {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(LizardTail __instance) {
            return RelicRework.changeLizardTail ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = LizardTail.class, method = "getUpdatedDescription")
    public static class LizardTail_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(LizardTail __instance) {
            return RelicRework.changeLizardTail ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
