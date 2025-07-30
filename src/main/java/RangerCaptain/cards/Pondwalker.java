package RangerCaptain.cards;

import RangerCaptain.actions.StashTopCardsAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.StashTopCardsComponent;
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
        // 7x2 -> 7x3
        new FusionComponentHelper(MonsterEnum.PONDWALKER)
                .withCost(2)
                .withMultiDamage(3, 2, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new StashTopCardsComponent(1))
                .register();
        // 10x2 -> 10x3
        new FusionComponentHelper(MonsterEnum.SHARKTANKER)
                .withCost(2)
                .withMultiDamage(4, 2, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new StashTopCardsComponent(1))
                .register();
    }

    public Pondwalker() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 7;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.PONDWALKER);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, info == 1 ? AbstractGameAction.AttackEffect.SLASH_HEAVY : AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        dmg(m, info == 1 ? AbstractGameAction.AttackEffect.SLASH_HEAVY : AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        addToBot(new StashTopCardsAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
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