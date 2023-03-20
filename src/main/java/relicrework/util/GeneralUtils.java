package relicrework.util;

import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;

import java.util.ArrayList;

public class GeneralUtils {
    public static String arrToString(Object[] arr) {
        if (arr == null)
            return null;
        if (arr.length == 0)
            return "";

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length - 1; ++i) {
            sb.append(arr[i]).append(", ");
        }
        sb.append(arr[arr.length - 1]);
        return sb.toString();
    }

    public static AbstractPotion getRandomPotionByRarity(AbstractPotion.PotionRarity rarity) {
        ArrayList<AbstractPotion> potions = PotionHelper.getPotionsByRarity(rarity);
        int randomChoice = (int)(Math.random() * potions.size());
        return potions.get(randomChoice);
    }
}
