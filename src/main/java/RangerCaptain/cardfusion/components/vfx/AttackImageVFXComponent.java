package RangerCaptain.cardfusion.components.vfx;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.abstracts.AbstractVFXComponent;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class AttackImageVFXComponent extends AbstractVFXComponent {
    public static final String ID = MainModfile.makeID(AttackImageVFXComponent.class.getSimpleName());
    private final AbstractGameAction.AttackEffect effect;

    public AttackImageVFXComponent(AbstractGameAction.AttackEffect effect) {
        super(ID, ComponentTarget.NONE);
        this.effect = effect;
    }

    @Override
    public void playVFX(AbstractCreature source, AbstractCreature target) {
        if (target != null) {
            addToBot(new DoAction(() -> AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY, effect))));
        }
    }

    @Override
    public AbstractComponent makeCopy() {
        return new AttackImageVFXComponent(effect);
    }
}
