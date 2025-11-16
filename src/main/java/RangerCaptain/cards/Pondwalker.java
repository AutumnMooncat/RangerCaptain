package RangerCaptain.cards;

import RangerCaptain.actions.StashTopCardsAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.StashTopCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Pondwalker extends AbstractEasyCard {
    public final static String ID = makeID(Pondwalker.class.getSimpleName());

    static {
        // 15 -> 20
        new FusionComponentHelper(MonsterEnum.PONDWALKER)
                .withCost(2)
                .withDamage(10, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new StashTopCardsComponent(1.5f))
                .register();
        // 20 -> 27
        new FusionComponentHelper(MonsterEnum.SHARKTANKER)
                .withCost(2)
                .withDamage(13.5f, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new StashTopCardsComponent(1.5f))
                .register();
    }

    public Pondwalker() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 15;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.PONDWALKER);
        setElementalType(ElementalType.WATER);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, info == 1 ? AbstractGameAction.AttackEffect.SLASH_HEAVY : AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new StashTopCardsAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(5);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SHARKTANKER);
        baseInfo = info = 1;
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