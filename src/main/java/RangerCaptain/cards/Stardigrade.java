package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.OnPlayNoAttacksComponent;
import RangerCaptain.cardmods.fusion.components.StrengthComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.MeditatingPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Stardigrade extends AbstractEasyCard {
    public final static String ID = makeID(Stardigrade.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.STARDIGRADE)
                .withCost(1)
                .with(new OnPlayNoAttacksComponent())
                .with(new StrengthComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.GALAGOR)
                .withCost(1)
                .with(new OnPlayNoAttacksComponent())
                .with(new StrengthComponent(4))
                .register();
    }

    public Stardigrade() {
        super(ID, 1, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.STARDIGRADE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new MeditatingPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.GALAGOR);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BLUE), WHITE, darken(BLUE), WHITE, false);
    }
}