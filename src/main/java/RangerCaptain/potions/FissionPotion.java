package RangerCaptain.potions;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.util.ColorUtil;
import RangerCaptain.util.Wiz;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class FissionPotion extends CustomPotion {
    public static final String POTION_ID = MainModfile.makeID(FissionPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final Color LIQUID = ColorUtil.EMERALD.cpy();
    public static final Color HYBRID = ColorUtil.darken(ColorUtil.JADE);
    public static final Color SPOTS = null;

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final int EFFECT = 2;

    public FissionPotion() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPIKY, PotionColor.FIRE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new BetterSelectCardsInHandAction(1, potionStrings.DESCRIPTIONS[3], false, false, c -> true, cards -> {
            for (AbstractCard card : cards) {
                Wiz.att(new MakeTempCardInHandAction(card, potency));
            }
        }));
    }

    // This is your potency.
    @Override
    public int getPotency(final int ascensionLevel) {
        return EFFECT;
    }

    @Override
    public void initializeData() {
        potency = getPotency();
        if (potency == 1) {
            description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[1];
        } else {
            description = potionStrings.DESCRIPTIONS[0] + potency + potionStrings.DESCRIPTIONS[2];
        }
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new FissionPotion();
    }
}
