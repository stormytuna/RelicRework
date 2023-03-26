package relicrework.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.VelvetChoker;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import relicrework.RelicRework;

public class VelvetChoker_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(VelvetChoker.ID);

    @SpirePatch(clz = VelvetChoker.class, method = "atBattleStart")
    public static class VelvetChoker_RemoveAtBattleStart {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(VelvetChoker __instance) {
            return RelicRework.isEnabled(VelvetChoker.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = VelvetChoker.class, method = "atTurnStart")
    public static class VelvetChoker_RemoveAtTurnStart {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(VelvetChoker __instance) {
            return RelicRework.isEnabled(VelvetChoker.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = VelvetChoker.class, method = "onPlayCard", paramtypez = {AbstractCard.class, AbstractMonster.class})
    public static class VelvetChoker_RemoveOnPlayCard {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(VelvetChoker __instance, AbstractCard card, AbstractMonster monster) {
            return RelicRework.isEnabled(VelvetChoker.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = VelvetChoker.class, method = "canPlay", paramtypez = {AbstractCard.class})
    public static class VelvetChoker_RemoveCanPlay {
        @SpirePrefixPatch
        public static SpireReturn<Boolean> patch(VelvetChoker __instance, AbstractCard card) {
            return RelicRework.isEnabled(VelvetChoker.ID) ? SpireReturn.Return(true) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = VelvetChoker.class, method = "getUpdatedDescription")
    public static class VelvetChoker_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(VelvetChoker __instance) {
            return RelicRework.isEnabled(VelvetChoker.ID) ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = EnergyPanel.class, method = "update")
    public static class EnergyPanel_CapEnergy {
        @SpirePostfixPatch
        public static void patch(EnergyPanel __instance) {
            if (!RelicRework.playerHasRelicThatIsEnabled(AbstractDungeon.player, VelvetChoker.ID)) {
                return;
            }

            int totalCount = ReflectionHacks.getPrivate(__instance, __instance.getClass(), "totalCount");
            if (totalCount > AbstractDungeon.player.energy.energy) {
                ReflectionHacks.setPrivate(__instance, __instance.getClass(), "totalCount", AbstractDungeon.player.energy.energy);
            }
        }
    }
}
