package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.BetterSelectCardsCenteredAction;
import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.patches.ActionCapturePatch;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
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

public class ExhaustCardsComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(ExhaustCardsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final String IN_HAND = DESCRIPTION_TEXT[3];
    public static final String IN_DRAW = DESCRIPTION_TEXT[4];
    public static final String IN_DISCARD = DESCRIPTION_TEXT[5];
    public static final String FOR_EACH = DESCRIPTION_TEXT[6];
    public static AbstractCard lastExhausted;

    public enum TargetPile {
        HAND,
        DRAW,
        DISCARD
    }

    public TargetPile pile;
    public boolean optional;
    public boolean random;

    public ExhaustCardsComponent(float base) {
        this(base, TargetPile.HAND, false, false);
    }

    public ExhaustCardsComponent(float base, TargetPile pile, boolean optional, boolean random) {
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
        if (other instanceof ExhaustAttacksComponent) {
            return true;
        }
        if (other instanceof ExhaustCardsComponent) {
            if (((ExhaustCardsComponent) other).optional) {
                return true;
            }
            return !optional;
        }
        return false;
    }

    @Override
    public void receiveStacks(AbstractComponent other) {
        if (other instanceof ExhaustCardsComponent) {
            optional |= ((ExhaustCardsComponent) other).optional;
            if (other.workingAmount > workingAmount) {
                pile = ((ExhaustCardsComponent) other).pile;
                random |= ((ExhaustCardsComponent) other).random;
            }
        }
        super.receiveStacks(other);
    }

    @Override
    public boolean captures(AbstractComponent other) {
        return other.hasFlags(Flag.EXHAUST_FOLLOWUP) || other.hasFlags(Flag.EXHAUST_COMPLEX_FOLLOWUP);
    }

    @Override
    public void onCapture(AbstractComponent other) {
        super.onCapture(other);
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
        if (pile == TargetPile.HAND && !random && !(optional && workingAmount != 1)) {
            pileInsert = "";
        }
        String text;
        int index = random ? 6 : optional ? 3 : 0;
        if (dynvar == DynVar.FLAT) {
            text = workingAmount == 1 ? String.format(CARD_TEXT[index + 1], pileInsert) : String.format(CARD_TEXT[index + 2], workingAmount, pileInsert);
        } else {
            text = String.format(CARD_TEXT[index], dynKey(), pileInsert);
        }
        return text + ((optional && workingAmount == 1) ? exhaustYouMayFollowupText(captured): exhaustFollowupText(captured));
    }

    @Override
    public String rawCapturedText() {
        return rawCardText(Collections.emptyList());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        if (random) {
            addToBot(new DoAction(() -> {
                CardGroup source = pile == TargetPile.HAND ? Wiz.adp().hand : pile == TargetPile.DRAW ? Wiz.adp().drawPile : Wiz.adp().discardPile;
                if (amount >= source.size()) {
                    for (AbstractCard card : source.group) {
                        doCapturedActions(provider, p, m, captured, card);
                        addToTop(new ExhaustSpecificCardAction(card, source, true));
                    }
                } else {
                    List<AbstractCard> cards = new ArrayList<>(source.group);
                    for (int i = 0; i < amount; i++) {
                        AbstractCard card = cards.remove(AbstractDungeon.cardRandomRng.random(cards.size() - 1));
                        doCapturedActions(provider, p, m, captured, card);
                        addToTop(new ExhaustSpecificCardAction(card, source, true));
                    }
                }
            }));
        } else {
            switch (pile) {
                case HAND:
                    addToBot(new BetterSelectCardsInHandAction(amount, ExhaustAction.TEXT[0], optional, optional, c -> true, cards -> {
                        for (AbstractCard card : cards) {
                            doCapturedActions(provider, p, m, captured, card);
                            addToTop(new ExhaustSpecificCardAction(card, p.hand, true));
                        }
                    }));
                    break;
                case DRAW:
                    addToBot(new BetterSelectCardsCenteredAction(Wiz.adp().drawPile.group, amount, ExhaustAction.TEXT[0], optional, c -> true, cards -> {
                        for (AbstractCard card : cards) {
                            doCapturedActions(provider, p, m, captured, card);
                            addToTop(new ExhaustSpecificCardAction(card, p.drawPile, true));
                        }
                    }));
                    break;
                case DISCARD:
                    addToBot(new BetterSelectCardsCenteredAction(Wiz.adp().discardPile.group, amount, ExhaustAction.TEXT[0], optional, c -> true, cards -> {
                        for (AbstractCard card : cards) {
                            doCapturedActions(provider, p, m, captured, card);
                            addToTop(new ExhaustSpecificCardAction(card, p.discardPile, true));
                        }
                    }));
                    break;
            }
        }
    }

    private void doCapturedActions(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured, AbstractCard exhausted) {
        lastExhausted = exhausted;
        ActionCapturePatch.doCapture = true;
        for (int i = captured.size() - 1; i >= 0; i--) {
            captured.get(i).onTrigger(provider, p, m, Collections.emptyList());
        }
        lastExhausted = null;
        ActionCapturePatch.releaseToTop();
    }

    @Override
    public AbstractComponent makeCopy() {
        return new ExhaustCardsComponent(baseAmount, pile, optional, random);
    }

    public static String exhaustFollowupText(List<AbstractComponent> captured) {
        StringBuilder text = new StringBuilder();
        if (captured.stream().anyMatch(c -> c.hasFlags(Flag.EXHAUST_FOLLOWUP))) {
            text.append(LocalizedStrings.PERIOD).append(" NL ").append(FormatHelper.capitalize(StringUtils.join(captured.stream().filter(c -> c.hasFlags(Flag.EXHAUST_FOLLOWUP)).map(c -> FormatHelper.uncapitalize(c.rawCardText(Collections.emptyList()))).collect(Collectors.toList()), " " + AND + " "))).append(" ").append(FOR_EACH);
        }
        for (AbstractComponent component : captured) {
            if (component.hasFlags(Flag.EXHAUST_COMPLEX_FOLLOWUP)) {
                text.append(LocalizedStrings.PERIOD).append(" NL ").append(component.rawCardText(Collections.emptyList()));
            }
        }
        return text.toString();
    }

    public static String exhaustYouMayFollowupText(List<AbstractComponent> captured) {
        StringBuilder text = new StringBuilder();
        if (captured.stream().anyMatch(c -> c.hasFlags(Flag.EXHAUST_FOLLOWUP))) {
            text.append(" ").append(StringUtils.join(captured.stream().filter(c -> c.hasFlags(Flag.EXHAUST_FOLLOWUP)).map(c -> FormatHelper.uncapitalize(c.rawCardText(Collections.emptyList()))).collect(Collectors.toList()), " " + AND + " "));
        }
        for (AbstractComponent component : captured) {
            if (component.hasFlags(Flag.EXHAUST_COMPLEX_FOLLOWUP)) {
                text.append(LocalizedStrings.PERIOD).append(" NL ").append(component.rawCardText(Collections.emptyList()));
            }
        }
        return text.toString();
    }
}
