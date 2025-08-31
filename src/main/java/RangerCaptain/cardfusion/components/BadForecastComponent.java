package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.powers.BadForecastPower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.List;

public class BadForecastComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(BadForecastComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public BadForecastComponent(float base) {
        this(base, ComponentTarget.ENEMY);
    }

    public BadForecastComponent(float base, ComponentTarget target) {
        super(ID, base, ComponentType.APPLY, target, DynVar.MAGIC);
        setFlags(Flag.TARGETLESS_WHEN_CAPTURED);
    }

    @Override
    public void updatePrio() {
        priority = COMPLEX_APPLY_PRIO + target.ordinal();
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return other instanceof BadForecastComponent;
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return String.format(CARD_TEXT[target.ordinal()], dynKey());
    }

    @Override
    public String rawCapturedText() {
        return String.format(CARD_TEXT[ComponentTarget.values().length], dynKey());
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        switch (target) {
            case ENEMY:
                Wiz.applyToEnemy(m, new BadForecastPower(m, p, amount));
                break;
            case ENEMY_AOE:
                Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new BadForecastPower(mon, p, amount)));
                break;
            case NONE:
                addToBot(new DoAction(() -> {
                    AbstractMonster mon = AbstractDungeon.getRandomMonster();
                    if (mon != null) {
                        Wiz.applyToEnemy(mon, new BadForecastPower(mon, p, amount));
                    }
                }));
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new BadForecastComponent(baseAmount, target);
    }
}
