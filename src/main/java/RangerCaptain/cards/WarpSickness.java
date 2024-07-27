package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.Wiz;
import basemod.ReflectionHacks;
import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.FleetingField;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DrawReductionPower;

import static RangerCaptain.MainModfile.makeID;

public class WarpSickness extends AbstractEasyCard {
    public final static String ID = makeID(WarpSickness.class.getSimpleName());

    public WarpSickness() {
        super(ID, 1, CardType.CURSE, CardRarity.SPECIAL, CardTarget.SELF, CardColor.CURSE);
        FleetingField.fleeting.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        DrawReductionPower drp = new DrawReductionPower(p, 1);
        ReflectionHacks.setPrivate(drp, DrawReductionPower.class, "justApplied", false);
        Wiz.applyToSelf(drp);
    }

    @Override
    public void upp() {}
}