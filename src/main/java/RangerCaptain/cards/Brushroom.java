package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.CostsLessPerStashedComponent;
import RangerCaptain.cardmods.fusion.components.DamageComponent;
import RangerCaptain.cardmods.fusion.components.VulnerableComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.interfaces.OnOtherCardStashedCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;

import static RangerCaptain.MainModfile.makeID;

public class Brushroom extends AbstractEasyCard implements OnOtherCardStashedCard {
    public final static String ID = makeID(Brushroom.class.getSimpleName());

    static {
        // 14 -> 20
        new FusionComponentHelper(MonsterEnum.BRUSHROOM)
                .withCost(3)
                .with(new CostsLessPerStashedComponent())
                .with(new DamageComponent(4, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new VulnerableComponent(1))
                .register();
        // 18 -> 25
        new FusionComponentHelper(MonsterEnum.FUNGOGH)
                .withCost(3)
                .with(new CostsLessPerStashedComponent())
                .with(new DamageComponent(5, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new VulnerableComponent(1))
                .register();
    }

    public Brushroom() {
        super(ID, 3, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 14;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.BRUSHROOM);
        tags.add(CustomTags.MAGIC_DRAW);
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
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        Wiz.applyToEnemy(m, new VulnerablePower(m, magicNumber, false));
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        upgradeMagicNumber(1);
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