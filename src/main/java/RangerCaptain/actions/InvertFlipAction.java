package RangerCaptain.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class InvertFlipAction extends AbstractGameAction {
    private final boolean flipH;
    private final boolean flipV;

    public InvertFlipAction(boolean flipH, boolean flipV) {
        this.flipH = flipH;
        this.flipV = flipV;
    }

    @Override
    public void update() {
        if (flipH) {
            AbstractDungeon.player.flipHorizontal = !AbstractDungeon.player.flipHorizontal;
        }
        if (flipV) {
            AbstractDungeon.player.flipVertical = !AbstractDungeon.player.flipVertical;
        }
        this.isDone = true;
    }
}
