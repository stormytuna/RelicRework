package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.StrikeDummy;
import relicrework.RelicRework;

public class StrikeDummy_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings("Strike Dummy");

    @SpirePatch(clz = StrikeDummy.class, method = "atDamageModify")
    public static class StrikeDummy_ReplaceAtDamageModify {
        @SpirePrefixPatch
        public static SpireReturn<Float> patch(StrikeDummy __instance, float damage, AbstractCard card) {
            boolean isBasicOrCommon = card.rarity == AbstractCard.CardRarity.BASIC || card.rarity == AbstractCard.CardRarity.COMMON;
            boolean isAttack = card.type == AbstractCard.CardType.ATTACK;
            if (!RelicRework.changeStrikeDummy || !isBasicOrCommon || !isAttack) {
                return RelicRework.changeStrikeDummy ? SpireReturn.Return(damage) : SpireReturn.Continue();
            }

            return SpireReturn.Return(damage + 3.0F);
        }
    }

    @SpirePatch(clz = StrikeDummy.class, method = "getUpdatedDescription")
    public static class StrikeDummy_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(StrikeDummy __instance) {
            return RelicRework.changeStrikeDummy ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
