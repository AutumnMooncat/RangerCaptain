package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.powers.BurnedPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.List;

public class BurnPointsComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(BurnPointsComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public BurnPointsComponent() {
        super(ID, 0, ComponentType.DO, ComponentTarget.ENEMY_AOE, DynVar.NONE);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof BurnPointsComponent;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[0];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return CARD_TEXT[0];
    }

    @Override
    public String rawCapturedText() {
        return CARD_TEXT[0];
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        addToBot(new DoAction(() -> {
            for (int i = AbstractDungeon.getMonsters().monsters.size() - 1; i >= 0; i--) {
                AbstractMonster mon = AbstractDungeon.getMonsters().monsters.get(i);
                if (!mon.isDeadOrEscaped()) {
                    AbstractPower burn = mon.getPower(BurnedPower.POWER_ID);
                    if (burn != null) {
                        addToTop(new LoseHPAction(mon, p, burn.amount, AbstractGameAction.AttackEffect.FIRE));
                    }
                }
            }
        }));
    }

    @Override
    public AbstractComponent makeCopy() {
        return new BurnPointsComponent();
    }
}
