package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.BoobyTrapComponent;
import RangerCaptain.cardmods.fusion.components.VulnerableComponent;
import RangerCaptain.cards.abstracts.AbstractMultiUpgradeCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BoobyTrappedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static RangerCaptain.MainModfile.makeID;

public class Charlequin extends AbstractMultiUpgradeCard {
    public final static String ID = makeID(Charlequin.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.CHARLEQUIN)
                .withCost(1)
                .with(new BoobyTrapComponent(3))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.BLUNDERBUSK)
                .withCost(0)
                .with(new BoobyTrapComponent(3))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.FRAGLIACCI)
                .withCost(1)
                .with(new BoobyTrapComponent(3))
                .with(new VulnerableComponent(1, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .withExhaust()
                .register();
    }

    public Charlequin() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        setMonsterData(MonsterEnum.CHARLEQUIN);
        baseInfo = info = 0;
        exhaust = true;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new BoobyTrappedPower(m, 15));
        if (info == 1) {
            Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new VulnerablePower(mon, magicNumber, false)));
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(Color.PINK), WHITE, darken(Color.PINK), WHITE, false);
    }

    @Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0,1);
    }

    public void upgrade0() {
        upgradeBaseCost(0);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.BLUNDERBUSK);
    }

    public void upgrade1() {
        baseMagicNumber = magicNumber = 0;
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[1];
        initializeTitle();
        setMonsterData(MonsterEnum.FRAGLIACCI);
        baseInfo = info = 1;
        tags.add(CustomTags.MAGIC_VULN_AOE);
    }
}