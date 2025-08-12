package RangerCaptain.cardfusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

import java.util.List;

public class NextTurnBlockComponent extends AbstractComponent {
    public static final String ID = MainModfile.makeID(NextTurnBlockComponent.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public NextTurnBlockComponent(float base) {
        this(base, ComponentTarget.SELF);
    }

    public NextTurnBlockComponent(float base, ComponentTarget target) {
        super(ID, base, ComponentType.BLOCK, target, DynVar.BLOCK);
        isSimple = true;
    }

    @Override
    public void updatePrio() {
        priority = BLOCK_PRIO + target.ordinal() + 1;
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
            case SELF:
                Wiz.applyToSelf(new NextTurnBlockPower(p, amount));
                break;
            case ENEMY:
                Wiz.applyToEnemy(m, new NextTurnBlockPower(m, amount));
                break;
            case ENEMY_RANDOM:
                addToBot(new DoAction(() -> {
                    AbstractMonster mon = AbstractDungeon.getRandomMonster();
                    Wiz.applyToEnemyTop(mon, new NextTurnBlockPower(mon, amount));
                }));
                break;
            case ENEMY_AOE:
                Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new NextTurnBlockPower(mon, amount)));
                break;
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new NextTurnBlockComponent(baseAmount, target);
    }
}
