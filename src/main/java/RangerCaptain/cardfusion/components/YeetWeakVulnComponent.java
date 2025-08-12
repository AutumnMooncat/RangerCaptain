package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.actions.FormalComplaintAction;
import RangerCaptain.actions.ThrowPowerAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import java.util.Collections;
import java.util.List;

public class YeetWeakVulnComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(YeetWeakVulnComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public YeetWeakVulnComponent() {
        super(ID, 0, ComponentType.DO, ComponentTarget.ENEMY, DynVar.NONE);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof YeetWeakVulnComponent) {
            return dynvar == other.dynvar;
        }
        return false;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[target.ordinal()], dynKey());
    }

    @Override
    public String rawCapturedText() {
        return target == ComponentTarget.NONE ? rawCardText(Collections.emptyList()) : CARD_TEXT[ComponentTarget.values().length];
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        switch (target) {
            case ENEMY:
                addToBot(new FormalComplaintAction(m));
                break;
            case ENEMY_RANDOM:
                addToBot(new DoAction(() -> {
                    addToTop(new FormalComplaintAction(AbstractDungeon.getRandomMonster()));
                }));
                break;
            case ENEMY_AOE:
                addToBot(new DoAction(() -> {
                    AbstractPower vuln = p.getPower(VulnerablePower.POWER_ID);
                    if (vuln != null) {
                        Wiz.forAllMonstersLiving(mon -> addToTop(new ThrowPowerAction(mon, new VulnerablePower(mon, vuln.amount, false))));
                        addToTop(new RemoveSpecificPowerAction(p, p, vuln));
                    }

                    AbstractPower weak = p.getPower(WeakPower.POWER_ID);
                    if (weak != null) {
                        Wiz.forAllMonstersLiving(mon -> addToTop(new ThrowPowerAction(mon, new WeakPower(mon, weak.amount, false))));
                        addToTop(new RemoveSpecificPowerAction(p, p, weak));
                    }
                }));
                break;
            case NONE:
                addToBot(new RemoveSpecificPowerAction(p, p, WeakPower.POWER_ID));
                addToBot(new RemoveSpecificPowerAction(p, p, VulnerablePower.POWER_ID));
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new YeetWeakVulnComponent();
    }
}
