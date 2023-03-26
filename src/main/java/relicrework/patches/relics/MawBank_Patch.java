package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.relics.MawBank;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import javassist.*;
import relicrework.RelicRework;

public class MawBank_Patch {
    private static final String ON_RIGHT_CLICK_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"MawBank\") && !this.usedUp) { " +
            "       com.megacrit.cardcrawl.core.CardCrawlGame.sound.play(\"GOLD_GAIN\");" +
            "       com.megacrit.cardcrawl.dungeons.AbstractDungeon.player.gainGold(this.counter);" +
            "       this.usedUp();" +
            "       this.counter = -2;" +
            "   }" +
            "}";
    private static final int GOLD_PER_FLOOR = 12;

    @SpirePatch(clz = MawBank.class, method = SpirePatch.CONSTRUCTOR)
    public static class MawBank_RawPatch {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctMawBankClass = ctMethodToPatch.getDeclaringClass();
            ClassPool ctClassPool = ctMawBankClass.getClassPool();

            CtClass clickableRelicClass = ctClassPool.get("com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic");
            ctMawBankClass.addInterface(clickableRelicClass);

            CtMethod onRightClickMethod = CtNewMethod.make(CtClass.voidType, "onRightClick", null, null, ON_RIGHT_CLICK_METHOD_BODY, ctMawBankClass);
            ctMawBankClass.addMethod(onRightClickMethod);
        }
    }

    @SpirePatch(clz = MawBank.class, method = SpirePatch.CONSTRUCTOR)
    public static class MawBank_InitializeCounterField {
        @SpirePostfixPatch
        public static void patch(MawBank __instance) {
            if (!RelicRework.isEnabled(MawBank.ID)) {
                return;
            }

            __instance.counter = 0;
        }
    }

    @SpirePatch(clz = MawBank.class, method = "onSpendGold")
    public static class MawBank_RemoveOnSpendGold {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(MawBank __instance) {
            return RelicRework.isEnabled(MawBank.ID) ? SpireReturn.Return() : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = MawBank.class, method = "onEnterRoom")
    public static class MawBank_ReplaceOnEnterRoom {
        @SpirePrefixPatch
        public static SpireReturn<Void> patch(MawBank __instance, AbstractRoom room) throws IllegalAccessException {
            if (!RelicRework.isEnabled(MawBank.ID) || __instance.usedUp) {
                return SpireReturn.Continue();
            }

            __instance.flash();
            __instance.counter += GOLD_PER_FLOOR;
            return SpireReturn.Return();
        }
    }
}
