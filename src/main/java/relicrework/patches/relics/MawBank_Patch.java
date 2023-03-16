package relicrework.patches.relics;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.common.GainGoldAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.MawBank;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.*;
import relicrework.RelicRework;

import java.lang.reflect.Field;

public class MawBank_Patch {
    private static final int GOLD_PER_FLOOR = 12;
    private static final RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings("MawBank");

    @SpirePatch(clz = MawBank.class, method = SpirePatch.CONSTRUCTOR)
    public static class MawBank_RawPatch {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool ctClassPool = ctClass.getClassPool();

            CtClass clickableRelic = ctClassPool.get("com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic");
            ctClass.addInterface(clickableRelic);

            CtMethod onRightClick = CtNewMethod.make(CtClass.voidType, "onRightClick", new CtClass[] { }, null, "{ if (relicrework.RelicRework.changeMawBank && !this.usedUp) { com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.gainGold(this.counter); this.usedUp(); this.counter = -2; } }", ctClass);
            ctClass.addMethod(onRightClick);
        }
    }

    @SpirePatch(clz = MawBank.class, method = SpirePatch.CONSTRUCTOR)
    public static class MawBank_InitializeCounterField {
        @SpirePostfixPatch
        public static void patch(MawBank __instance) {
            if (RelicRework.changeMawBank) {
                __instance.counter = 0;
            }
        }
    }

    @SpirePatch(clz = MawBank.class, method = "getUpdatedDescription")
    public static class MawBank_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(MawBank __instance) throws IllegalAccessException {
            if (!RelicRework.changeMawBank) {
                return SpireReturn.Continue();
            }

            Field goldStoredField = ReflectionHacks.getCachedField(__instance.getClass(), "goldStored");
            return SpireReturn.Return(relicStrings.DESCRIPTIONS[0]);
        }
    }

    @SpirePatch(clz = MawBank.class, method = "onSpendGold")
    public static class MawBank_RemoveOnSpendGold {
        @SpirePrefixPatch
        public static SpireReturn patch(MawBank __instance) {
            return RelicRework.changeMawBank ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MawBank.class, method = "onEnterRoom")
    public static class MawBank_ReplaceOnEnterRoom {
        @SpirePrefixPatch
        public static SpireReturn patch(MawBank __instance, AbstractRoom room) throws IllegalAccessException {
            if (!RelicRework.changeMawBank || __instance.usedUp) {
                return SpireReturn.Continue();
            }

            __instance.flash();
            __instance.counter += GOLD_PER_FLOOR;
            return SpireReturn.Return();
        }
    }
}
