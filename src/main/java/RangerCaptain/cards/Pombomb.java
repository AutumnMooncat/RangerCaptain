package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.BurnComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.BurnedPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Pombomb extends AbstractEasyCard {
    public final static String ID = makeID(Pombomb.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.POMBOMB)
                .withCost(1)
                .withDamageAOE(4, AbstractGameAction.AttackEffect.FIRE)
                .with(new BurnComponent(2, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
        new FusionComponentHelper(MonsterEnum.SPITZFYRE)
                .withCost(1)
                .withDamageAOE(6, AbstractGameAction.AttackEffect.FIRE)
                .with(new BurnComponent(3, AbstractComponent.ComponentTarget.ENEMY_AOE))
                .register();
    }

    public Pombomb() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ALL_ENEMY);
        baseDamage = damage = 5;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.POMBOMB);
        isMultiDamage = true;
        tags.add(CustomTags.AOE_DAMAGE);
        tags.add(CustomTags.MAGIC_BURN_AOE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        allDmg(AbstractGameAction.AttackEffect.FIRE);
        Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new BurnedPower(mon, p, magicNumber)));
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SPITZFYRE);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BLUE), WHITE, darken(BLUE), WHITE, false);
    }
}