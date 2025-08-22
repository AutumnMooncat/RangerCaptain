package RangerCaptain.cards;

import RangerCaptain.actions.EasyXCostAction;
import RangerCaptain.actions.GatherAction;
import RangerCaptain.actions.StashCardsAction;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Arrays;

import static RangerCaptain.MainModfile.makeID;

public class SupplyCache extends AbstractEasyCard {
    public final static String ID = makeID(SupplyCache.class.getSimpleName());

    public SupplyCache() {
        super(ID, -1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 6;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new EasyXCostAction(this, (base, params) -> {
            int effect = base + Arrays.stream(params).sum();
            if (effect > 0) {
                CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                group.group.addAll(GatherAction.generateCardChoices(effect));
                addToTop(new StashCardsAction(group, 1, true, true));
                for (int i = 0; i < effect; i++) {
                    blckTop();
                }
            }
            return true;
        }));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
    }
}