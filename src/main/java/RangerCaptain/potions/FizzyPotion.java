package RangerCaptain.potions;

import RangerCaptain.MainModfile;
import RangerCaptain.util.ColorUtil;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class FizzyPotion extends CustomPotion {
    public static final String POTION_ID = MainModfile.makeID(FizzyPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final Color LIQUID = ColorUtil.darken(ColorUtil.ORANGE_PEEL);
    public static final Color HYBRID = ColorUtil.darken(ColorUtil.ORANGE_PEEL);
    public static final Color SPOTS = ColorUtil.AMBER.cpy();

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final int EFFECT = 3;

    public FizzyPotion() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.BOLT, PotionColor.FIRE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new GainEnergyAction(potency));
        addToBot(new MakeTempCardInHandAction(new Dazed(), potency));
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return EFFECT;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1] + potency + potionStrings.DESCRIPTIONS[2];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(TipHelper.capitalize(GameDictionary.DAZED.NAMES[0]), GameDictionary.keywords.get(GameDictionary.DAZED.NAMES[0])));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FizzyPotion();
    }
}
