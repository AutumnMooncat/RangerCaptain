package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.interfaces.ThatManyComponent;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class DiscardCardsComponent extends AbstractComponent implements AbstractComponent.ComponentAmountProvider {
    public static final String ID = MainModfile.makeID(DiscardCardsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final String FOR_EACH = DESCRIPTION_TEXT[3];
    private int discardAmount;
    public static AbstractCard lastDiscarded;
    public boolean optional;
    public boolean random;

    public DiscardCardsComponent(int base) {
        this(base, false, false);
    }

    public DiscardCardsComponent(int base, boolean optional, boolean random) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, DynVar.MAGIC);
        this.optional = optional;
        this.random = random;
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof DiscardCardsComponent) {
            if (((DiscardCardsComponent) other).optional) {
                return true;
            }
            return !optional;
        }
        return false;
    }

    @Override
    public void receiveStacks(AbstractComponent other) {
        if (other instanceof DiscardCardsComponent) {
            optional |= ((DiscardCardsComponent) other).optional;
            if (other.baseAmount > baseAmount) {
                random |= ((DiscardCardsComponent) other).random;
            }
        }
        super.receiveStacks(other);
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return other.hasFlags(Flag.DISCARD_FOLLOWUP) || (other.hasFlags(Flag.THAT_MANY) && other instanceof ThatManyComponent);
    }

    @Override
    public String componentDescription() {
        if (random) {
            return DESCRIPTION_TEXT[2];
        } else if (optional) {
            return DESCRIPTION_TEXT[1];
        } else {
            return DESCRIPTION_TEXT[0];
        }
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        // TODO Capture fails if exhaust is captured by power
        String text;
        int index = random ? 6 : optional ? 3 : 0;
        if (dynvar == DynVar.FLAT) {
            text = baseAmount == 1 ? CARD_TEXT[index + 1] : String.format(CARD_TEXT[index + 2], baseAmount);
        } else {
            text = String.format(CARD_TEXT[index], dynKey());
        }
        return text + discardFollowupText(captured) + discardThatManyText(captured);
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        if (random) {
            addToBot(new DoAction(() -> {
                CardGroup source = Wiz.adp().hand;
                if (amount >= source.size()) {
                    doThatManyActions(p, m, captured, source.size());
                    for (AbstractCard card : source.group) {
                        doFollowupActions(provider, p, m, captured, card);
                        addToTop(new DiscardSpecificCardAction(card, source));
                    }
                } else {
                    List<AbstractCard> cards = new ArrayList<>(source.group);
                    doThatManyActions(p, m, captured, amount);
                    for (int i = 0; i < amount; i++) {
                        AbstractCard card = cards.remove(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
                        doFollowupActions(provider, p, m, captured, card);
                        addToTop(new DiscardSpecificCardAction(card, source));
                    }
                }
            }));
        } else {
            addToBot(new BetterSelectCardsInHandAction(amount, DiscardAction.TEXT[0], optional, optional, c -> true, cards -> {
                doThatManyActions(p, m, captured, cards.size());
                for (AbstractCard card : cards) {
                    doFollowupActions(provider, p, m, captured, card);
                    addToTop(new DiscardSpecificCardAction(card, p.hand));
                }
            }));
        }
    }

    private void doFollowupActions(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured, AbstractCard discarded) {
        lastDiscarded = discarded;
        ActionCapturePatch.doCapture = true;
        for (int i = captured.size() - 1; i >= 0; i--) {
            if (captured.get(i).hasFlags(Flag.DISCARD_FOLLOWUP)) {
                captured.get(i).onTrigger(provider, p, m, Collections.emptyList());
            }
        }
        lastDiscarded = null;
        ActionCapturePatch.releaseToTop();
    }

    private void doThatManyActions(AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured, int discardAmount) {
        this.discardAmount = discardAmount;
        if (discardAmount == 0) {
            return;
        }
        ActionCapturePatch.doCapture = true;
        for (int i = captured.size() - 1; i >= 0; i--) {
            if (captured.get(i).hasFlags(Flag.THAT_MANY)) {
                captured.get(i).onTrigger(this, p, m, Collections.emptyList());
            }
        }
        ActionCapturePatch.releaseToTop();
    }

    @Override
    public AbstractComponent makeCopy() {
        return new DiscardCardsComponent(baseAmount, optional, random);
    }

    public static String discardFollowupText(List<AbstractComponent> captured) {
        if (captured.stream().anyMatch(c -> c.hasFlags(Flag.DISCARD_FOLLOWUP))) {
            return LocalizedStrings.PERIOD + " NL " + FormatHelper.capitalize(StringUtils.join(captured.stream().filter(c -> c.hasFlags(Flag.DISCARD_FOLLOWUP)).map(c -> FormatHelper.uncapitalize(c.rawCardText(Collections.emptyList()))).collect(Collectors.toList()), " " + AND + " ")) + " " + FOR_EACH;
        }
        return "";
    }

    public static String discardThatManyText(List<AbstractComponent> captured) {
        if (captured.stream().anyMatch(c -> c instanceof ThatManyComponent)) {
            return LocalizedStrings.PERIOD + " NL " + FormatHelper.capitalize(StringUtils.join(captured.stream().filter(c -> c instanceof ThatManyComponent).map(c -> FormatHelper.uncapitalize(((ThatManyComponent) c).thatManyText())).collect(Collectors.toList()), " " + AND + " "));
        }
        return "";
    }

    @Override
    public int getAmount(AbstractComponent component) {
        return discardAmount;
    }
}
