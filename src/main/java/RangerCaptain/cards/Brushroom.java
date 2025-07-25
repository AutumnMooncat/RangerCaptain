package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.DamageAlreadyAttackedComponent;
import RangerCaptain.cardmods.fusion.components.VulnerableComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import static RangerCaptain.MainModfile.makeID;

public class Brushroom extends AbstractEasyCard {
    public final static String ID = makeID(Brushroom.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BRUSHROOM)
                .withCost(1)
                .with(new DamageAlreadyAttackedComponent(7, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new VulnerableComponent(2))
                .register();
        new FusionComponentHelper(MonsterEnum.FUNGOGH)
                .withCost(1)
                .with(new DamageAlreadyAttackedComponent(2, AbstractGameAction.AttackEffect.BLUNT_HEAVY))
                .with(new VulnerableComponent(1))
                .register();
    }

    public Brushroom() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 9;
        baseMagicNumber = magicNumber = 2;
        setMonsterData(MonsterEnum.BRUSHROOM);
        tags.add(CustomTags.MAGIC_VULN);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_HEAVY);
        Wiz.applyToEnemy(m, new VulnerablePower(m, magicNumber, false));
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        boolean canUse = super.canUse(p, m);
        if (!canUse) {
            return false;
        } else {
            if (m != null && CardCounterPatches.AttackCountField.attackedThisTurn.get(m) == 0) {
                canUse = false;
                cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[1];
            }
            return canUse;
        }
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FUNGOGH);
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