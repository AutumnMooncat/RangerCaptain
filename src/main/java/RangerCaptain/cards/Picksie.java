package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.NextTurnEnergyComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.APBoostPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Picksie extends AbstractEasyCard {
    public final static String ID = makeID(Picksie.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.PICKSIE)
                .withCost(2)
                .withBlock(5)
                .with(new NextTurnEnergyComponent(1))
                .register();
    }

    public Picksie() {
        super(ID, 2, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        baseBlock = block = 12;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.PICKSIE);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        tags.add(CustomTags.MAGIC_ENERGY_NEXT_TURN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        Wiz.applyToSelf(new APBoostPower(p, magicNumber));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BRONZE), WHITE, darken(BRONZE), WHITE, false);
    }
}