package RangerCaptain.cards.abstracts;

import RangerCaptain.actions.InstantExhaustSpecificCardAction;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SanctityEffect;

public abstract class AbstractEmpowerCard extends AbstractEasyCard {
    public AbstractEmpowerCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target) {
        super(cardID, cost, type, rarity, target);
    }

    public AbstractEmpowerCard(String cardID, int cost, CardType type, CardRarity rarity, CardTarget target, CardColor color) {
        super(cardID, cost, type, rarity, target, color);
    }

    public void triggerOnGlowCheck() {
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (Wiz.adp().hand.group.stream().anyMatch(c -> c instanceof Miracle)) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    @Override
    public void onRightClick() {
        //TODO Empower based on Light Charges?
        for (AbstractCard c : Wiz.adp().hand.group) {
            if (c instanceof Miracle) {
                addToTop(new InstantExhaustSpecificCardAction(c, Wiz.adp().hand));
                addToTop(new AbstractGameAction() {
                    @Override
                    public void update() {
                        AbstractDungeon.effectList.add(new SanctityEffect(Wiz.adp().hb.cX, Wiz.adp().hb.cY));
                        AbstractDungeon.effectList.add(new BorderFlashEffect(Color.GOLD, true));
                        CardCrawlGame.sound.play("HEAL_1");
                        this.isDone = true;
                    }
                });
                superFlash();
                onEmpower();
                return;
            }
        }
    }

    public abstract void onEmpower();
}
