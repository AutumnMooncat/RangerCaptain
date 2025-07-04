package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.Elfless;
import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class EnergyIfDebuffMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(EnergyIfDebuffMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final Color GOLD_COLOR = Color.GOLD.cpy();

    public EnergyIfDebuffMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card instanceof Elfless) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        makeTargeted(card);
    }

    @Override
    public void onUpgrade(AbstractCard card) {
        makeTargeted(card);
    }

    @Override
    public String getModDescription() {
        return String.format(DESCRIPTION_TEXT[0], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!(card instanceof Elfless)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!(card instanceof Elfless)) {
            addToBot(new DoAction(() -> {
                if (target != null && target.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)) {
                    addToTop(new GainEnergyAction(val));
                }
            }));
        }
    }

    @Override
    public Color getGlow(AbstractCard card) {
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (mon.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)) {
                return GOLD_COLOR;
            }
        }
        return null;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new EnergyIfDebuffMod(baseVal);
    }
}
