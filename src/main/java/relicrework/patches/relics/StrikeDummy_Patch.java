package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.relics.StrikeDummy;
import relicrework.RelicRework;

public class StrikeDummy_Patch {
    private static final float EXTRA_DAMAGE = 3.0F;

    @SpirePatch(clz = StrikeDummy.class, method = "atDamageModify")
    public static class StrikeDummy_ReplaceAtDamageModify {
        @SpirePrefixPatch
        public static SpireReturn<Float> patch(StrikeDummy __instance, float damage, AbstractCard card) {
            boolean isBasicOrCommon = card.rarity == AbstractCard.CardRarity.BASIC || card.rarity == AbstractCard.CardRarity.COMMON;
            boolean isAttack = card.type == AbstractCard.CardType.ATTACK;
            if (!RelicRework.isEnabled(StrikeDummy.ID) || !isBasicOrCommon || !isAttack) {
                return SpireReturn.Continue();
            }

            return SpireReturn.Return(damage + EXTRA_DAMAGE);
        }
    }
}
