package relicrework.patches.localization;

import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import relicrework.RelicRework;

import java.util.Map;

public class LocalizedStrings_Patch {
    private static Map<String, RelicStrings> relics;

    @SpirePatch(clz = LocalizedStrings.class, method = SpirePatch.CONSTRUCTOR)
    public static class LocalizedStrings_InitRelicStrings {
        @SpirePostfixPatch
        public static void patch(LocalizedStrings __instance) {
            LocalizedStrings_Patch.relics = ReflectionHacks.getPrivate(__instance, LocalizedStrings.class, "relics");
        }
    }

    @SpirePatch(clz = LocalizedStrings.class, method = "getRelicStrings", paramtypez = {String.class})
    public static class LocalizedStrings_ReplaceRelicStrings {
        @SpirePrefixPatch
        public static SpireReturn<RelicStrings> patch(LocalizedStrings __instance, String relicName) {
            if (RelicRework.isEnabled(relicName)) {
                RelicStrings newRelicStrings = LocalizedStrings_Patch.relics.get(RelicRework.makeID(relicName));
                if (newRelicStrings != null) {
                    return SpireReturn.Return(newRelicStrings);
                }
            }

            return SpireReturn.Continue();
        }
    }
}
