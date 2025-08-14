package RangerCaptain.cards;

import RangerCaptain.actions.FormalComplaintAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.YeetWeakVulnComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Spirouette extends AbstractEasyCard {
    public final static String ID = makeID(Spirouette.class.getSimpleName());

    static {
        // 15 -> 20
        new FusionComponentHelper(MonsterEnum.SPIROUETTE)
                .withCost(2)
                .withDamage(10, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new YeetWeakVulnComponent())
                .register();
        // 20 -> 27
        new FusionComponentHelper(MonsterEnum.REGENSEA)
                .withCost(2)
                .withDamage(13.5f, AbstractGameAction.AttackEffect.SLASH_HEAVY)
                .with(new YeetWeakVulnComponent())
                .register();
    }

    public Spirouette() {
        super(ID, 2, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 15;
        setMonsterData(MonsterEnum.SPIROUETTE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_HEAVY);
        addToBot(new FormalComplaintAction(m));
    }

    @Override
    public void upp() {
        upgradeDamage(5);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.REGENSEA);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(Color.PINK), WHITE, darken(Color.PINK), WHITE, false);
    }
}