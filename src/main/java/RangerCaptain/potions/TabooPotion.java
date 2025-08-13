package RangerCaptain.potions;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.AnimationAction;
import RangerCaptain.actions.ThrowObjectAction;
import RangerCaptain.util.TextureSniper;
import RangerCaptain.util.Wiz;
import RangerCaptain.vfx.ColoredSmokeBombEffect;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;

public class TabooPotion extends CustomPotion {
    public static final String POTION_ID = MainModfile.makeID(TabooPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final int EFFECT = 8;

    public TabooPotion() {
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SPHERE, PotionColor.FIRE);
        isThrown = true;
        targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target) {
        if (target instanceof AbstractMonster) {
            Wiz.atb(new AnimationAction(AnimationAction.Animation.ATTACK));
            Wiz.atb(new ThrowObjectAction(TextureSniper.snipePotion(this), 1f, target.hb, Color.PURPLE, false));
            Wiz.atb(new VFXAction(new ColoredSmokeBombEffect(target.hb.cX, target.hb.cY, Color.PURPLE)));
            //Wiz.applyToEnemy((AbstractMonster) target, new StaggerPower(target, potency));
        }
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
        //tips.add(new PowerTip(BaseMod.getKeywordTitle(KeywordManager.STAGGER), BaseMod.getKeywordDescription(KeywordManager.STAGGER)));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new TabooPotion();
    }
}
