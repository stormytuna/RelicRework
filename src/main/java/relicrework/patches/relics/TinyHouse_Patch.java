package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.TinyHouse;
import javassist.*;
import relicrework.RelicRework;

public class TinyHouse_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(TinyHouse.ID);
    private static final String AT_BATTLE_START_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Tiny House\")) {" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       this.flash();" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(player, this));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.DrawCardAction(player, 1));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.GainEnergyAction(1));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.GainBlockAction(player, 8));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(player, player, new com.megacrit.cardcrawl.powers.StrengthPower(player, 2), 2));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(player, player, new com.megacrit.cardcrawl.powers.LoseStrengthPower(player, 2), 2));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(player, player, new com.megacrit.cardcrawl.powers.DexterityPower(player, 2), 2));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.ApplyPowerAction(player, player, new com.megacrit.cardcrawl.powers.LoseDexterityPower(player, 2), 2));" +
            "   }" +
            "}";

    @SpirePatch(clz = TinyHouse.class, method = SpirePatch.CONSTRUCTOR)
    public static class TinyHouse_AddAtBattleStartMethod {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToMatch) throws CannotCompileException {
            CtClass ctTinyHouseClass = ctMethodToMatch.getDeclaringClass();

            CtMethod ctAtBattleStartMethod = CtNewMethod.make(CtClass.voidType, "atBattleStart", null, null, AT_BATTLE_START_METHOD_BODY, ctTinyHouseClass);
            ctTinyHouseClass.addMethod(ctAtBattleStartMethod);
        }
    }

    @SpirePatch(clz = TinyHouse.class, method = "onEquip")
    public static class TinyHouse_RemoveOnEquip {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(TinyHouse __instance) {
            return RelicRework.isEnabled(TinyHouse.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = TinyHouse.class, method = "getUpdatedDescription")
    public static class TinyHouse_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(TinyHouse __instance) {
            return RelicRework.isEnabled(TinyHouse.ID) ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
