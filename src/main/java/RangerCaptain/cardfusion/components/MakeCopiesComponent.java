package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MakeCopiesComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(MakeCopiesComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final String HAND = DESCRIPTION_TEXT[1];
    public static final String DRAW = DESCRIPTION_TEXT[2];
    public static final String DISCARD = DESCRIPTION_TEXT[3];
    public static final String PILE = DESCRIPTION_TEXT[4];
    public static final String PILES = DESCRIPTION_TEXT[5];

    public enum Location {
        HAND,
        DRAW,
        DISCARD
    }

    public final ArrayList<Location> locations = new ArrayList<>();
    private transient FusedCard reference;

    public MakeCopiesComponent(float base) {
        this(base, Location.HAND);
    }

    public MakeCopiesComponent(float base, Location... locations) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, DynVar.MAGIC);
        this.locations.addAll(Arrays.stream(locations).distinct().collect(Collectors.toList()));
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
        this.reference = card;
    }

    @Override
    public void receiveStacks(AbstractComponent other) {
        if (other instanceof MakeCopiesComponent) {
            ArrayList<Location> combined = new ArrayList<>(locations);
            combined.addAll(((MakeCopiesComponent) other).locations);
            locations.clear();
            locations.addAll(combined.stream().distinct().collect(Collectors.toList()));
        }
        super.receiveStacks(other);
    }

    private String locationText() {
        String text = "";
        if (locations.isEmpty()) {
            return DESCRIPTION_TEXT[6];
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
        return String.format(DESCRIPTION_TEXT[0], locationText());
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        int index = 0;
        if (locations.contains(Location.DRAW)) {
            index = 3;
        }
        if (dynvar == DynVar.FLAT) {
            return workingAmount == 1 ? String.format(CARD_TEXT[index + 1], locationText()) : String.format(CARD_TEXT[index + 2], workingAmount, locationText());
        }
        return String.format(CARD_TEXT[index], dynKey(), locationText());
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
        return new MakeCopiesComponent(baseAmount, locations.toArray(new Location[0]));
    }
}
