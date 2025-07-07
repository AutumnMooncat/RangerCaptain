package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.PoisonComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import static RangerCaptain.MainModfile.makeID;

public class Nevermort extends AbstractEasyCard {
    public final static String ID = makeID(Nevermort.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.NEVERMORT)
                .withCost(0)
                .with(new PoisonComponent(3, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.APOCROWLYPSE)
                .withCost(0)
                .with(new PoisonComponent(5, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
    }

    public Nevermort() {
        super(ID, 0, CardType.SKILL, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.NEVERMORT);
        tags.add(CustomTags.MAGIC_POISON_AOE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new PoisonPower(mon, p, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.APOCROWLYPSE);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLACK, WHITE, BLACK, WHITE, false);
    }
}