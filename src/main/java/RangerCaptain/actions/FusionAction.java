package RangerCaptain.actions;

import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.screens.FusionScreen;
import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FusionAction extends AbstractGameAction {
    private final Predicate<AbstractCard> predicate;
    private final ArrayList<AbstractCard> cardOrder = new ArrayList<>();
    private ArrayList<AbstractCard> hand;
    private ArrayList<AbstractCard> tempHand;

    public FusionAction() {
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.predicate = Wiz::canBeFused;
    }

    @Override
    public void update() {
        if (this.duration == this.startDuration) {
            hand = AbstractDungeon.player.hand.group;
            tempHand = new ArrayList<>();
            tempHand.addAll(this.hand);
            cardOrder.addAll(hand);
            if (!this.hand.isEmpty() && this.hand.stream().filter(this.predicate).count() >= 2) {
                this.tempHand.removeIf(this.predicate);
                if (!this.tempHand.isEmpty()) {
                    this.hand.removeIf(this.tempHand::contains);
                }
                BaseMod.openCustomScreen(FusionScreen.Enum.FUSION_SCREEN);
                this.tickDuration();
            } else {
                this.isDone = true;
            }
        } else if (!((FusionScreen) BaseMod.getCustomScreen(FusionScreen.Enum.FUSION_SCREEN)).wereCardsRetrieved) {
            AbstractCard baseCard = ((FusionScreen) BaseMod.getCustomScreen(FusionScreen.Enum.FUSION_SCREEN)).baseCard;
            AbstractCard donorCard = ((FusionScreen) BaseMod.getCustomScreen(FusionScreen.Enum.FUSION_SCREEN)).donorCard;
            FusedCard fusion = Wiz.fuse(baseCard, donorCard);
            fusion.superFlash();
            CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1");
            //Only add fusion back, baseCard and donorCard have been fused
            hand.add(fusion);
            ((FusionScreen) BaseMod.getCustomScreen(FusionScreen.Enum.FUSION_SCREEN)).wereCardsRetrieved = true;
            ((FusionScreen) BaseMod.getCustomScreen(FusionScreen.Enum.FUSION_SCREEN)).baseCard = null;
            ((FusionScreen) BaseMod.getCustomScreen(FusionScreen.Enum.FUSION_SCREEN)).donorCard = null;
            if (!this.tempHand.isEmpty()) {
                this.hand.addAll(this.tempHand);
            }

            ArrayList<AbstractCard> newCards = cardOrder.stream().filter(hand::contains).collect(Collectors.toCollection(ArrayList::new));
            hand.removeAll(newCards);
            hand.addAll(newCards);
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
            this.isDone = true;
        } else {
            this.tickDuration();
        }
    }
}
