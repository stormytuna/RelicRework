package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.BlackBlood;
import javassist.*;
import relicrework.RelicRework;

public class BlackBlood_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(BlackBlood.ID);
    private static final String ON_MONSTER_DEATH_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Black Blood\") && !$1.hasPower(\"Minion\")) {" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       com.megacrit.cardcrawl.dungeons.AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.common.HealAction(player, player, 4));" +
            "   }" +
            "}";

    @SpirePatch(clz = BlackBlood.class, method = SpirePatch.CONSTRUCTOR)
    public static class BlackBlood_AddOnMonsterDeathMethod {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException, NotFoundException {
            CtClass ctBlackBloodClass = ctMethodToPatch.getDeclaringClass();
            ClassPool classPool = ctBlackBloodClass.getClassPool();
            CtClass ctAbstractMonsterClass = classPool.get(AbstractMonster.class.getName());

            CtMethod ctOnMonsterDeathMethod = CtNewMethod.make(CtClass.voidType, "onMonsterDeath", new CtClass[]{ctAbstractMonsterClass}, null, ON_MONSTER_DEATH_METHOD_BODY, ctBlackBloodClass);
            ctBlackBloodClass.addMethod(ctOnMonsterDeathMethod);
        }
    }

    @SpirePatch(clz = BlackBlood.class, method = "onVictory")
    public static class BlackBlood_RemoveOnVictory {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(BlackBlood __instance) {
            return RelicRework.isEnabled(BlackBlood.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = BlackBlood.class, method = "getUpdatedDescription")
    public static class BlackBlood_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(BlackBlood __instance) {
            return RelicRework.isEnabled(BlackBlood.ID) ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
