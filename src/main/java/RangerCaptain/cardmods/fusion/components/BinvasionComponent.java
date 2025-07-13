package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cards.Binvader;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;

import java.util.List;

public class BinvasionComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(BinvasionComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final AbstractGameAction.AttackEffect effect;
    public int binvaderCount;

    public BinvasionComponent(int base, AbstractGameAction.AttackEffect effect) {
        this(base, 1, effect, ComponentTarget.ENEMY);
    }

    public BinvasionComponent(int base, int binvaderCount, AbstractGameAction.AttackEffect effect, ComponentTarget target) {
        super(ID, base, target == ComponentTarget.SELF ? ComponentType.APPLY : ComponentType.DAMAGE, target, target == ComponentTarget.SELF ? DynVar.MAGIC : DynVar.DAMAGE);
        this.effect = effect;
        this.binvaderCount = binvaderCount;
        isSimple = true;
    }

    @Override
    public void updatePrio() {
        if (target == ComponentTarget.SELF) {
            priority = DO_PRIO;
        } else {
            priority = DAMAGE_PRIO + target.ordinal();
        }
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof BinvasionComponent) {
            if (target == other.target) {
                return true;
            }
            if (other.target == ComponentTarget.ENEMY_AOE || other.target == ComponentTarget.ENEMY_RANDOM) {
                return target == ComponentTarget.ENEMY;
            }
        }
        return super.shouldStack(other);
    }

    @Override
    public void receiveStacks(AbstractComponent other) {
        float mult = 0.7f;
        if (other instanceof BinvasionComponent) {
            binvaderCount += ((BinvasionComponent) other).binvaderCount;
            mult = 1f;
        }
        if (target == ComponentTarget.ENEMY_AOE && other.target == ComponentTarget.ENEMY) {
            mult *= 0.75f;
        }
        baseAmount += (int) (other.baseAmount * mult);
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        // TODO Bin dynvar to update text?
        return String.format(CARD_TEXT[target.ordinal()], dynKey());
    }

    @Override
    public String rawCapturedText() {
        return String.format(CARD_TEXT[ComponentTarget.values().length], dynKey());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        // TODO damage followups
        int amount = provider.getAmount(this);
        int hits = Binvader.binvasionCount();
        DamageInfo.DamageType dt = provider instanceof AbstractCard ? ((AbstractCard) provider).damageTypeForTurn : DamageInfo.DamageType.THORNS;
        switch (target) {
            case SELF:
                for (int i = 0; i < hits; i++) {
                    Wiz.sequenceActions(false,
                            new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F),
                            new VFXAction(new BorderFlashEffect(Color.SKY)),
                            new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, p.hb.cX, p.hb.cY), 0.1F),
                            new DamageAction(p, new DamageInfo(p, amount, dt), effect));
                }
                break;
            case ENEMY:
                if (m == null) {
                    break;
                }
                for (int i = 0; i < hits; i++) {
                    Wiz.sequenceActions(false,
                            new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F),
                            new VFXAction(new BorderFlashEffect(Color.SKY)),
                            new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, m.hb.cX, m.hb.cY), 0.1F),
                            new DamageAction(m, new DamageInfo(p, amount, dt), effect));
                }
                break;
            case ENEMY_RANDOM:
                // TODO effect may not match text if captured
                if (provider instanceof AbstractCard) {
                    for (int i = 0; i < hits; i++) {
                        addToBot(new DoAction(() -> {
                            AbstractMonster mon = AbstractDungeon.getRandomMonster();
                            if (mon == null) {
                                return;
                            }
                            Wiz.sequenceActions(true,
                                    new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F),
                                    new VFXAction(new BorderFlashEffect(Color.SKY)),
                                    new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, mon.hb.cX, mon.hb.cY), 0.1F),
                                    new DamageAction(mon, new DamageInfo(p, amount, dt), effect));
                        }));
                    }
                }
                else {
                    addToBot(new DoAction(() -> {
                        AbstractMonster mon = AbstractDungeon.getMonsters().getRandomMonster();
                        if (mon != null) {
                            for (int i = 0; i < hits; i++) {
                                Wiz.sequenceActions(true,
                                        new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F),
                                        new VFXAction(new BorderFlashEffect(Color.SKY)),
                                        new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, mon.hb.cX, mon.hb.cY), 0.1F),
                                        new DamageAction(mon, new DamageInfo(p, amount, dt), effect));
                            }
                        }
                    }));
                }
                break;
            case ENEMY_AOE:
                int[] damages = DamageInfo.createDamageMatrix(amount, true);
                if (provider instanceof AbstractCard) {
                    damages = ((AbstractCard) provider).multiDamage;
                    if (((AbstractCard) provider).cost == -1) {
                        int effect = EnergyPanel.totalCount;
                        if (((AbstractCard) provider).energyOnUse != -1) {
                            effect = ((AbstractCard) provider).energyOnUse;
                        }

                        if (Wiz.adp().hasRelic("Chemical X")) {
                            effect += 2;
                            Wiz.adp().getRelic("Chemical X").flash();
                        }
                        for (int i = 0 ; i < damages.length ; i++) {
                            damages[i] *= effect;
                        }
                    }
                }
                for (int i = 0; i < hits; i++) {
                    for (int j = 0; j < AbstractDungeon.getMonsters().monsters.size(); j++) {
                        AbstractMonster mon = AbstractDungeon.getMonsters().monsters.get(j);
                        if (mon == null) {
                            continue;
                        }
                        Wiz.sequenceActions(false,
                                new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F),
                                new VFXAction(new BorderFlashEffect(Color.SKY)),
                                new VFXAction(new SmallLaserEffect(p.hb.cX, p.hb.cY, mon.hb.cX, mon.hb.cY), 0.1F),
                                new DamageAction(mon, new DamageInfo(p, damages[j], dt), effect));
                    }
                }
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new BinvasionComponent(baseAmount, binvaderCount, effect, target);
    }
}
