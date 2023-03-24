package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.Cauldron;
import com.megacrit.cardcrawl.rewards.RewardItem;
import relicrework.RelicRework;
import relicrework.util.GeneralUtils;

public class Cauldron_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(Cauldron.ID);
    private static final int POTION_COMMON_CHANCE = 45;
    private static final int POTION_UNCOMMON_CHANCE = 30;

    @SpirePatch(clz = Cauldron.class, method = "onEquip")
    public static class Cauldron_ReplaceOnEquip {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(Cauldron __instance) {
            if (!RelicRework.isEnabled(Cauldron.ID)) {
                return SpireReturn.Continue();
            }

            // Add potions
            AbstractDungeon.getCurrRoom().addPotionToRewards(GeneralUtils.getRandomPotionByRarity(AbstractPotion.PotionRarity.COMMON));
            AbstractDungeon.getCurrRoom().addPotionToRewards(GeneralUtils.getRandomPotionByRarity(AbstractPotion.PotionRarity.UNCOMMON));
            AbstractDungeon.getCurrRoom().addPotionToRewards(GeneralUtils.getRandomPotionByRarity(AbstractPotion.PotionRarity.RARE));

            // Open rewards screen
            AbstractDungeon.combatRewardScreen.open(RELIC_STRINGS.DESCRIPTIONS[1]);
            AbstractDungeon.getCurrRoom().rewardPopOutTimer = 0.5F;

            // Remove card from rewards
            for (int i = 0; i < AbstractDungeon.combatRewardScreen.rewards.size(); i++) {
                RewardItem reward = AbstractDungeon.combatRewardScreen.rewards.get(i);
                if (reward.type == RewardItem.RewardType.CARD) {
                    AbstractDungeon.combatRewardScreen.rewards.remove(i);
                    break;
                }
            }

            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "returnRandomPotion", paramtypez = { boolean.class })
    public static class AbstractDungeon_ReplaceReturnRandomPotion {
        @SpirePrefixPatch
        public static SpireReturn<AbstractPotion> patch(boolean limited) {
            if (AbstractDungeon.player.hasRelic(Cauldron.ID) && RelicRework.isEnabled(Cauldron.ID)) {
                int roll = AbstractDungeon.potionRng.random(0, 99);
                if (roll < PotionHelper.POTION_COMMON_CHANCE)
                    return SpireReturn.Return(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.COMMON, limited));
                if (roll < PotionHelper.POTION_UNCOMMON_CHANCE + PotionHelper.POTION_COMMON_CHANCE)
                    return SpireReturn.Return(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.UNCOMMON, limited));
                return SpireReturn.Return(AbstractDungeon.returnRandomPotion(AbstractPotion.PotionRarity.RARE, limited));
            }

            return SpireReturn.Continue();
        }
    }
}
