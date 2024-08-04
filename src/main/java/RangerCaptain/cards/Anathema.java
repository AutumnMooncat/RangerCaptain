package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterData;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.mod.stslib.cards.interfaces.StartupCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.DoubleTapPower;

import static RangerCaptain.MainModfile.makeID;

public class Anathema extends AbstractEasyCard implements StartupCard {
    public final static String ID = makeID(Anathema.class.getSimpleName());

    public Anathema() {
        super(ID, 0, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
        setMonsterData(MonsterData.ANATHEMA);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new DoubleTapPower(Wiz.adp(), 1));
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
        Wiz.applyToSelf(new DoubleTapPower(Wiz.adp(), 1));
        return true;
    }
}