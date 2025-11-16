package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.BurnComponent;
import RangerCaptain.cardfusion.components.BurnPointsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class Burnace extends AbstractEasyCard {
    public final static String ID = makeID(Burnace.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BURNACE)
                .withCost(0)
                .with(new BurnComponent(2.5f), new BurnPointsComponent(AbstractComponent.ComponentTarget.ENEMY))
                .register();
        new FusionComponentHelper(MonsterEnum.SMOGMAGOG)
                .withCost(0)
                .with(new BurnComponent(4), new BurnPointsComponent(AbstractComponent.ComponentTarget.ENEMY))
                .register();
    }

    public Burnace() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.BURNACE);
        setElementalType(ElementalType.FIRE);
        tags.add(CustomTags.MAGIC_BURN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new BurnedPower(m, p, magicNumber));
        addToBot(new DoAction(() -> {
            AbstractPower burn = m.getPower(BurnedPower.POWER_ID);
            if (burn != null) {
                addToTop(new LoseHPAction(m, p, burn.amount, AbstractGameAction.AttackEffect.FIRE));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SMOGMAGOG);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, DARK_GRAY, WHITE, DARK_GRAY, WHITE, false);
    }
}