package RangerCaptain.potions;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.ThrowObjectAction;
import RangerCaptain.powers.ResonancePower;
import RangerCaptain.util.ColorUtil;
import RangerCaptain.util.KeywordManager;
import RangerCaptain.util.TextureSniper;
import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

public class ResonancePotion extends CustomPotion {
    public static final String POTION_ID = MainModfile.makeID(ResonancePotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final Color LIQUID = ColorUtil.SILVER.cpy();
    public static final Color HYBRID = ColorUtil.darken(ColorUtil.GRAY);
    public static final Color SPOTS = null;

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final int EFFECT = 3;

    public ResonancePotion() {
        super(NAME, POTION_ID, PotionRarity.COMMON, PotionSize.SPHERE, PotionColor.FIRE);
        isThrown = true;
        targetRequired = true;
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new AnimateFastAttackAction(Wiz.adp()));
        addToBot(new ThrowObjectAction(TextureSniper.snipePotion(this), 1f, target.hb, Color.GRAY, false));
        addToBot(new SFXAction("POTION_DROP_1", 0.1f));
        addToBot(new SFXAction("ORB_FROST_EVOKE", 0.1f));
        addToBot(new VFXAction(new ShockWaveEffect(target.hb.cX, target.hb.cY, Color.GRAY, ShockWaveEffect.ShockWaveType.CHAOTIC)));
        addToBot(new ApplyPowerAction(target, Wiz.adp(), new ResonancePower(target, Wiz.adp(), potency)));
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
        tips.add(new PowerTip(BaseMod.getKeywordTitle(KeywordManager.RESONANCE), BaseMod.getKeywordDescription(KeywordManager.RESONANCE)));
    }

    @Override
    public AbstractPotion makeCopy() {
        return new ResonancePotion();
    }
}
