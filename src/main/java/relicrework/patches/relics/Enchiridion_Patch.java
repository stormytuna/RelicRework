package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Discovery;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.Enchiridion;
import relicrework.RelicRework;

public class Enchiridion_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(Enchiridion.ID);

    @SpirePatch(clz = Enchiridion.class, method = "atPreBattle")
    public static class Enchiridion_ReplaceAtPreBattle {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(Enchiridion __instance) {
            if (!RelicRework.changeEnchiridion) {
                return SpireReturn.Continue();
            }

            __instance.flash();
            AbstractDungeon.actionManager.addToBottom(new DiscoveryAction(AbstractCard.CardType.POWER, 1));
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = Enchiridion.class, method = "getUpdatedDescription")
    public static class Enchiridion_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(Enchiridion __instance) {
            return RelicRework.changeEnchiridion ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
