package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.Sanzatime;
import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class IncreaseDebuffsMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(IncreaseDebuffsMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;


    public IncreaseDebuffsMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card instanceof Sanzatime) {
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
        if (!(card instanceof Sanzatime)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature targetCreature, UseCardAction action) {
        if (!(card instanceof Sanzatime)) {
            if (targetCreature != null) {
                addToBot(new DoAction(() -> {
                    for (AbstractPower pow : targetCreature.powers) {
                        if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof NonStackablePower)) {
                            if (pow.amount > 0) {
                                pow.stackPower(val);
                            } else if (pow.canGoNegative) {
                                pow.stackPower(-val);
                            }
                            pow.updateDescription();
                        }
                    }
                }));
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new IncreaseDebuffsMod(baseVal);
    }
}
