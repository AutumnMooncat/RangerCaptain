package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.ForEachDebuffComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class Muskrateer extends AbstractEasyCard {
    public final static String ID = makeID(Muskrateer.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MUSKRATEER)
                .withCost(0)
                .withDamage(6, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .with(new ForEachDebuffComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.RATCOUSEL)
                .withCost(0)
                .withDamage(10, AbstractGameAction.AttackEffect.BLUNT_HEAVY)
                .with(new ForEachDebuffComponent())
                .register();
    }

    public Muskrateer() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 3;
        setMonsterData(MonsterEnum.MUSKRATEER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DoAction(() -> {
            if (m != null) {
                for (AbstractPower power : m.powers) {
                    if (power.type == AbstractPower.PowerType.DEBUFF) {
                        dmgTop(m, upgraded ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
                    }
                }
            }
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.RATCOUSEL);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }
}