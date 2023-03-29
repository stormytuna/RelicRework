package relicrework;

import basemod.BaseMod;
import basemod.ModLabel;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;
import relicrework.util.GeneralUtils;
import relicrework.util.TextureLoader;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;

@SpireInitializer
public class RelicRework implements
        EditStringsSubscriber,
        PostInitializeSubscriber {
    private static final String resourcesFolder = "relicrework";
    private static final String CONFIG_PATH = "preferences/relicrework.cfg";
    private static final String defaultLanguage = "eng";
    public static ModInfo info;
    public static String modID;
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    private static TreeMap<String, HashSet<String>> config = new TreeMap<>();
    private static HashSet<String> disabled = new HashSet<>();

    static {
        loadModInfo();
    }

    Properties defaultSettings = new Properties();

    public RelicRework() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");
    }

    private static UIStrings getUIStrings(String uiName) {
        return CardCrawlGame.languagePack.getUIString(makeID(uiName));
    }

    private static void saveConfig() {
        String sConfig = new Gson().toJson(config);
        Gdx.files.local(CONFIG_PATH).writeString(sConfig, false, String.valueOf(StandardCharsets.UTF_8));
        logger.info("saved config=");
    }

    private static void loadConfig() {
        if (Gdx.files.local(CONFIG_PATH).exists()) {
            String sConfig = Gdx.files.local(CONFIG_PATH).readString(String.valueOf(StandardCharsets.UTF_8));
            logger.info("loaded config=" + sConfig);
            Type mapType = (new TypeToken<TreeMap<String, HashSet<String>>>() {
            }.getType());
            config = new Gson().fromJson(sConfig, mapType);
            disabled = config.get("disabled");
        } else {
            config.put("disabled", disabled);
        }
    }

    public static boolean isEnabled(String relicID) {
        return !disabled.contains(relicID);
    }

    private static float xPos(int index) {
        return 410.0F + 400.0F * (index / 10);
    }

    private static float yPos(int index) {
        return 660.0F - 50F * (index % 10);
    }

    public static String makeID(String id) {
        return modID + ":" + id;
    }

    public static boolean playerHasRelicThatIsEnabled(AbstractPlayer player, String relicID) {
        return player.hasRelic(relicID) && isEnabled(relicID);
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new RelicRework();
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString() {
        return Settings.language.name().toLowerCase();
    }

    //These methods are used to generate the correct filepaths to various parts of the resources folder.
    public static String localizationPath(String lang, String file) {
        return resourcesFolder + "/localization/" + lang + "/" + file;
    }

    public static String resourcePath(String file) {
        return resourcesFolder + "/" + file;
    }

    public static String characterPath(String file) {
        return resourcesFolder + "/character/" + file;
    }

    public static String powerPath(String file) {
        return resourcesFolder + "/powers/" + file;
    }

    public static String relicPath(String file) {
        return resourcesFolder + "/relics/" + file;
    }

    //This determines the mod's ID based on information stored by ModTheSpire.
    private static void loadModInfo() {
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo) -> {
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(RelicRework.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        } else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }

    @Override
    public void receivePostInitialize() {
        loadConfig();

        ModPanel configPanel = new ModPanel();
        String labelText = getUIStrings("DisabledChanges").TEXT[0];
        ModLabel disabledLabel = new ModLabel(labelText, 400.0F, 730.0F, configPanel, label -> {
        });
        configPanel.addUIElement(disabledLabel);

        String[] relicChoices = {
                CeramicFish.ID, MawBank.ID, Strawberry.ID, DarkstonePeriapt.ID, Pear.ID, StrikeDummy.ID, Girya.ID, Mango.ID, OldCoin.ID, Cauldron.ID, SnakeRing.ID, TinyChest.ID, DeadBranch.ID,
                StrangeSpoon.ID, Enchiridion.ID, BustedCrown.ID, LizardTail.ID, DollysMirror.ID, WingBoots.ID, Orrery.ID, Astrolabe.ID, Ectoplasm.ID, RunicPyramid.ID, VelvetChoker.ID, BlackBlood.ID,
                MarkOfPain.ID, FrozenCore.ID, Brimstone.ID, TinyHouse.ID, JuzuBracelet.ID
        };

        for (int i = 0; i < relicChoices.length; i++) {
            String relicID = relicChoices[i];
            ModLabeledToggleButton disableButton = new ModLabeledToggleButton(CardCrawlGame.languagePack.getRelicStrings(relicID).NAME, xPos(i), yPos(i), Settings.CREAM_COLOR, FontHelper.charDescFont, !isEnabled(relicID), configPanel, label -> {
            }, button -> {
                if (button.enabled) {
                    disabled.add(relicID);
                } else {
                    disabled.remove(relicID);
                }
                saveConfig();
            });
            configPanel.addUIElement(disableButton);
        }

        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, configPanel);
    }

    @Override
    public void receiveEditStrings() {
        /*
            First, load the default localization.
            Then, if the current language is different, attempt to load localization for that language.
            This results in the default localization being used for anything that might be missing.
            The same process is used to load keywords slightly below.
        */
        loadLocalization(defaultLanguage); //no except catching for default localization, you better have at least one that works.
        if (!defaultLanguage.equals(getLangString())) {
            try {
                loadLocalization(getLangString());
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
    }

    private void loadLocalization(String lang) {
        //While this does load every type of localization, most of these files are just outlines so that you can see how they're formatted.
        //Feel free to comment out/delete any that you don't end up using.
        BaseMod.loadCustomStringsFile(RelicStrings.class,
                localizationPath(lang, "RelicStrings.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class,
                localizationPath(lang, "UIStrings.json"));
    }
}
