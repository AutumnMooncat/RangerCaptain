package RangerCaptain.potions;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.actions.FusionAction;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.relics.interfaces.PreFusionRelic;
import RangerCaptain.util.ColorUtil;
import RangerCaptain.util.CustomLighting;
import RangerCaptain.util.KeywordManager;
import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class SunPotion extends CustomPotion implements CustomLighting {
    public static final String POTION_ID = MainModfile.makeID(SunPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final Color LIQUID = ColorUtil.YELLOW.cpy();
    public static final Color HYBRID = ColorUtil.GOLDEN_YELLOW.cpy();
    public static final Color SPOTS = null;

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
    public static final int EFFECT = 2;

    public SunPotion() {
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.BOTTLE, PotionColor.FIRE);
        isThrown = false;
        targetRequired = false;
    }

    @Override
    public void use(AbstractCreature target) {
        addToBot(new DoAction(() -> {
            for (AbstractRelic relic : Wiz.adp().relics) {
                if (relic instanceof PreFusionRelic) {
                    ((PreFusionRelic) relic).preFusion();
                }
            }
            addToBot(new DoAction(() -> {
                CardGroup hand = Wiz.adp().hand;
                ArrayList<AbstractCard> validCards = hand.group.stream().filter(Wiz::canBeFused).collect(Collectors.toCollection(ArrayList::new));
                for (int i = 0; i < potency; i++) {
                    if (validCards.size() >= 2) {
                        AbstractCard base = validCards.remove(AbstractDungeon.cardRandomRng.random(validCards.size() - 1));
                        AbstractCard donor = validCards.remove(AbstractDungeon.cardRandomRng.random(validCards.size() - 1));
                        hand.removeCard(base);
                        hand.removeCard(donor);
                        FusedCard fusion = Wiz.fuse(base, donor);
                        hand.addToTop(fusion);
                        hand.refreshHandLayout();
                        hand.applyPowers();
                        FusionAction.fusionTriggers(base, donor, fusion);
                    }
                }
            }));
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
            description = potionStrings.DESCRIPTIONS[0];
        } else {
            description = potionStrings.DESCRIPTIONS[1] + potency + potionStrings.DESCRIPTIONS[2];
        }
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BaseMod.getKeywordTitle(KeywordManager.FUSION), BaseMod.getKeywordDescription(KeywordManager.FUSION)));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && Wiz.adp().hand.group.stream().filter(Wiz::canBeFused).count() >= 2;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new SunPotion();
    }

    @Override
    public float[] _lightsOutGetXYRI() {
        return new float[] {posX, posY, 150f, 3f};
    }

    @Override
    public Color[] _lightsOutGetColor() {
        return new Color[] {LIQUID};
    }
}
