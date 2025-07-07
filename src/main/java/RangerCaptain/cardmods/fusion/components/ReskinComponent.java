package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.abstracts.AbstractTraitComponent;
import RangerCaptain.cards.tokens.FusedCard;
import com.badlogic.gdx.graphics.Color;

import java.util.List;

public class ReskinComponent extends AbstractTraitComponent {
    public static final String ID = MainModfile.makeID(ReskinComponent.class.getSimpleName());
    public Color anchor1;
    public Color anchor2;
    public Color target1;
    public Color target2;
    public boolean flipX;

    public ReskinComponent(Color anchor1, Color anchor2, Color target1, Color target2, boolean flipX) {
        super(ID);
        this.anchor1 = anchor1;
        this.anchor2 = anchor2;
        this.target1 = target1;
        this.target2 = target2;
        this.flipX = flipX;
    }

    @Override
    public void updatePrio() {
        priority = COST_PRIO;
    }

    @Override
    public boolean shouldStack(AbstractComponent other) {
        return false;
    }

    @Override
    public void applyTraits(FusedCard card, List<AbstractComponent> captured) {
        card.anchor1 = anchor1;
        card.anchor2 = anchor2;
        card.target1 = target1;
        card.target2 = target2;
        card.flipX = flipX;
    }

    @Override
    public String componentDescription() {
        return null;
    }

    @Override
    public String rawCardText(List<AbstractComponent> captured) {
        return null;
    }

    @Override
    public AbstractComponent makeCopy() {
        return new ReskinComponent(anchor1, anchor2, target1, target2, flipX);
    }
}
