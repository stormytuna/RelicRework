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
            "   if (relicrework.RelicRework.isEnabled(\"Mango\") && !$1.hasPower(\"Minion\")) {" +
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
            CtClass ctMangoClass = ctBehavior.getDeclaringClass();
            ClassPool classPool = ctMangoClass.getClassPool();
            CtClass ctAbstractMonsterClass = classPool.get(AbstractMonster.class.getName());

            CtMethod onMonsterDeathMethod = CtNewMethod.make(CtClass.voidType, "onMonsterDeath", new CtClass[]{ctAbstractMonsterClass}, null, ON_MONSTER_DEATH_METHOD_BODY, ctMangoClass);
            ctMangoClass.addMethod(onMonsterDeathMethod);
        }
    }

    @SpirePatch(clz = Mango.class, method = "getUpdatedDescription")
    public static class Mango_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(Mango __instance) throws IllegalAccessException {
            return RelicRework.isEnabled(Mango.ID) ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
