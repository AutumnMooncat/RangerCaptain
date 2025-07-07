package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.ResonanceComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ResonancePower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Puppercut extends AbstractEasyCard {
    public final static String ID = makeID(Puppercut.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.PUPPERCUT)
                .withCost(0)
                .withDamage(4, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                .with(new ResonanceComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.SOUTHPAW)
                .withCost(0)
                .withDamage(7, AbstractGameAction.AttackEffect.BLUNT_LIGHT)
                .with(new ResonanceComponent(1))
                .register();
    }

    public Puppercut() {
        super(ID, 0, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 4;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.PUPPERCUT);
        tags.add(CustomTags.MAGIC_RESONANCE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        Wiz.applyToEnemy(m, new ResonancePower(m, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SOUTHPAW);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(RED), WHITE, darken(RED), WHITE, false);
    }
}