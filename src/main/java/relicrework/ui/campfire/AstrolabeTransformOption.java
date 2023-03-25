package relicrework.ui.campfire;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import relicrework.RelicRework;
import relicrework.util.TextureLoader;

public class AstrolabeTransformOption extends AbstractCampfireOption {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(RelicRework.makeID("AstrolabeTransformOption"));
    public static final String[] TEXT = uiStrings.TEXT;

    public AstrolabeTransformOption() {
        this.label = TEXT[0];
        this.description = TEXT[1];
        this.img = TextureLoader.getTexture(RelicRework.resourcePath("images/ui/campfire/transform.png"));
    }

    public void useOption() {
        AbstractDungeon.effectList.add(new CampfireTransformEffect());
    }
}
