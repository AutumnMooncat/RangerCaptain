package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

public class EnergyIfDebuffComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(EnergyIfDebuffComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    private final Color GOLD_COLOR = Color.GOLD.cpy();

    public EnergyIfDebuffComponent(float base) {
        super(ID, base, ComponentType.DO, ComponentTarget.ENEMY, DynVar.MAGIC);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        if (dynvar == DynVar.FLAT) {
            if (workingAmount >= 1 && workingAmount <= 3) {
                return CARD_TEXT[workingAmount];
            }
            return String.format(CARD_TEXT[4], workingAmount);
        }
        return String.format(CARD_TEXT[0], dynKey());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        addToBot(new DoAction(() -> {
            if (target != null && m.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)) {
                addToTop(new GainEnergyAction(provider.getAmount(this)));
            }
        }));
    }

    @Override
    public void glowCheck(FusedCard card) {
        for (AbstractMonster mon : AbstractDungeon.getMonsters().monsters) {
            if (mon.powers.stream().anyMatch(pow -> pow.type == AbstractPower.PowerType.DEBUFF)) {
                card.glowColor = GOLD_COLOR;
            }
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new EnergyIfDebuffComponent(baseAmount);
    }
}
