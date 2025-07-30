package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.BurnComponent;
import RangerCaptain.cardmods.fusion.components.BurnPointsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class Burnace extends AbstractEasyCard {
    public final static String ID = makeID(Burnace.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BURNACE)
                .withCost(0)
                .with(new BurnComponent(4), new BurnPointsComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.SMOGMAGOG)
                .withCost(0)
                .with(new BurnComponent(6), new BurnPointsComponent())
                .register();
    }

    public Burnace() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.BURNACE);
        tags.add(CustomTags.MAGIC_BURN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.applyToEnemy(m, new BurnedPower(m, p, magicNumber));
        addToBot(new DoAction(() -> {
            for (int i = AbstractDungeon.getMonsters().monsters.size() - 1; i >= 0; i--) {
                AbstractMonster mon = AbstractDungeon.getMonsters().monsters.get(i);
                if (!mon.isDeadOrEscaped()) {
                    AbstractPower burn = mon.getPower(BurnedPower.POWER_ID);
                    if (burn != null) {
                        addToTop(new LoseHPAction(mon, p, burn.amount, AbstractGameAction.AttackEffect.FIRE));
                    }
                }
            }
        }));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SMOGMAGOG);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, DARK_GRAY, WHITE, DARK_GRAY, WHITE, false);
    }
}