package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.OnPerformFusionComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.powers.interfaces.OnFusionPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import java.util.List;

public class GlassBondsPower extends AbstractComponentPower implements OnFusionPower {
    public static final String POWER_ID = MainModfile.makeID(GlassBondsPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public GlassBondsPower(AbstractCreature owner, int amount) {
        super(POWER_ID, NAME, PowerType.BUFF, false, owner, amount);
    }

    public GlassBondsPower(AbstractCreature owner, String name, OnPerformFusionComponent source, List<AbstractComponent> captured) {
        super(POWER_ID, name, PowerType.BUFF, false, owner, source, captured);
    }

    @Override
    public void updateNormalDescription() {
        this.description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onPerformFusion(AbstractCard base, AbstractCard donor, FusedCard fused) {
        flash();
        if (source == null) {
            addToBot(new DamageAllEnemiesAction(null, DamageInfo.createDamageMatrix(amount, true), DamageInfo.DamageType.THORNS, AbstractGameAction.AttackEffect.SLASH_HEAVY, true));
        } else {
            triggerComponents(null, true);
        }
    }
}