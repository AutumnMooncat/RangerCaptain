package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.CleansePowerAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PenNibPower;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;

import java.util.List;

public class RemoveBuffForBlockComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(RemoveBuffForBlockComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public RemoveBuffForBlockComponent() {
        super(ID, 0, ComponentType.DO, ComponentTarget.SELF, DynVar.NONE);
        setFlags(Flag.REQUIRES_BLOCK);
    }

    @Override
    public void updatePrio() {
        priority = BLOCK_PRIO + target.ordinal() + 1;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof RemoveBuffForBlockComponent;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public String rawCapturedText() {
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public void postAssignment(FusedCard card, List<AbstractComponent> otherComponents) {
        baseAmount = otherComponents.stream().filter(c -> c.dynvar == DynVar.BLOCK).map(c -> c.baseAmount).findFirst().orElse(-1);
        dynvar = DynVar.BLOCK;
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        AbstractCard.CardType cardType = provider instanceof AbstractCard ? ((AbstractCard) provider).type : AbstractCard.CardType.SKILL;
        addToBot(new CleansePowerAction(p, 1, true, power -> power.type == AbstractPower.PowerType.BUFF && !(cardType == AbstractCard.CardType.ATTACK && (power instanceof VigorPower || power instanceof PenNibPower)), removed -> {
            if (!removed.isEmpty()) {
                addToTop(new GainBlockAction(p, p, amount));
            }
        }));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new RemoveBuffForBlockComponent();
    }
}
