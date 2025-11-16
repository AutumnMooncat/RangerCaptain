package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.DrawComponent;
import RangerCaptain.cardfusion.components.ExhaustCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Muskrateer extends AbstractEasyCard {
    public final static String ID = makeID(Muskrateer.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MUSKRATEER)
                .withCost(1)
                .withDamage(6, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .with(new ExhaustCardsComponent(0.5f, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .withFlags(new DrawComponent(0.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
        new FusionComponentHelper(MonsterEnum.RATCOUSEL)
                .withCost(1)
                .withDamage(8, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new ExhaustCardsComponent(0.5f, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .withFlags(new DrawComponent(0.5f), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
    }

    public Muskrateer() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 8;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.MUSKRATEER);
        setElementalType(ElementalType.BEAST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, upgraded ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        addToBot(new BetterSelectCardsInHandAction(1, ExhaustAction.TEXT[0], true, true, c -> true, cards -> {
            for (AbstractCard card : cards) {
                addToTop(new DrawCardAction(magicNumber));
                addToTop(new ExhaustSpecificCardAction(card, p.hand, true));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        //upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.RATCOUSEL);
        setElementalType(ElementalType.PLASTIC);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }
}