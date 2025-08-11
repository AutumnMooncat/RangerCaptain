package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.AddDoublePlayComponent;
import RangerCaptain.cardmods.fusion.components.DoubleTapComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.MultistrikePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Anathema extends AbstractEasyCard implements StartupCard {
    public final static String ID = makeID(Anathema.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.ANATHEMA)
                .withCost(1)
                .withFlags(new DoubleTapComponent(1), AbstractComponent.Flag.REQUIRES_SAME_SOURCES)
                .withFlags(new AddDoublePlayComponent(), AbstractComponent.Flag.REQUIRES_DIFFERENT_SOURCES)
                .register();
    }

    public Anathema() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        setMonsterData(MonsterEnum.ANATHEMA);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        //Wiz.applyToSelf(new DoubleTapPower(Wiz.adp(), 1));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(GREEN), WHITE, darken(GREEN), WHITE, false);
    }

    @Override
    public boolean atBattleStartPreDraw() {
        Wiz.applyToSelf(new MultistrikePower(Wiz.adp(), 1));
        return true;
    }
}