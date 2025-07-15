package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.util.FormatHelper;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MakeCardsComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(MakeCardsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final String HAND = DESCRIPTION_TEXT[1];
    public static final String DRAW = DESCRIPTION_TEXT[2];
    public static final String DISCARD = DESCRIPTION_TEXT[3];
    public static final String PILE = DESCRIPTION_TEXT[4];
    public static final String PILES = DESCRIPTION_TEXT[5];
    public static final String INVALID_LOCATION = DESCRIPTION_TEXT[6];
    public static final String INVALID_REFERENCE = DESCRIPTION_TEXT[7];
    public static final String S = DESCRIPTION_TEXT[8];

    public enum Location {
        HAND,
        DRAW,
        DISCARD
    }

    public final ArrayList<Location> locations = new ArrayList<>();
    public final String cardID;
    public final boolean pluralize;
    private transient AbstractCard reference;

    public MakeCardsComponent(int base, AbstractCard card, boolean pluralize) {
        this(base, card, pluralize, Location.HAND);
    }

    public MakeCardsComponent(int base, AbstractCard card, boolean pluralize, Location... locations) {
        this(base, card.cardID, pluralize, locations);
    }

    public MakeCardsComponent(int base, String cardID, boolean pluralize, Location... locations) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, DynVar.MAGIC);
        this.locations.addAll(Arrays.stream(locations).distinct().collect(Collectors.toList()));
        this.cardID = cardID;
        this.reference = CardLibrary.getCard(cardID);
        this.pluralize = pluralize;
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public boolean scalesWithCost() {
        return false;
    }

    @Override
    public void postAssignment(FusedCard card, List<AbstractComponent> otherComponents) {
        MultiCardPreview.add(card, reference);
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        if (other instanceof MakeCardsComponent) {
            if (!cardID.equals(((MakeCardsComponent) other).cardID)) {
                return false;
            }
        }
        return super.shouldStack(other);
    }

    @Override
    public void receiveStacks(AbstractComponent other) {
        if (other instanceof MakeCardsComponent) {
            ArrayList<Location> combined = new ArrayList<>(locations);
            combined.addAll(((MakeCardsComponent) other).locations);
            locations.clear();
            locations.addAll(combined.stream().distinct().collect(Collectors.toList()));
        }
        super.receiveStacks(other);
    }

    private String locationText() {
        String text = "";
        if (locations.isEmpty()) {
            return INVALID_LOCATION;
        }
        boolean hasHand = locations.contains(Location.HAND);
        boolean hasDraw = locations.contains(Location.DRAW);
        boolean hasDiscard = locations.contains(Location.DISCARD);
        if (hasHand) {
            text += HAND;
            if (hasDraw && hasDiscard) {
                text += ", " + DRAW + ", " + AND + " " + DISCARD + " " + PILES;
            } else if (hasDraw) {
                text += " " + AND + " " + DRAW + " " + PILES;
            } else if (hasDiscard) {
                text += " " + AND + " " + DISCARD + " " + PILES;
            }
        } else if (hasDraw) {
            text += DRAW;
            if (hasDiscard) {
                text += " " + AND + " " + DISCARD + " " + PILES;
            } else {
                text += " " + PILE;
            }
        } else {
            text += DISCARD + " " + PILE;
        }
        return text;
    }

    @Override
    public String componentDescription() {
        if (reference == null) {
            reference = CardLibrary.getCard(cardID);
            if (reference == null) {
                return INVALID_REFERENCE;
            }
        }
        return String.format(DESCRIPTION_TEXT[0], FormatHelper.prefixWords(reference.name, "#y"), locationText());
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (reference == null) {
            return INVALID_REFERENCE;
        }

        int index = 0;
        String name = FormatHelper.prefixWords(reference.name, "*");
        if (locations.contains(Location.DRAW)) {
            index = 3;
        }
        if (dynvar == DynVar.FLAT) {
            return baseAmount == 1 ? String.format(CARD_TEXT[index + 1], name, locationText()) : String.format(CARD_TEXT[index + 2], baseAmount, pluralize ? name + S : name, locationText());
        }
        return String.format(CARD_TEXT[index], dynKey(), name, pluralize ? S : "", locationText());
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        if (reference == null) {
            return;
        }
        int amount = provider.getAmount(this);
        if (locations.contains(Location.HAND)) {
            addToBot(new MakeTempCardInHandAction(reference.makeStatEquivalentCopy(), amount));
        }
        if (locations.contains(Location.DRAW)) {
            addToBot(new MakeTempCardInDrawPileAction(reference.makeStatEquivalentCopy(), amount, true, true));
        }
        if (locations.contains(Location.DISCARD)) {
            addToBot(new MakeTempCardInDiscardAction(reference.makeStatEquivalentCopy(), amount));
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new MakeCardsComponent(baseAmount, cardID, pluralize, locations.toArray(new Location[0]));
    }
}
