package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.SprintComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.SprintPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Icepeck extends AbstractEasyCard {
    public final static String ID = makeID(Icepeck.class.getSimpleName());

    static {
        // 9,1 -> 12,2
        new FusionComponentHelper(MonsterEnum.ICEPECK)
                .withCost(2)
                .withDamage(6, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new SprintComponent(1))
                .register();
        // 12,1 -> 16,2
        new FusionComponentHelper(MonsterEnum.CRYOSHEAR)
                .withCost(2)
                .withDamage(8, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new SprintComponent(1))
                .register();
    }

    public Icepeck() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 9;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.ICEPECK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        Wiz.applyToSelf(new SprintPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.CRYOSHEAR);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(RED), WHITE, darken(RED), WHITE, false);
    }
}