package RangerCaptain.cards;

import RangerCaptain.actions.IncreaseDebuffsAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.IncreaseDebuffsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Sanzatime extends AbstractEasyCard {
    public final static String ID = makeID(Sanzatime.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SANZATIME)
                .withCost(2)
                .withDamage(5, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new IncreaseDebuffsComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.FORTIWINX)
                .withCost(2)
                .withDamage(7, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new IncreaseDebuffsComponent(2))
                .register();
    }

    public Sanzatime() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 12;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.SANZATIME);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new IncreaseDebuffsAction(m, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeMagicNumber(1);
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