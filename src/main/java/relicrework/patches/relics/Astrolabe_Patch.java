package relicrework.patches.relics;

import basemod.devcommands.relic.Relic;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.relics.Astrolabe;
import com.megacrit.cardcrawl.relics.Shovel;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import javassist.*;
import relicrework.RelicRework;
import relicrework.ui.campfire.AstrolabeTransformOption;

import java.util.ArrayList;

public class Astrolabe_Patch {
    private static final String ADD_CAMPFIRE_OPTION_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Astrolabe\")) {" +
            "       $1.add(new relicrework.ui.campfire.AstrolabeTransformOption());" +
            "       relicrework.RelicRework.logger.info(\"Added transform option!\");" +
            "   }" +
            "}";

    @SpirePatch(clz = Astrolabe.class, method = SpirePatch.CONSTRUCTOR)
    public static class Astrolabe_AddAddCampfireOptionMethod {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctClass = ctMethodToPatch.getDeclaringClass();
            ClassPool classPool = ctClass.getClassPool();
            CtClass ctArrayListClass = classPool.get(ArrayList.class.getName());
            // ctAbstractCampfireOptionClass = classPool.get(AbstractCampfireOption.class.getName());

            CtMethod ctAddCampfireOptionMethod = CtNewMethod.make(CtClass.voidType, "addCampfireOption", new CtClass[] { ctArrayListClass }, null, ADD_CAMPFIRE_OPTION_METHOD_BODY, ctClass);
            ctClass.addMethod(ctAddCampfireOptionMethod);
        }
    }
}
