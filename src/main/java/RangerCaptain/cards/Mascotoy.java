package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.StashNextCardComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.powers.InterceptingPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Mascotoy extends AbstractEasyCard {
    public final static String ID = makeID(Mascotoy.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MASCOTOY)
                .withCost(1)
                .withDamage(6.5f, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new StashNextCardComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.MASCOTORN)
                .withCost(1)
                .withDamage(8, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new StashNextCardComponent(1))
                .register();
    }

    public Mascotoy() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 9;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.MASCOTOY);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
        Wiz.applyToSelf(new InterceptingPower(p, magicNumber));
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MASCOTORN);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, Color.PINK, WHITE, Color.PINK, WHITE, false);
    }
}