package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.TapeJamComponent;
import RangerCaptain.cardmods.fusion.components.WeakComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.TapeJamPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import static RangerCaptain.MainModfile.makeID;

public class Ferriclaw extends AbstractEasyCard {
    public final static String ID = makeID(Ferriclaw.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.FERRICLAW)
                .withCost(1)
                .with(new WeakComponent(2))
                .with(new TapeJamComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.AURICLAW)
                .withCost(1)
                .with(new WeakComponent(3))
                .with(new TapeJamComponent(3))
                .register();
    }

    public Ferriclaw() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 2;
        setMonsterData(MonsterEnum.FERRICLAW);
        tags.add(CustomTags.MAGIC_WEAK);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new WeakPower(m, magicNumber, false));
        Wiz.applyToEnemy(m, new TapeJamPower(m, secondMagic));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.AURICLAW);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLUE, WHITE, BLUE, WHITE, false);
    }
}