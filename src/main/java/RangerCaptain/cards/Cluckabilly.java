package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.HavocComponent;
import RangerCaptain.cardmods.fusion.components.MultitargetComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.MultitargetPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Cluckabilly extends AbstractEasyCard {
    public final static String ID = makeID(Cluckabilly.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.CLUCKABILLY)
                .withCost(1)
                .with(new MultitargetComponent(1), new HavocComponent(1))
                .register();
        // TODO doesnt scale properly becasue current cost scaling is stupid
        new FusionComponentHelper(MonsterEnum.ROCKERTRICE)
                .withCost(0)
                .with(new MultitargetComponent(1), new HavocComponent(1))
                .register();
    }

    public Cluckabilly() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 1;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.CLUCKABILLY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToSelf(new MultitargetPower(p, magicNumber));
        for (int i = 0; i < secondMagic; i++) {
            addToBot(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), true));
        }
    }

    @Override
    public void upp() {
        upgradeBaseCost(0);
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