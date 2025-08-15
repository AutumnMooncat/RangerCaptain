package RangerCaptain.potions;

import RangerCaptain.MainModfile;
import RangerCaptain.util.ColorUtil;
import RangerCaptain.util.Wiz;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

public class VigorPotion extends CustomPotion {
    public static final String POTION_ID = MainModfile.makeID(VigorPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final Color LIQUID = ColorUtil.VERMILION;
    public static final Color HYBRID = ColorUtil.darken(ColorUtil.SCARLET);
    public static final Color SPOTS = null;

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final int EFFECT = 10;

    public VigorPotion() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.S, PotionColor.FIRE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void use(AbstractCreature target) {
        Wiz.applyToSelf(new VigorPower(Wiz.adp(), potency));
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return EFFECT;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.VIGOR.NAMES[0]), GameDictionary.keywords.get(GameDictionary.VIGOR.NAMES[0])));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new VigorPotion();
    }
}
