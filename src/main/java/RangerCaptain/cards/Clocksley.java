package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.DrawComponent;
import RangerCaptain.cardfusion.components.OnDrawUnplayableComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.SpringLoadedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Clocksley extends AbstractEasyCard {
    public final static String ID = makeID(Clocksley.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.CLOCKSLEY)
                .withCost(1)
                .with(new OnDrawUnplayableComponent())
                .withFlags(new DrawComponent(2), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
        new FusionComponentHelper(MonsterEnum.ROBINDAM)
                .withCost(1)
                .with(new OnDrawUnplayableComponent())
                .withFlags(new DrawComponent(3), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
    }

    public Clocksley() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.CLOCKSLEY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new SpringLoadedPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.ROBINDAM);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, EMERALD, WHITE, EMERALD, WHITE, false);
    }
}