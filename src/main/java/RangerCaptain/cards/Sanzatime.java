package RangerCaptain.cards;

import RangerCaptain.actions.ResolveNextTurnEffectsAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.ResolveNextTurnEffectsComponent;
import RangerCaptain.cardfusion.components.WeakComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RangerCaptain.MainModfile.makeID;

public class Sanzatime extends AbstractEasyCard {
    public final static String ID = makeID(Sanzatime.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SANZATIME)
                .withCost(1)
                .with(new WeakComponent(1.5f), new ResolveNextTurnEffectsComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.FORTIWINX)
                .withCost(0)
                .with(new WeakComponent(1.5f), new ResolveNextTurnEffectsComponent())
                .register();
    }

    public Sanzatime() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.SANZATIME);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new WeakPower(m, magicNumber, false));
        addToBot(new ResolveNextTurnEffectsAction());
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
        //upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FORTIWINX);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(GOLD), WHITE, darken(GOLD), WHITE, false);
    }
}