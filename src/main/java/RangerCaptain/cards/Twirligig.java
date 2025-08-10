package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.OnParryComponent;
import RangerCaptain.cardmods.fusion.components.VigorComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.ParryPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Twirligig extends AbstractEasyCard {
    public final static String ID = makeID(Twirligig.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.TWIRLIGIG)
                .withCost(2)
                .with(new OnParryComponent())
                .withFlags(new VigorComponent(10), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
        new FusionComponentHelper(MonsterEnum.KIRIKURI)
                .withCost(2)
                .with(new OnParryComponent())
                .withFlags(new VigorComponent(14), AbstractComponent.Flag.INVERSE_PREFERRED)
                .register();
    }

    public Twirligig() {
        super(ID, 2, CardType.POWER, CardRarity.UNCOMMON, CardTarget.SELF);
        baseMagicNumber = magicNumber = 5;
        setMonsterData(MonsterEnum.TWIRLIGIG);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new ParryPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.KIRIKURI);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, Color.PINK, WHITE, Color.PINK, WHITE, false);
    }
}