package RangerCaptain.cardmods.fusion;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.cards.Carniviper;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;

public class JormungoldMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(JormungoldMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public JormungoldMod() {
        super(ID);
    }

    @Override
    public void onDrawn(AbstractCard card) {
        Wiz.forAllMonstersLiving(mon -> {
            AbstractPower poison = mon.getPower(PoisonPower.POWER_ID);
            if (poison != null) {
                addToBot(new LoseHPAction(mon, Wiz.adp(), poison.amount, AbstractGameAction.AttackEffect.POISON));
            }
        });
    }

    @Override
    public String getModDescription(AbstractCard card) {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (card instanceof Carniviper && ((Carniviper) card).info == 2) {
            return rawDescription.replace(CARD_TEXT[0], CARD_TEXT[1]);
        }
        return FormatHelper.insertAfterText(rawDescription, CARD_TEXT[0]);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new JormungoldMod();
    }
}
