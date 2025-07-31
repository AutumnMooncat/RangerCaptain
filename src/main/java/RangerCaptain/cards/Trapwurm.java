package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.ExhaustAttacksComponent;
import RangerCaptain.cardmods.fusion.components.ScaleDamageComponent;
import RangerCaptain.cardmods.fusion.components.vfx.BiteVFXComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import static RangerCaptain.MainModfile.makeID;

public class Trapwurm extends AbstractEasyCard {
    public final static String ID = makeID(Trapwurm.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.TRAPWURM)
                .withCost(1)
                .withDamage(8, AbstractGameAction.AttackEffect.NONE)
                .with(new BiteVFXComponent())
                .with(new ExhaustAttacksComponent(1))
                .withFlags(new ScaleDamageComponent(), AbstractComponent.Flag.EXHAUST_COMPLEX_FOLLOWUP)
                .register();
        new FusionComponentHelper(MonsterEnum.WYRMAW)
                .withCost(1)
                .withDamage(11, AbstractGameAction.AttackEffect.NONE)
                .with(new BiteVFXComponent())
                .with(new ExhaustAttacksComponent(1))
                .withFlags(new ScaleDamageComponent(), AbstractComponent.Flag.EXHAUST_COMPLEX_FOLLOWUP)
                .register();
    }

    public Trapwurm() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 11;
        setMonsterData(MonsterEnum.TRAPWURM);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m != null) {
            addToBot(new VFXAction(new BiteEffect(m.hb.cX, m.hb.cY - 40.0F * Settings.scale, Settings.GOLD_COLOR.cpy()), 0.1F));
        }
        dmg(m, AbstractGameAction.AttackEffect.NONE);
        addToBot(new BetterSelectCardsInHandAction(1, ExhaustAction.TEXT[0], false, false, c -> c.type == CardType.ATTACK, cards -> {
            for (AbstractCard card : cards) {
                baseDamage += card.baseDamage;
                addToTop(new ExhaustSpecificCardAction(card, p.hand, true));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.WYRMAW);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BRONZE), WHITE, darken(BRONZE), WHITE, false);
    }
}