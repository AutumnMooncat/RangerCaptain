package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class WhenExhaustedComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(WhenExhaustedComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public WhenExhaustedComponent() {
        super(ID, 0, ComponentType.DO, ComponentTarget.NONE, DynVar.NONE);
        setFlags(Flag.MUST_CAPTURE, Flag.CANT_BE_CAPTURED, Flag.REMOVE_IF_POWER);
    }

    @Override
    public void updatePrio() {
        priority = SUFFIX_PRIO + 1;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof WhenExhaustedComponent;
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return other.type == ComponentType.BLOCK || other.type == ComponentType.APPLY || other.type == ComponentType.DO;
    }

    @Override
    public void onCapture(AbstractComponent other) {
        if (other.target == ComponentTarget.ENEMY) {
            other.target = ComponentTarget.ENEMY_RANDOM;
            other.updatePrio();
        }
        if (other.target == ComponentTarget.ENEMY_RANDOM || other.target == ComponentTarget.ENEMY_AOE) {
            target = ComponentTarget.ENEMY_AOE;
        } else if (target != ComponentTarget.ENEMY_AOE && other.target == ComponentTarget.SELF) {
            target = ComponentTarget.SELF;
        }
        if (other.type == ComponentType.DAMAGE) {
            setFlags(Flag.PSEUDO_DAMAGE);
        }
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[0], assembleCapturedText(captured));
    }

    @Override
    public String rawCapturedText() {
        return null;
    }

    public String assembleCapturedText(List<AbstractComponent> captured) {
        List<String> parts = new ArrayList<>();
        List<AbstractComponent> selfTarget = captured.stream().filter(c -> c.target == ComponentTarget.SELF && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());
        List<AbstractComponent> enemyTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());
        List<AbstractComponent> randomTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY_RANDOM && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());
        List<AbstractComponent> aoeTarget = captured.stream().filter(c -> c.target == ComponentTarget.ENEMY_AOE && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());
        List<AbstractComponent> noTarget = captured.stream().filter(c -> c.target == ComponentTarget.NONE && !StringUtils.isEmpty(c.rawCapturedText())).collect(Collectors.toList());

        // Self
        // Gain %s Block and (Gain) %s <effect>, Take %s damage, other
        parts.addAll(assembleCapturedGainText(selfTarget));

        // Enemy
        // Give %s Block and Deal %s damage and Apply %s <effect> and other <target>
        parts.addAll(assembleCapturedText(enemyTarget, null));

        // Random
        // Give %s Block and Deal %s damage and Apply %s <effect> and other to a random enemy
        parts.addAll(assembleCapturedText(randomTarget, RANDOM_ENEMY));

        // AOE
        // Give %s Block and Deal %s damage and Apply %s <effect> and other to ALL enemies
        parts.addAll(assembleCapturedText(aoeTarget, ALL_ENEMIES));

        // None
        // Just list as is
        for (AbstractComponent component : noTarget) {
            parts.add(component.rawCapturedText());
        }

        String text = compressCapturedText(parts);

        if (text.isEmpty()) {
            text = "Invalid When Exhausted";
        }

        return text;
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {}

    @Override
    public void triggerOnExhaust(FusedCard card, List<AbstractComponent> captured) {
        ActionCapturePatch.doCapture = true;
        for (int i = captured.size() - 1; i >= 0; i--) {
            captured.get(i).onTrigger(card, Wiz.adp(), null, Collections.emptyList());
        }
        ActionCapturePatch.releaseToTop();
    }

    @Override
    public AbstractComponent makeCopy() {
        return new WhenExhaustedComponent();
    }
}
