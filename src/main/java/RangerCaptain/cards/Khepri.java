package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.ExhaustCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Khepri extends AbstractEasyCard {
    public final static String ID = makeID(Khepri.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.KHEPRI)
                .withCost(0)
                .withDamage(6, AbstractGameAction.AttackEffect.FIRE)
                .with(new ExhaustCardsComponent(1))
                .withCloseEncounter()
                .register();
    }

    public Khepri() {
        super(ID, 0, CardType.ATTACK, CardRarity.RARE, CardTarget.ENEMY);
        baseDamage = damage = 6;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.KHEPRI);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
        tags.add(CustomTags.CLOSE_ENCOUNTER);
        tags.add(CustomTags.MAGIC_EXHAUST);
        ExtraEffectPatches.EffectFields.closeEncounter.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.FIRE);
        addToBot(new ExhaustAction(magicNumber, false, false, false));
    }

    @Override
    public void upp() {}

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BLUE), WHITE, darken(BLUE), WHITE, false);
    }
}