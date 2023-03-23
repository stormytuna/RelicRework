package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.CeramicFish;
import com.megacrit.cardcrawl.relics.TinyChest;
import com.megacrit.cardcrawl.rewards.chests.AbstractChest;
import com.megacrit.cardcrawl.rewards.chests.LargeChest;
import com.megacrit.cardcrawl.rewards.chests.MediumChest;
import com.megacrit.cardcrawl.rewards.chests.SmallChest;
import relicrework.RelicRework;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.random.Random;

public class TinyChest_Patch {
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(TinyChest.ID);
    private static final int MEDIUM_CHEST_CHANCE = 50;
    private static final int LARGE_CHEST_CHANCE = 33;

    @SpirePatch(clz = TinyChest.class, method = "onEquip")
    public static class TinyChest_PostfixOnEquip {
        @SpirePostfixPatch
        public static void patch(TinyChest __instance) {
            __instance.counter = -1;
        }
    }

    @SpirePatch(clz = TinyChest.class, method = "getUpdatedDescription")
    public static class TinyChest_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(TinyChest __instance) {
            return RelicRework.isEnabled(TinyChest.ID) ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = EventHelper.class, method = "roll", paramtypez = { com.megacrit.cardcrawl.random.Random.class })
    public static class EventHelper_UndoTinyChestCounterIncrement {
        @SpirePostfixPatch
        public static void patch(Random eventRng) {
            if (AbstractDungeon.player.hasRelic(TinyChest.ID) && RelicRework.isEnabled(TinyChest.ID)) {
                AbstractRelic tinyChestRelic = AbstractDungeon.player.getRelic(TinyChest.ID);
                tinyChestRelic.counter = -1;
            }
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "getRandomChest")
    public static class AbstractDungeon_ReplaceGetRandomChest {
        @SpirePrefixPatch
        public static SpireReturn<AbstractChest> patch() {
            if (!RelicRework.isEnabled(TinyChest.ID) || !AbstractDungeon.player.hasRelic("Tiny Chest")) {
                return SpireReturn.Continue();
            }

            int roll = AbstractDungeon.treasureRng.random(0, 99);
            if (roll < MEDIUM_CHEST_CHANCE) {
                RelicRework.logger.info("Medium chest!!");
                return SpireReturn.Return(new MediumChest());
            } else if (roll < MEDIUM_CHEST_CHANCE + LARGE_CHEST_CHANCE) {
                RelicRework.logger.info("Large chest!!");
                return SpireReturn.Return(new LargeChest());
            }

            RelicRework.logger.info("Small chest!!");
            return SpireReturn.Return(new SmallChest());
        }
    }
}
