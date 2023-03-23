package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.BlackStar;
import com.megacrit.cardcrawl.relics.BustedCrown;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import relicrework.RelicRework;

public class BustedCrown_Patch {
    @SpirePatch(clz = BustedCrown.class, method = "changeNumberOfCardsInReward")
    public static class BustedCrown_ReplaceChangeNumberOfCardsInReward {
        @SpirePrefixPatch
        public static SpireReturn<Integer> patch(BustedCrown __instance, int numberOfCards) {
            if (AbstractDungeon.getCurrRoom() instanceof MonsterRoomElite && RelicRework.isEnabled(BustedCrown.ID)) {
                return SpireReturn.Return(numberOfCards);
            }

            return SpireReturn.Continue();
        }
    }
}
