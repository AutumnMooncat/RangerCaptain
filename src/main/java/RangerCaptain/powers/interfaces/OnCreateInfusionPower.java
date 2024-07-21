package RangerCaptain.powers.interfaces;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;

public interface OnCreateInfusionPower {
    int increaseTimes(AbstractCard c, AbstractCardModifier mod);
}
