package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.MultitargetComponent;
import RangerCaptain.cardmods.fusion.components.VulnerableComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.MultitargetPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static RangerCaptain.MainModfile.makeID;

public class Cluckabilly extends AbstractEasyCard {
    public final static String ID = makeID(Cluckabilly.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.CLUCKABILLY)
                .withCost(0)
                .with(new MultitargetComponent(2))
                .with(new VulnerableComponent(2, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.ROCKERTRICE)
                .withCost(1)
                .with(new MultitargetComponent(2))
                .with(new VulnerableComponent(3, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
    }

    public Cluckabilly() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ALL);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.CLUCKABILLY);
        tags.add(CustomTags.SECOND_MAGIC_VULN_AOE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new MultitargetPower(p, magicNumber));
        Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new VulnerablePower(mon, secondMagic, false)));
    }

    @Override
    public void upp() {
        upgradeSecondMagic(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.ROCKERTRICE);
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