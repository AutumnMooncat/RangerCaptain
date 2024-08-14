package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.cards.Sanzatime;
import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class FortiwinxMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(FortiwinxMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final int AMOUNT = 2;


    public FortiwinxMod() {
        super(ID);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card instanceof Sanzatime) {
            magic += AMOUNT;
        }
        return magic;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
            card.target = AbstractCard.CardTarget.ENEMY;
        }
        if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
        }
    }

    @Override
    public void onUpgrade(AbstractCard card) {
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
            card.target = AbstractCard.CardTarget.ENEMY;
        }
        if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
        }
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return DESCRIPTION_TEXT[0];
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!(card instanceof Sanzatime)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, CARD_TEXT[0]);
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
                                pow.stackPower(AMOUNT);
                            } else if (pow.canGoNegative) {
                                pow.stackPower(-AMOUNT);
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
        return new FortiwinxMod();
    }
}
