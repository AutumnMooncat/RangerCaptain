package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.patches.CardCounterPatches;
import RangerCaptain.util.FormatHelper;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DamageLastAttackerComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(DamageLastAttackerComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final AbstractGameAction.AttackEffect effect;

    public DamageLastAttackerComponent(int base, AbstractGameAction.AttackEffect effect) {
        super(ID, base, ComponentType.DAMAGE, ComponentTarget.ENEMY_RANDOM, DynVar.DAMAGE);
        this.effect = effect;
        setFlags(Flag.CANT_COLLAPSE_TARGET_TEXT);
    }

    @Override
    public void updatePrio() {
        priority = DAMAGE_PRIO + 5;
    }

    @Override
    public void receiveStacks(AbstractComponent other) {
        float mult = 1f;
        if (!(other instanceof DamageLastAttackerComponent)) {
            mult *= 1.25f;
        }
        floatingAmount += other.floatingAmount * mult;
    }

    @Override
    public boolean captures(AbstractComponent other) {
        if (other.type == ComponentType.APPLY && other.isSimple) {
            return other.target == ComponentTarget.ENEMY || other.target == ComponentTarget.ENEMY_RANDOM;
        }
        return false;
    }

    @Override
    public void onCapture(AbstractComponent other) {
        if (other.target == ComponentTarget.ENEMY_RANDOM) {
            other.target = ComponentTarget.ENEMY;
        }
    }

    @Override
    public float amountMultiplier(AbstractComponent other) {
        return 1.25f;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        String insert = "";
        List<String> parts = captured.stream().map(AbstractComponent::rawCapturedText).collect(Collectors.toList());
        if (!parts.isEmpty()) {
            insert = AND_APPLY + " " + StringUtils.join(parts, " " + AND + " ") + " ";
        }
        return String.format(CARD_TEXT[0], dynKey(), insert);
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        DamageInfo.DamageType dt = provider instanceof AbstractCard ? ((AbstractCard) provider).damageTypeForTurn : DamageInfo.DamageType.THORNS;
        AbstractCreature lastAttacker = CardCounterPatches.lastAttacker;
        if (lastAttacker instanceof AbstractMonster && !lastAttacker.isDeadOrEscaped()) {
            addToBot(new DamageAction(lastAttacker, new DamageInfo(p, amount, dt), effect));
            for (AbstractComponent cap : captured) {
                cap.onTrigger(provider, p, (AbstractMonster) lastAttacker, Collections.emptyList());
            }
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new DamageLastAttackerComponent(baseAmount, effect);
    }
}
