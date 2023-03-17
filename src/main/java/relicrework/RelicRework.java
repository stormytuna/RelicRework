package relicrework;

import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.sun.org.apache.xpath.internal.operations.Bool;
import relicrework.util.GeneralUtils;
import relicrework.util.TextureLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.ModInfo;
import com.evacipated.cardcrawl.modthespire.Patcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.scannotation.AnnotationDB;

import java.io.IOException;
import java.util.*;

@SpireInitializer
public class RelicRework implements
        EditStringsSubscriber,
        PostInitializeSubscriber {
    public static ModInfo info;
    public static String modID;
    static { loadModInfo(); }
    public static final Logger logger = LogManager.getLogger(modID); //Used to output to the console.
    private static final String resourcesFolder = "relicrework";
    Properties defaultSettings = new Properties();

    public static boolean changeCeramicFish = false;
    public static boolean changeMawBank = false;
    public static boolean changeStrawberry = false;
    public static boolean changeDarkstonePeriapt = false;
    public static boolean changePear = false;
    public static boolean changeStrikeDummy = false;
    public static boolean changeGirya = false;
    public static boolean changeMango = false;

    public static String makeID(String id) {
        return modID + ":" + id;
    }

    //This will be called by ModTheSpire because of the @SpireInitializer annotation at the top of the class.
    public static void initialize() {
        new RelicRework();
    }

    public RelicRework() {
        BaseMod.subscribe(this); //This will make BaseMod trigger all the subscribers at their appropriate times.
        logger.info(modID + " subscribed to BaseMod.");

        defaultSettings.setProperty("change-ceramic-fish", Boolean.toString(true));
        defaultSettings.setProperty("change-maw-bank", Boolean.toString(true));
        defaultSettings.setProperty("change-strawberry", Boolean.toString(true));
        defaultSettings.setProperty("change-darkstone-periapt", Boolean.toString(true));
        defaultSettings.setProperty("change-pear", Boolean.toString(true));
        defaultSettings.setProperty("change-strike-dummy", Boolean.toString(true));
        defaultSettings.setProperty("change-girya", Boolean.toString(true));
        defaultSettings.setProperty("change-mango", Boolean.toString(true));
        try {
            SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
            config.load();

            changeCeramicFish = config.getBool("change-ceramic-fish");
            changeMawBank = config.getBool("change-maw-bank");
            changeStrawberry = config.getBool("change-strawberry");
            changeDarkstonePeriapt = config.getBool("change-darkstone-periapt");
            changePear = config.getBool("change-pear");
            changeStrikeDummy = config.getBool("change-strike-dummy");
            changeGirya = config.getBool("change-girya");
            changeMango = config.getBool("change-mango");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void receivePostInitialize() {
        UIStrings configStrings = CardCrawlGame.languagePack.getUIString(makeID("ConfigMenuText"));
        ModPanel settingsPanel = new ModPanel();
        ModLabeledToggleButton enableChangeCeramicFishButton = new ModLabeledToggleButton(configStrings.TEXT[0], 350.0F, 750.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, changeCeramicFish, settingsPanel, label -> { }, button -> {
            changeCeramicFish = button.enabled;
            try {
                SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
                config.setBool("change-ceramic-fish", changeCeramicFish);
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ModLabeledToggleButton enableChangeMawBankButton = new ModLabeledToggleButton(configStrings.TEXT[1], 350.0F, 700.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, changeMawBank, settingsPanel, label -> { }, button -> {
            changeMawBank = button.enabled;
            try {
                SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
                config.setBool("change-maw-bank", changeMawBank);
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ModLabeledToggleButton enableChangeStrawberryButton = new ModLabeledToggleButton(configStrings.TEXT[2], 350.0F, 650.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, changeStrawberry, settingsPanel, label -> { }, button -> {
            changeStrawberry = button.enabled;
            try {
                SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
                config.setBool("change-strawberry", changeStrawberry);
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ModLabeledToggleButton enableChangeDarkstonePeriaptButton = new ModLabeledToggleButton(configStrings.TEXT[3], 350.0F, 600.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, changeDarkstonePeriapt, settingsPanel, label -> { }, button -> {
            changeDarkstonePeriapt = button.enabled;
            try {
                SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
                config.setBool("change-darkstone-periapt", changeDarkstonePeriapt);
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ModLabeledToggleButton enableChangePearButton = new ModLabeledToggleButton(configStrings.TEXT[4], 350.0F, 550.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, changePear, settingsPanel, label -> { }, button -> {
            changePear = button.enabled;
            try {
                SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
                config.setBool("change-pear", changePear);
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ModLabeledToggleButton enableChangeStrikeDummyButton = new ModLabeledToggleButton(configStrings.TEXT[5], 350.0F, 500.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, changeStrikeDummy, settingsPanel, label -> { }, button -> {
            changeStrikeDummy = button.enabled;
            try {
                SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
                config.setBool("change-strike-dummy", changeStrikeDummy);
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ModLabeledToggleButton enableChangeGiryaButton = new ModLabeledToggleButton(configStrings.TEXT[6], 350.0F, 450.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, changeGirya, settingsPanel, label -> { }, button -> {
            changeGirya = button.enabled;
            try {
                SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
                config.setBool("change-girya", changeGirya);
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        ModLabeledToggleButton enableChangeMangoButton = new ModLabeledToggleButton(configStrings.TEXT[7], 350.0F, 450.0F, Settings.CREAM_COLOR, FontHelper.charDescFont, changeMango, settingsPanel, label -> { }, button -> {
            changeMango = button.enabled;
            try {
                SpireConfig config = new SpireConfig("relicrework", "RelicReworkConfig", defaultSettings);
                config.setBool("change-mango", changeMango);
                config.save();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        settingsPanel.addUIElement(enableChangeCeramicFishButton);
        settingsPanel.addUIElement(enableChangeMawBankButton);
        settingsPanel.addUIElement(enableChangeStrawberryButton);
        settingsPanel.addUIElement(enableChangeDarkstonePeriaptButton);
        settingsPanel.addUIElement(enableChangePearButton);
        settingsPanel.addUIElement(enableChangeStrikeDummyButton);
        settingsPanel.addUIElement(enableChangeGiryaButton);
        settingsPanel.addUIElement(enableChangeMangoButton);

        Texture badgeTexture = TextureLoader.getTexture(resourcePath("badge.png"));
        BaseMod.registerModBadge(badgeTexture, info.Name, GeneralUtils.arrToString(info.Authors), info.Description, settingsPanel);
    }

    /*----------Localization----------*/

    //This is used to load the appropriate localization files based on language.
    private static String getLangString()
    {
        return Settings.language.name().toLowerCase();
    }
    private static final String defaultLanguage = "eng";

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
            }
            catch (GdxRuntimeException e) {
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
        Optional<ModInfo> infos = Arrays.stream(Loader.MODINFOS).filter((modInfo)->{
            AnnotationDB annotationDB = Patcher.annotationDBMap.get(modInfo.jarURL);
            if (annotationDB == null)
                return false;
            Set<String> initializers = annotationDB.getAnnotationIndex().getOrDefault(SpireInitializer.class.getName(), Collections.emptySet());
            return initializers.contains(RelicRework.class.getName());
        }).findFirst();
        if (infos.isPresent()) {
            info = infos.get();
            modID = info.ID;
        }
        else {
            throw new RuntimeException("Failed to determine mod info/ID based on initializer.");
        }
    }
}
