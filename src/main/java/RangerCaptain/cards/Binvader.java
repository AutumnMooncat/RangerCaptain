package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.BinvasionComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.powers.AbstractComponentPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import static RangerCaptain.MainModfile.makeID;

public class Binvader extends AbstractEasyCard {
    public final static String ID = makeID(Binvader.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BINVADER)
                .withCost(1)
                .with(new BinvasionComponent(4.5f, AbstractGameAction.AttackEffect.BLUNT_LIGHT))
                .register();
        new FusionComponentHelper(MonsterEnum.BINTERLOPER)
                .withCost(1)
                .with(new BinvasionComponent(6.5f, AbstractGameAction.AttackEffect.BLUNT_LIGHT))
                .register();
    }

    public Binvader() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 6;
        setMonsterData(MonsterEnum.BINVADER);
        baseInfo = info = 0;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            for (int i = 0 ; i < binvasionCount() ; i++) {
                addToBot(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
                addToBot(new VFXAction(new BorderFlashEffect(Color.SKY)));
                addToBot(new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.1F));
                dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            }
        }
    }

    public static int binvasionCount() {
        return (int) Wiz.getAllCardsInCardGroups(true, true).stream().filter(card -> card instanceof Binvader).count()
                + Wiz.getAllCardsInCardGroups(true, true).stream().filter(card -> card instanceof FusedCard).map(c -> ((FusedCard) c).binvasionCount()).reduce(0, Integer::sum)
                + Wiz.adp().powers.stream().filter(p -> p instanceof AbstractComponentPower && ((AbstractComponentPower) p).captured != null).flatMap(p -> ((AbstractComponentPower) p).captured.stream()).filter(c -> c instanceof BinvasionComponent).map(bd -> ((BinvasionComponent) bd).binvaderCount).reduce(0, Integer::sum);
    }

    @Override
    protected void applyPowersToBlock() {
        super.applyPowersToBlock();
        info = baseInfo = binvasionCount();
    }

    @Override
    public void upp() {
        upgradeDamage(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.BINTERLOPER);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, GRAY, WHITE, GRAY, WHITE, false);
    }
}