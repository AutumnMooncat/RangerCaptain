package RangerCaptain.actions;

import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.powers.interfaces.OnFusionPower;
import RangerCaptain.relics.PearFusilli;
import RangerCaptain.relics.interfaces.OnFusionRelic;
import RangerCaptain.screens.FusionScreen;
import RangerCaptain.ui.FusionButton;
import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class FusionAction extends AbstractGameAction {
    private final Predicate<AbstractCard> predicate;
    private final ArrayList<AbstractCard> cardOrder = new ArrayList<>();
    private ArrayList<AbstractCard> hand;
    private ArrayList<AbstractCard> tempHand;
    private final boolean justPreview;

    public FusionAction(boolean justPreview) {
        this.duration = this.startDuration = Settings.ACTION_DUR_XFAST;
        this.predicate = Wiz::canBeFused;
        this.justPreview = justPreview;
    }

    @Override
    public void update() {
        if (duration == startDuration) {
            hand = AbstractDungeon.player.hand.group;
            tempHand = new ArrayList<>();
            tempHand.addAll(hand);
            cardOrder.addAll(hand);
            if (!hand.isEmpty() && hand.stream().filter(predicate).count() >= 2) {
                tempHand.removeIf(predicate);
                if (!tempHand.isEmpty()) {
                    hand.removeIf(tempHand::contains);
                }
                BaseMod.openCustomScreen(FusionScreen.Enum.FUSION_SCREEN, justPreview);
                tickDuration();
            } else {
                isDone = true;
            }
        } else if (!((FusionScreen) BaseMod.getCustomScreen(FusionScreen.Enum.FUSION_SCREEN)).wereCardsRetrieved) {
            FusionScreen screen = (FusionScreen) BaseMod.getCustomScreen(FusionScreen.Enum.FUSION_SCREEN);
            AbstractCard baseCard = screen.baseCard;
            AbstractCard donorCard = screen.donorCard;
            FusedCard fusion = null;
            if (baseCard != null && donorCard != null && !justPreview && screen.didFuse) {
                fusion = Wiz.fuse(baseCard, donorCard);
                screen.wereCardsRetrieved = true;
                screen.baseCard = null;
                screen.donorCard = null;
                screen.didFuse = false;

                boolean free = false;
                for (AbstractRelic relic : Wiz.adp().relics) {
                    if (relic instanceof PearFusilli && !relic.grayscale) {
                        relic.onTrigger();
                        free = true;
                        break;
                    }
                }

                if (!free) {
                    AbstractDungeon.player.loseEnergy(1);
                }

                FusionButton button = FusionButton.get();
                if (button != null) {
                    button.fusedThisTurn = true;
                }
            } else {
                if (baseCard != null) {
                    hand.add(baseCard);
                }
                if (donorCard != null) {
                    hand.add(donorCard);
                }
            }
            if (!this.tempHand.isEmpty()) {
                hand.addAll(this.tempHand);
            }

            ArrayList<AbstractCard> newCards = cardOrder.stream().filter(hand::contains).collect(Collectors.toCollection(ArrayList::new));
            hand.removeAll(newCards);
            hand.addAll(newCards);
            if (fusion != null) {
                //Only add fusion back, baseCard and donorCard have been fused
                hand.add(fusion);
            }
            AbstractDungeon.player.hand.refreshHandLayout();
            AbstractDungeon.player.hand.applyPowers();
            if (fusion != null) {
                fusionTriggers(baseCard, donorCard, fusion);
            }
            isDone = true;
        } else {
            tickDuration();
        }
    }

    public static void fusionTriggers(AbstractCard base, AbstractCard donor, FusedCard fusion) {
        fusion.superFlash();
        CardCrawlGame.sound.play("GHOST_ORB_IGNITE_1", 0.1f);

        for (AbstractRelic relic : Wiz.adp().relics) {
            if (relic instanceof OnFusionRelic) {
                ((OnFusionRelic) relic).onPerformFusion(base, donor, fusion);
            }
        }

        for (AbstractPower power : Wiz.adp().powers) {
            if (power instanceof OnFusionPower) {
                ((OnFusionPower) power).onPerformFusion(base, donor, fusion);
            }
        }
    }
}
