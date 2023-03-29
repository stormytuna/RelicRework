package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.helpers.EventHelper.RoomResult;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.JuzuBracelet;
import javassist.*;
import relicrework.RelicRework;

public class JuzuBracelet_Patch {
    private static final String AT_TURN_START_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Juzu Bracelet\") && this.counter > 0) {" +
            "       this.flash();" +
            "       this.counter--;" +
            "       com.megacrit.cardcrawl.characters.AbstractPlayer player = com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.GainBlockAction(player, 4));" +
            "       this.addToBot(new com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction(player, this));" +
            "       if (this.counter == 0) {" +
            "           this.counter = -1;" +
            "           this.grayscale = true;" +
            "       }" +
            "   }" +
            "}";
    private static final String ON_VICTORY_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Juzu Bracelet\")) {" +
            "       this.counter = -1;" +
            "       this.grayscale = false;" +
            "   }" +
            "}";
    private static final String AT_BATTLE_START_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Juzu Bracelet\")) {" +
            "       this.counter = 3;" +
            "       this.grayscale = false;" +
            "   }" +
            "}";

    @SpirePatch(clz = JuzuBracelet.class, method = SpirePatch.CONSTRUCTOR)
    public static class JuzuBracelet_AddMethods {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws CannotCompileException {
            CtClass ctJuzuBraceletClass = ctMethodToPatch.getDeclaringClass();

            CtMethod atTurnStartMethod = CtNewMethod.make(CtClass.voidType, "atTurnStart", null, null, AT_TURN_START_METHOD_BODY, ctJuzuBraceletClass);
            ctJuzuBraceletClass.addMethod(atTurnStartMethod);

            CtMethod onVictoryMethod = CtNewMethod.make(CtClass.voidType, "onVictory", null, null, ON_VICTORY_METHOD_BODY, ctJuzuBraceletClass);
            ctJuzuBraceletClass.addMethod(onVictoryMethod);

            CtMethod atBattleStartMethod = CtNewMethod.make(CtClass.voidType, "atBattleStart", null, null, AT_BATTLE_START_METHOD_BODY, ctJuzuBraceletClass);
            ctJuzuBraceletClass.addMethod(atBattleStartMethod);
        }
    }

    @SpirePatch(clz = EventHelper.class, method = "roll", paramtypez = {Random.class})
    public static class EventHelper_RemoveJuzuBraceletEffect {
        @SpireInsertPatch(locator = Locator.class, localvars = {"choice"})
        public static void patch(Random eventRng, @ByRef RoomResult[] choice) {
            AbstractPlayer player = AbstractDungeon.player;

            if (!RelicRework.playerHasRelicThatIsEnabled(player, JuzuBracelet.ID)) {
                return;
            }

            if (player.hasRelic(JuzuBracelet.ID)) {
                AbstractRelic juzuBracelet = player.getRelic(JuzuBracelet.ID);
                juzuBracelet.flashTimer = 0.0F; // Undoes 'juzuBracelet.flash()'
                choice[0] = RoomResult.MONSTER;
                RelicRework.logger.info("Prevented juzu effect!!");
            }
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctBehavior) throws PatchingException, CannotCompileException {
            Matcher matcher = new Matcher.FieldAccessMatcher(EventHelper.class, "MONSTER_CHANCE");
            int[] lines = LineFinder.findAllInOrder(ctBehavior, matcher);
            return new int[]{lines[2]};
        }
    }
}
