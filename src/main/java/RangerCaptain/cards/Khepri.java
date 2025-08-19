package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.DamageComponent;
import RangerCaptain.cardfusion.components.ExhaustCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Khepri extends AbstractEasyCard {
    public final static String ID = makeID(Khepri.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.KHEPRI)
                .withCost(0)
                .with(new ExhaustCardsComponent(2.5f, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .withFlags(new DamageComponent(2, AbstractGameAction.AttackEffect.FIRE), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .register();
    }

    public Khepri() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 3;
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.KHEPRI);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        tags.add(CustomTags.MAGIC_EXHAUST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BetterSelectCardsInHandAction(magicNumber, ExhaustAction.TEXT[0], true, true, c -> true, cards -> {
            for (int i = 0; i < cards.size(); i++) {
                AbstractCard card = cards.get(i);
                dmgTop(m, AbstractGameAction.AttackEffect.FIRE);
                addToTop(new ExhaustSpecificCardAction(card, Wiz.adp().hand));
            }
        }));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BLUE), WHITE, darken(BLUE), WHITE, false);
    }
}