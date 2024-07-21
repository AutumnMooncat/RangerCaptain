package RangerCaptain.vfx;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class ColoredAngledFlashAtkImgEffect extends FlashAtkImgEffect {
    public ColoredAngledFlashAtkImgEffect(float x, float y, float r, AbstractGameAction.AttackEffect effect, boolean mute) {
        super(x, y, effect, mute);
        rotation = r;
    }

    public ColoredAngledFlashAtkImgEffect(float x, float y, float r, AbstractGameAction.AttackEffect effect) {
        this(x, y, r, effect, false);
    }

    public ColoredAngledFlashAtkImgEffect(float x, float y, float r, AbstractGameAction.AttackEffect effect, Color c, boolean mute) {
        super(x, y, effect, mute);
        rotation = r;
        color.set(c);
    }

    public ColoredAngledFlashAtkImgEffect(float x, float y, float r, AbstractGameAction.AttackEffect effect, Color c) {
        this(x, y, r, effect, c, false);
    }
}
