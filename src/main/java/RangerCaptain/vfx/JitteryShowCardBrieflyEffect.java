package RangerCaptain.vfx;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

public class JitteryShowCardBrieflyEffect extends ShowCardBrieflyEffect {
    AbstractCard card;
    public JitteryShowCardBrieflyEffect(AbstractCard card) {
        super(card, Settings.WIDTH/2f, Settings.HEIGHT/2f);
        this.card = card;
        duration = startingDuration = 1.5f;
    }

    @Override
    public void update() {
        this.card.current_x += MathUtils.random(-10.0F, 10.0F) * Settings.scale;// 74
        this.card.current_y += MathUtils.random(-10.0F, 10.0F) * Settings.scale;
        super.update();
    }
}
