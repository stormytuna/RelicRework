package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.DarkstonePeriapt;
import com.megacrit.cardcrawl.relics.Strawberry;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.*;
import relicrework.RelicRework;

public class DarkstonePeriapt_Patch {
    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings("Darkstone Periapt");
    private static final String onCardDrawMethodBody = "" +
            "{" +
            "   com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "   if (relicrework.RelicRework.changeDarkstonePeriapt && $1.type == com.megacrit.cardcrawl.cards.AbstractCard.CardType.CURSE && !player.hasPower(\"No Draw\")) {" +
            "       this.flash();" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.DrawCardAction(player, 1));" +
            "   }" +
            "}";

    @SpirePatch(clz = DarkstonePeriapt.class, method = "onObtainCard")
    public static class DarkstonePeriapt_RemoveOnObtainCard {
        @SpirePrefixPatch
        public static SpireReturn patch(DarkstonePeriapt __instance) {
            return RelicRework.changeDarkstonePeriapt ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = DarkstonePeriapt.class, method = SpirePatch.CONSTRUCTOR)
    public static class DarkstonePeriapt_AddOnCardDraw {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException, NotFoundException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool classPool = ctClass.getClassPool();
            CtClass ctAbstractCard = classPool.get(AbstractCard.class.getName());

            CtMethod onCardDraw = CtNewMethod.make(CtClass.voidType, "onCardDraw", new CtClass[] { ctAbstractCard }, null, onCardDrawMethodBody, ctClass);
            ctClass.addMethod(onCardDraw);
        }
    }

    @SpirePatch(clz = DarkstonePeriapt.class, method = "getUpdatedDescription")
    public static class DarkstonePeriapt_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(DarkstonePeriapt __instance) {
            return RelicRework.changeDarkstonePeriapt ? SpireReturn.Return(relicStrings.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
