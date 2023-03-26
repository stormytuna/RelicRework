package relicrework.patches.relics;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireRawPatch;
import com.megacrit.cardcrawl.relics.Astrolabe;
import javassist.*;

import java.util.ArrayList;

public class Astrolabe_Patch {
    private static final String ADD_CAMPFIRE_OPTION_METHOD_BODY = "" +
            "{" +
            "   if (relicrework.RelicRework.isEnabled(\"Astrolabe\")) {" +
            "       $1.add(new relicrework.ui.campfire.AstrolabeTransformOption());" +
            "   }" +
            "}";

    @SpirePatch(clz = Astrolabe.class, method = SpirePatch.CONSTRUCTOR)
    public static class Astrolabe_AddAddCampfireOptionMethod {
        @SpireRawPatch
        public static void raw(CtBehavior ctMethodToPatch) throws NotFoundException, CannotCompileException {
            CtClass ctAstrolabeClass = ctMethodToPatch.getDeclaringClass();
            ClassPool classPool = ctAstrolabeClass.getClassPool();
            CtClass ctArrayListClass = classPool.get(ArrayList.class.getName());

            CtMethod ctAddCampfireOptionMethod = CtNewMethod.make(CtClass.voidType, "addCampfireOption", new CtClass[]{ctArrayListClass}, null, ADD_CAMPFIRE_OPTION_METHOD_BODY, ctAstrolabeClass);
            ctAstrolabeClass.addMethod(ctAddCampfireOptionMethod);
        }
    }
}
