package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.util.FormatHelper;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.Collections;
import java.util.List;

public class ForEachDebuffComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(ForEachDebuffComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public ForEachDebuffComponent() {
        super(ID, 0, ComponentType.DO, ComponentTarget.ENEMY, DynVar.NONE);
        setFlags(Flag.MUST_CAPTURE, Flag.REMOVE_IF_POWER, Flag.CANT_BE_CAPTURED);
    }

    @Override
    public void updatePrio() {
        priority = DAMAGE_PRIO - 1;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof ForEachDebuffComponent;
    }

    @Override
    public boolean captures(AbstractComponent other) {
        if (other.makesActions()) {
            return other.target == ComponentTarget.ENEMY && !other.hasFlags(Flag.RANDOM_WHEN_CAPTURED);
        }
        return false;
    }

    @Override
    public void onCapture(AbstractComponent other) {
        super.onCapture(other);
        if (other.type == ComponentType.DAMAGE) {
            setFlags(Flag.PSEUDO_DAMAGE);
        }
    }

    @Override
    public boolean modifiesAmount(AbstractComponent other) {
        return capturedComponents.contains(other);
    }

    @Override
    public float amountMultiplier(AbstractComponent other) {
        return 0.7f;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return FormatHelper.capitalize(String.format(CARD_TEXT[0], effectText(captured)));
    }

    @Override
    public String rawCapturedText() {
        return rawCardText(Collections.emptyList());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        addToBot(new DoAction(() -> {
            if (m != null) {
                for (AbstractPower power : m.powers) {
                    if (power.type == AbstractPower.PowerType.DEBUFF) {
                        doCapturedActions(provider, p, m, captured);
                    }
                }
            }
        }));
    }

    private void doCapturedActions(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        ActionCapturePatch.doCapture = true;
        for (int i = captured.size() - 1; i >= 0; i--) {
            captured.get(i).onTrigger(provider, p, m, Collections.emptyList());
        }
        ActionCapturePatch.releaseToTop();
    }

    @Override
    public AbstractComponent makeCopy() {
        return new ForEachDebuffComponent();
    }

    public static String effectText(List<AbstractComponent> captured) {
        String text = compressCapturedText(assembleCapturedText(captured, null));
        if (text.isEmpty()) {
            text = "Invalid For Each Debuff";
        }
        return text;
    }
}
