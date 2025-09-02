package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.StashTempCardAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.util.FormatHelper;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class StashTempCardComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(StashTempCardComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final String INVALID_REFERENCE = DESCRIPTION_TEXT[1];
    public static final String S = DESCRIPTION_TEXT[2];

    public final String cardID;
    public final boolean pluralize;
    public transient AbstractCard reference;

    public StashTempCardComponent(float base, AbstractCard card, boolean pluralize) {
        super(ID, base, ComponentType.DO, ComponentTarget.NONE, DynVar.MAGIC);
        this.cardID = card.cardID;
        this.reference = card;
        this.pluralize = pluralize;
    }

    public StashTempCardComponent(float base, String cardID, boolean pluralize) {
        this(base, CardLibrary.getCard(cardID).makeCopy(), pluralize);
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
        if (other instanceof StashTempCardComponent) {
            if (!cardID.equals(((StashTempCardComponent) other).cardID)) {
                return false;
            }
        }
        return super.shouldStack(other);
    }

    @Override
    public String componentDescription() {
        if (reference == null) {
            reference = CardLibrary.getCard(cardID);
            if (reference == null) {
                return INVALID_REFERENCE;
            }
        }
        return String.format(DESCRIPTION_TEXT[0], FormatHelper.prefixWords(reference.name, "#y"));
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (reference == null) {
            return INVALID_REFERENCE;
        }

        int index = 0;
        String name = FormatHelper.prefixWords(reference.name, "*");
        if (dynvar == DynVar.FLAT) {
            return workingAmount == 1 ? String.format(CARD_TEXT[index + 1], name) : String.format(CARD_TEXT[index + 2], workingAmount, pluralize ? name + S : name);
        }
        return String.format(CARD_TEXT[index], dynKey(), name, pluralize ? S : "");
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        if (reference == null) {
            return;
        }
        int amount = provider.getAmount(this);
        addToBot(new StashTempCardAction(reference.makeStatEquivalentCopy(), amount));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new StashTempCardComponent(baseAmount, cardID, pluralize);
    }
}
