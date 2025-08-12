package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.StashCardsAction;
import RangerCaptain.actions.StashRandomCardsAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StashCardsComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(StashCardsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final String IN_HAND = DESCRIPTION_TEXT[3];
    public static final String IN_DRAW = DESCRIPTION_TEXT[4];
    public static final String IN_DISCARD = DESCRIPTION_TEXT[5];
    public static final String FOR_EACH = DESCRIPTION_TEXT[6];
    public static AbstractCard lastStashed;

    public enum TargetPile {
        HAND,
        DRAW,
        DISCARD
    }

    public TargetPile pile;
    public boolean optional;
    public boolean random;

    public StashCardsComponent(float base) {
        this(base, TargetPile.HAND, false, false);
    }

    public StashCardsComponent(float base, TargetPile pile, boolean optional, boolean random) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, DynVar.MAGIC);
        this.pile = pile;
        this.optional = optional;
        this.random = random;
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof StashCardsComponent) {
            if (((StashCardsComponent) other).optional) {
                return true;
            }
            return !optional;
        }
        return false;
    }

    @Override
    public void receiveStacks(AbstractComponent other) {
        if (other instanceof StashCardsComponent) {
            optional |= ((StashCardsComponent) other).optional;
            if (other.workingAmount > workingAmount) {
                pile = ((StashCardsComponent) other).pile;
                random |= ((StashCardsComponent) other).random;
            }
        }
        super.receiveStacks(other);
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return other.hasFlags(Flag.STASH_FOLLOWUP);
    }

    @Override
    public String componentDescription() {
        String pileInsert = pile == TargetPile.HAND ? IN_HAND : pile == TargetPile.DRAW ? IN_DRAW : IN_DISCARD;
        if (random) {
            return String.format(DESCRIPTION_TEXT[2], pileInsert);
        } else if (optional) {
            return String.format(DESCRIPTION_TEXT[1], pileInsert);
        } else {
            return String.format(DESCRIPTION_TEXT[0], pileInsert);
        }
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        // TODO Capture fails if exhaust is captured by power
        String pileInsert = " " + (pile == TargetPile.HAND ? IN_HAND : pile == TargetPile.DRAW ? IN_DRAW : IN_DISCARD);
        if (pile == TargetPile.HAND && !random && !optional) {
            pileInsert = "";
        }
        String text;
        int index = random ? 6 : optional ? 3 : 0;
        if (dynvar == DynVar.FLAT) {
            text = workingAmount == 1 ? String.format(CARD_TEXT[index + 1], pileInsert) : String.format(CARD_TEXT[index + 2], workingAmount, pileInsert);
        } else {
            text = String.format(CARD_TEXT[index], dynKey(), pileInsert);
        }
        return text + stashFollowupText(captured);
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        CardGroup group = pile == TargetPile.HAND ? Wiz.adp().hand : pile == TargetPile.DRAW ? Wiz.adp().drawPile : Wiz.adp().discardPile;
        if (random) {
            addToBot(new StashRandomCardsAction(group, amount, c -> true, cards -> {
                for (AbstractCard card : cards) {
                    doCapturedActions(provider, p, m, captured, card);
                }
            }));
        } else {
            addToBot(new StashCardsAction(group, amount, optional, optional, c -> true, cards -> {
                for (AbstractCard card : cards) {
                    doCapturedActions(provider, p, m, captured, card);
                }
            }));
        }
    }

    private void doCapturedActions(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured, AbstractCard stashed) {
        lastStashed = stashed;
        ActionCapturePatch.doCapture = true;
        for (int i = captured.size() - 1; i >= 0; i--) {
            captured.get(i).onTrigger(provider, p, m, Collections.emptyList());
        }
        lastStashed = null;
        ActionCapturePatch.releaseToTop();
    }

    @Override
    public AbstractComponent makeCopy() {
        return new StashCardsComponent(baseAmount, pile, optional, random);
    }

    public static String stashFollowupText(List<AbstractComponent> captured) {
        if (captured.stream().anyMatch(c -> c.hasFlags(Flag.STASH_FOLLOWUP))) {
            return LocalizedStrings.PERIOD + " NL " + FormatHelper.capitalize(StringUtils.join(captured.stream().filter(c -> c.hasFlags(Flag.STASH_FOLLOWUP)).map(c -> FormatHelper.uncapitalize(c.rawCardText(Collections.emptyList()))).collect(Collectors.toList()), " " + AND + " ")) + " " + FOR_EACH;
        }
        return "";
    }
}
