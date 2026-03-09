package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.WeakComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.ClickableTipsPatch;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.ArrayList;
import java.util.List;

import static RangerCaptain.MainModfile.makeID;

public class Faucetear extends AbstractEasyCard {
    public final static String ID = makeID(Faucetear.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.FAUCETEAR)
                .withCost(1)
                .withBlock(4.5f)
                .with(new WeakComponent(0.91f))
                .register();
        new FusionComponentHelper(MonsterEnum.FOUNTESS)
                .withCost(1)
                .withBlock(6)
                .with(new WeakComponent(1.91f))
                .register();
    }

    public Faucetear() {
        super(ID, 1, CardType.SKILL, CardRarity.COMMON, CardTarget.SELF_AND_ENEMY);
        baseBlock = block = 6;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.FAUCETEAR);
        setElementalType(ElementalType.WATER);
    }

    private static List<TooltipInfo> tips;
    @Override
    public List<TooltipInfo> getCustomTooltips() {
        if (tips == null) {
            tips = new ArrayList<>();
            tips.add(new TooltipInfo("SFX Example", ClickableTipsPatch.TEST2));
            tips.add(new TooltipInfo("Cycle Example", ClickableTipsPatch.TEST3));
            List<TooltipInfo> sup = super.getCustomTooltips();
            if (sup != null) {
                tips.addAll(sup);
            }
        }
        return tips;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.applyToEnemy(m, new WeakPower(m, magicNumber, false));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FOUNTESS);
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