package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MakeCardsFreeComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(MakeCardsFreeComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final String HAND = DESCRIPTION_TEXT[1];
    public static final String DRAW = DESCRIPTION_TEXT[2];
    public static final String DISCARD = DESCRIPTION_TEXT[3];
    public static final String PILE = DESCRIPTION_TEXT[4];
    public static final String PILES = DESCRIPTION_TEXT[5];
    public static final String INVALID_LOCATION = DESCRIPTION_TEXT[6];

    public enum Location {
        HAND,
        DRAW,
        DISCARD
    }

    public final ArrayList<Location> locations = new ArrayList<>();

    public MakeCardsFreeComponent(float base) {
        this(base, Location.HAND);
    }

    public MakeCardsFreeComponent(float base, Location... locations) {
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
    public void receiveStacks(AbstractComponent other) {
        if (other instanceof MakeCardsFreeComponent) {
            ArrayList<Location> combined = new ArrayList<>(locations);
            combined.addAll(((MakeCardsFreeComponent) other).locations);
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
        return String.format(DESCRIPTION_TEXT[0], locationText());
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (dynvar == DynVar.FLAT) {
            return workingAmount == 1 ? String.format(CARD_TEXT[1], locationText()) : String.format(CARD_TEXT[2], workingAmount, locationText());
        }
        return String.format(CARD_TEXT[0], dynKey(), locationText());
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        if (locations.contains(Location.HAND)) {
            addToBot(new DoAction(() -> {
                ArrayList<AbstractCard> better = Wiz.adp().hand.group.stream().filter(c -> !c.freeToPlayOnce && c.costForTurn != 0).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<AbstractCard> valid = Wiz.adp().hand.group.stream().filter(c -> !c.freeToPlayOnce).collect(Collectors.toCollection(ArrayList::new));
                int overflow = amount - better.size();
                if (overflow <= 0) {
                    for (int i = 0; i < amount; i++) {
                        AbstractCard card = better.remove(AbstractDungeon.cardRandomRng.random(better.size() - 1));
                        card.freeToPlayOnce = true;
                    }
                } else {
                    for (AbstractCard card : better) {
                        valid.remove(card);
                        card.freeToPlayOnce = true;
                    }
                    while (overflow > 0 && !valid.isEmpty()) {
                        AbstractCard card = valid.remove(AbstractDungeon.cardRandomRng.random(valid.size() - 1));
                        card.freeToPlayOnce = true;
                        overflow--;
                    }
                }
            }));
        }
        if (locations.contains(Location.DRAW)) {
            addToBot(new DoAction(() -> {
                ArrayList<AbstractCard> better = Wiz.adp().drawPile.group.stream().filter(c -> !c.freeToPlayOnce && c.costForTurn != 0).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<AbstractCard> valid = Wiz.adp().drawPile.group.stream().filter(c -> !c.freeToPlayOnce).collect(Collectors.toCollection(ArrayList::new));
                int overflow = amount - better.size();
                if (overflow <= 0) {
                    for (int i = 0; i < amount; i++) {
                        AbstractCard card = better.remove(AbstractDungeon.cardRandomRng.random(better.size() - 1));
                        card.freeToPlayOnce = true;
                    }
                } else {
                    for (AbstractCard card : better) {
                        valid.remove(card);
                        card.freeToPlayOnce = true;
                    }
                    while (overflow > 0 && !valid.isEmpty()) {
                        AbstractCard card = valid.remove(AbstractDungeon.cardRandomRng.random(valid.size() - 1));
                        card.freeToPlayOnce = true;
                        overflow--;
                    }
                }
            }));
        }
        if (locations.contains(Location.DISCARD)) {
            addToBot(new DoAction(() -> {
                ArrayList<AbstractCard> better = Wiz.adp().discardPile.group.stream().filter(c -> !c.freeToPlayOnce && c.costForTurn != 0).collect(Collectors.toCollection(ArrayList::new));
                ArrayList<AbstractCard> valid = Wiz.adp().discardPile.group.stream().filter(c -> !c.freeToPlayOnce).collect(Collectors.toCollection(ArrayList::new));
                int overflow = amount - better.size();
                if (overflow <= 0) {
                    for (int i = 0; i < amount; i++) {
                        AbstractCard card = better.remove(AbstractDungeon.cardRandomRng.random(better.size() - 1));
                        card.freeToPlayOnce = true;
                    }
                } else {
                    for (AbstractCard card : better) {
                        valid.remove(card);
                        card.freeToPlayOnce = true;
                    }
                    while (overflow > 0 && !valid.isEmpty()) {
                        AbstractCard card = valid.remove(AbstractDungeon.cardRandomRng.random(valid.size() - 1));
                        card.freeToPlayOnce = true;
                        overflow--;
                    }
                }
            }));
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new MakeCardsFreeComponent(baseAmount, locations.toArray(new Location[0]));
    }
}
