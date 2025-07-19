package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.SnowedInComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.powers.SnowedInPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Spookionna extends AbstractEasyCard {
    public final static String ID = makeID(Spookionna.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.SPOOKIONNA)
                .withCost(3)
                .with(new SnowedInComponent(1))
                .withExhaust()
                .register();
    }

    public Spookionna() {
        super(ID, 3, CardType.SKILL, CardRarity.RARE, CardTarget.ALL);
        setMonsterData(MonsterEnum.SPOOKIONNA);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new SnowedInPower(p, 1));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {
            if (!AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty()) {
                canUse = false;
                cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
            }
            return canUse;
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        glowColor = AbstractDungeon.actionManager.cardsPlayedThisTurn.isEmpty() ? AbstractCard.GOLD_BORDER_GLOW_COLOR : AbstractCard.BLUE_BORDER_GLOW_COLOR;
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }
}