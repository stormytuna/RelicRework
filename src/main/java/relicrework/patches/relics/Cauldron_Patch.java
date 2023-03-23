package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.relics.Cauldron;
import com.megacrit.cardcrawl.rewards.RewardItem;
import relicrework.RelicRework;
import relicrework.util.GeneralUtils;

public class Cauldron_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(Cauldron.ID);
    private static final int NUM_POTIONS = 3;

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

            // Grant potion slots
            AbstractPlayer player = AbstractDungeon.player;
            player.potionSlots += 1;
            player.potions.add(new PotionSlot(player.potionSlots - 1));

            return SpireReturn.Return();
        }
    }
}
