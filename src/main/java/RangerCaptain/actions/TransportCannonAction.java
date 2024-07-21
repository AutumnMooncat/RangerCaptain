package RangerCaptain.actions;

import RangerCaptain.MainModfile;
import RangerCaptain.util.TextureSniper;
import RangerCaptain.util.Wiz;
import RangerCaptain.vfx.VFXContainer;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DiscardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class TransportCannonAction extends AbstractGameAction {
    private final DamageInfo info;

    public TransportCannonAction(AbstractMonster target, DamageInfo info) {
        this.info = info;
        this.target = target;
        this.source = AbstractDungeon.player;
    }

    @Override
    public void update() {
        CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1");
        int size = Wiz.adp().hand.size();
        //Wiz.applyToEnemyTop((AbstractMonster) target, new StaggerPower(target, size));
        for (int i = 0; i < size; i++) {
            addToTop(new DamageAction(target, info, AttackEffect.SLASH_DIAGONAL));
        }
        //Wiz.applyToSelfTop(new DrawCardNextTurnPower(Wiz.adp(), size));
        for (AbstractCard c : Wiz.adp().hand.group) {
            addToTop(new VFXAction(VFXContainer.throwEffect(TextureSniper.snipeCard(c), 0.25f, target.hb, MainModfile.HEADBAND_PURPLE_COLOR, true, false)));
        }
        addToTop(new DiscardAction(AbstractDungeon.player, AbstractDungeon.player, size, false));
        this.isDone = true;
    }
}
