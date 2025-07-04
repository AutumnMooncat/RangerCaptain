package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionModHelper;
import RangerCaptain.cardmods.fusion.mods.TempThornsMod;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.LosePowerLaterPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ThornsPower;

import static RangerCaptain.MainModfile.makeID;

public class Macabra extends AbstractEasyCard {
    public final static String ID = makeID(Macabra.class.getSimpleName());

    static {
        new FusionModHelper(MonsterEnum.MACABRA)
                .withBlock(3)
                .with(new TempThornsMod(2))
                .register();
        new FusionModHelper(MonsterEnum.MACABRA)
                .withBlock(4)
                .with(new TempThornsMod(3))
                .register();
    }

    public Macabra() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF);
        baseBlock = block = 6;
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.MACABRA);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.applyToSelf(new ThornsPower(p, magicNumber));
        Wiz.applyToSelf(new LosePowerLaterPower(p, new ThornsPower(p, magicNumber), magicNumber));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FOLKLORD);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, GREEN, WHITE, GREEN, WHITE, false);
    }
}