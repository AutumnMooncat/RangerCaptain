package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.actions.DoIfPowerAppliedAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CardInHandSuite;
import RangerCaptain.powers.ToxinPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerToRandomEnemyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class EarthAltar extends AbstractEasyCard implements CardInHandSuite.InHandCard {
    public final static String ID = makeID(EarthAltar.class.getSimpleName());

    public EarthAltar() {
        super(ID, -2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void onGainBlock(int block) {
        superFlash();
        addToBot(new ApplyPowerToRandomEnemyAction(Wiz.adp(), new ToxinPower(null, magicNumber), magicNumber));
    }

    @Override
    public void onPowerApplied(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (upgraded && power.type == AbstractPower.PowerType.BUFF && target == Wiz.adp()) {
            addToBot(new DoIfPowerAppliedAction(power, new DoAction(() -> {
                superFlash();
                addToTop(new ApplyPowerToRandomEnemyAction(Wiz.adp(), new ToxinPower(null, magicNumber), magicNumber));
            })));
        }
    }

    @Override
    public void upp() {
        uDesc();
    }
}