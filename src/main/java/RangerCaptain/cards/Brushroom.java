package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.CostsLessPerStashedComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.interfaces.OnOtherCardStashedCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static RangerCaptain.MainModfile.makeID;

public class Brushroom extends AbstractEasyCard implements OnOtherCardStashedCard {
    public final static String ID = makeID(Brushroom.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BRUSHROOM)
                .withCost(3)
                .with(new CostsLessPerStashedComponent())
                .withBlock(10)
                .register();
        new FusionComponentHelper(MonsterEnum.FUNGOGH)
                .withCost(3)
                .with(new CostsLessPerStashedComponent())
                .withBlock(13.5f)
                .register();
    }

    public Brushroom() {
        super(ID, 3, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 15;
        setMonsterData(MonsterEnum.BRUSHROOM);
    }

    @Override
    public void onStashedOther(AbstractCard card) {
        setCostForTurn(costForTurn - 1);
    }

    @Override
    public void triggerWhenDrawn() {
        setCostForTurn(cost - CardCounterPatches.cardsStashedThisTurn);
    }

    @Override
    public void atTurnStart() {
        resetAttributes();
        applyPowers();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
    }

    @Override
    public void upp() {
        upgradeBlock(5);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FUNGOGH);
    }

    @Override
    public AbstractCard makeCopy() {
        AbstractCard tmp = super.makeCopy();
        if (CardCrawlGame.dungeon != null && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT) {
            tmp.setCostForTurn(tmp.cost - CardCounterPatches.cardsStashedThisTurn);
        }
        return tmp;
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, RED, WHITE, RED, WHITE, false);
    }
}