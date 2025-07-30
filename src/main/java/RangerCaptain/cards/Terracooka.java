package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.BurnComponent;
import RangerCaptain.cardmods.fusion.components.ExhaustCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Terracooka extends AbstractEasyCard {
    public final static String ID = makeID(Terracooka.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.TERRACOOKA)
                .withCost(1)
                .with(new BurnComponent(3), new ExhaustCardsComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.COALDRON)
                .withCost(1)
                .with(new BurnComponent(5), new ExhaustCardsComponent(1))
                .register();
    }

    public Terracooka() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 4;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.TERRACOOKA);
        tags.add(CustomTags.MAGIC_BURN);
        tags.add(CustomTags.SECOND_MAGIC_EXHAUST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new BurnedPower(m, p, magicNumber));
        addToBot(new ExhaustAction(secondMagic, false, false, false));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.COALDRON);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BRONZE), WHITE, darken(BRONZE), WHITE, false);
    }
}