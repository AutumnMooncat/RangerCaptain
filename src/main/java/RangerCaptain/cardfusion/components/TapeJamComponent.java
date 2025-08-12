package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.powers.TapeJamPower;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.Collections;
import java.util.List;

public class TapeJamComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(TapeJamComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public TapeJamComponent(float base) {
        this(base, ComponentTarget.ENEMY);
    }

    public TapeJamComponent(float base, ComponentTarget target) {
        super(ID, base, ComponentType.DO, target, DynVar.MAGIC);
        setFlags(Flag.CANT_BE_CAPTURED);
    }

    @Override
    public void updatePrio() {
        priority = DO_PRIO + target.ordinal();
    }

    @Override
    public String componentDescription() {
        return DESCRIPTION_TEXT[target.ordinal()];
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        int index = target.ordinal();
        if (dynvar == DynVar.FLAT) {
            index += ComponentTarget.values().length;
            if (workingAmount > 1) {
                index += ComponentTarget.values().length;
            }
        }
        return String.format(CARD_TEXT[index], dynKey());
    }

    @Override
    public String rawCapturedText() {
        return FormatHelper.uncapitalize(rawCardText(Collections.emptyList()));
    }

    @Override
    public void onTrigger(ComponentAmountProvider provider, AbstractPlayer p, AbstractMonster m, List<AbstractComponent> captured) {
        int amount = provider.getAmount(this);
        switch (target) {
             case ENEMY:
                Wiz.applyToEnemy(m, new TapeJamPower(m, amount));
                break;
            case ENEMY_RANDOM:
                addToBot(new DoAction(() -> {
                    AbstractMonster mon = AbstractDungeon.getRandomMonster();
                    if (mon != null) {
                        Wiz.applyToEnemy(mon, new TapeJamPower(mon, amount));
                    }
                }));
                break;
            case ENEMY_AOE:
                Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new TapeJamPower(mon, amount)));
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new TapeJamComponent(baseAmount, target);
    }
}
