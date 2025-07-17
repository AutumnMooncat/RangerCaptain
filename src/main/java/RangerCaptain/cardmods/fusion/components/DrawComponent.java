package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.util.FormatHelper;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DrawComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(DrawComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final String FOR_EACH = DESCRIPTION_TEXT[1];

    public DrawComponent(int base) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, DynVar.MAGIC);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return other.hasFlags(Flag.DRAW_FOLLOWUP);
    }

    @Override
    public void onCapture(AbstractComponent other) {
        if (other.type == ComponentType.DAMAGE) {
            setFlags(Flag.PSEUDO_DAMAGE);
        }
        if (other.target == ComponentTarget.ENEMY) {
            target = ComponentTarget.ENEMY;
        } else if (target != ComponentTarget.ENEMY) {
            if (other.target == ComponentTarget.ENEMY_RANDOM || other.target == ComponentTarget.ENEMY_AOE) {
                target = ComponentTarget.ENEMY_AOE;
            } else if (target != ComponentTarget.ENEMY_AOE && other.target == ComponentTarget.SELF) {
                target = ComponentTarget.SELF;
            }
        }
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        String text;
        if (dynvar == DynVar.FLAT) {
            text = baseAmount == 1 ? CARD_TEXT[1] : String.format(CARD_TEXT[2], baseAmount);
        } else {
            text = String.format(CARD_TEXT[0], dynKey());
        }
        return text + drawFollowupText(captured);
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        addToBot(new DrawCardAction(provider.getAmount(this), new DoAction(() -> {
            for (AbstractCard drawnCard : DrawCardAction.drawnCards) {
                ActionCapturePatch.doCapture = true;
                for (AbstractComponent cap : captured) {
                    cap.onTrigger(provider, p, m, Collections.emptyList());
                }
                ActionCapturePatch.releaseToTop();
            }
        })));
    }

    public static String drawFollowupText(List<AbstractComponent> captured) {
        if (captured.stream().anyMatch(c -> c.hasFlags(Flag.DRAW_FOLLOWUP))) {
            return LocalizedStrings.PERIOD + " NL " + FormatHelper.capitalize(StringUtils.join(captured.stream().filter(c -> c.hasFlags(Flag.DRAW_FOLLOWUP)).map(c -> FormatHelper.uncapitalize(c.rawCardText(Collections.emptyList()))).collect(Collectors.toList()), " " + AND + " ")) + " " + FOR_EACH;
        }
        return "";
    }

    @Override
    public AbstractComponent makeCopy() {
        return new DrawComponent(baseAmount);
    }
}
