package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.MawBank;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.*;
import relicrework.RelicRework;

public class MawBank_Patch {
    private static final int GOLD_PER_FLOOR = 12;
    private static final RelicStrings RELIC_STRINGS = CardCrawlGame.languagePack.getRelicStrings(MawBank.ID);
    private static final String ON_RIGHT_CLICK_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.changeMawBank && !this.usedUp) { " +
            "       com.megacrit.cardcrawl.core.CardCrawlGame.sound.play(\"GOLD_GAIN\");" +
            "       com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.gainGold(this.counter);" +
            "       this.usedUp();" +
            "       this.counter = -2;" +
            "   }" +
            "}";

    @SpirePatch(clz = MawBank.class, method = SpirePatch.CONSTRUCTOR)
    public static class MawBank_RawPatch {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool ctClassPool = ctClass.getClassPool();

            CtClass clickableRelic = ctClassPool.get("com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic");
            ctClass.addInterface(clickableRelic);

            CtMethod onRightClick = CtNewMethod.make(CtClass.voidType, "onRightClick", null, null, ON_RIGHT_CLICK_METHOD_BODY, ctClass);
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

    @SpirePatch(clz = MawBank.class, method = "onSpendGold")
    public static class MawBank_RemoveOnSpendGold {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(MawBank __instance) {
            return RelicRework.changeMawBank ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MawBank.class, method = "onEnterRoom")
    public static class MawBank_ReplaceOnEnterRoom {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(MawBank __instance, AbstractRoom room) throws IllegalAccessException {
            if (!RelicRework.changeMawBank || __instance.usedUp) {
                return SpireReturn.Continue();
            }

            __instance.flash();
            __instance.counter += GOLD_PER_FLOOR;
            return SpireReturn.Return();
        }
    }

    @SpirePatch(clz = MawBank.class, method = "getUpdatedDescription")
    public static class MawBank_ReplaceGetUpdatedDescription {
        @SpirePrefixPatch
        public static SpireReturn<String> patch(MawBank __instance) throws IllegalAccessException {
            return RelicRework.changeMawBank ? SpireReturn.Return(RELIC_STRINGS.DESCRIPTIONS[0]) : SpireReturn.Continue();
        }
    }
}
