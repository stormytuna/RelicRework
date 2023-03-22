package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.Mango;
import javassist.*;
import relicrework.RelicRework;

public class Mango_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(Mango.ID);
    private static final String ON_MONSTER_DEATH_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.changeMango && !$1.hasPower(\"Minion\")) {" +
            "       this.flash();" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.HealAction(player, player, 2));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(player, this));" +
            "   }" +
            "}";

    @SpirePatch(clz = Mango.class, method = SpirePatch.CONSTRUCTOR)
    public static class Mango_AddOnMonsterDeath {
        @SpireRawPatch
        public static void raw(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctBehavior.getDeclaringClass();
            ClassPool classPool = ctClass.getClassPool();
            CtClass ctAbstractMonster = classPool.get(AbstractMonster.class.getName());

            CtMethod onMonsterDeath = CtNewMethod.make(CtClass.voidType, "onMonsterDeath", new CtClass[] { ctAbstractMonster }, null, ON_MONSTER_DEATH_METHOD_BODY, ctClass);
            ctClass.addMethod(onMonsterDeath);
        }
    }

    @SpirePatch(clz = Mango.class, method = "getUpdatedDescription")
    public static class Mango_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(Mango __instance) throws IllegalAccessException {
            return RelicRework.changeMango ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
