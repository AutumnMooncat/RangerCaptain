package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
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

public class Pondwalker extends AbstractEasyCard {
    public final static String ID = makeID(Pondwalker.class.getSimpleName());

    static {
        // 13,6 -> 21,10
        new FusionComponentHelper(MonsterEnum.PONDWALKER)
                .withCost(2)
                .withDamage(6, AbstractGameAction.AttackEffect.FIRE)
                .with(new BurnComponent(3))
                .register();
        // 16,8 -> 24,14
        new FusionComponentHelper(MonsterEnum.SHARKTANKER)
                .withCost(2)
                .withDamage(7, AbstractGameAction.AttackEffect.FIRE)
                .with(new BurnComponent(4))
                .register();
    }

    public Pondwalker() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 13;
        baseMagicNumber = magicNumber = 6;
        setMonsterData(MonsterEnum.PONDWALKER);
        tags.add(CustomTags.MAGIC_BURN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        Wiz.applyToEnemy(m, new BurnedPower(m, p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeMagicNumber(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SHARKTANKER);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(GOLD), WHITE, darken(GOLD), WHITE, false);
    }
}