package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.EnterCardGroupPatches;
import com.evacipated.cardcrawl.mod.stslib.actions.common.MultiGroupSelectAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import static RangerCaptain.MainModfile.makeID;

public class PortalToAnywhere extends AbstractEasyCard {
    public final static String ID = makeID(PortalToAnywhere.class.getSimpleName());
    public boolean success;
    public Runnable onSuccess;

    public PortalToAnywhere() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        exhaust = true;
        /*baseMagicNumber = magicNumber = 3;
        cardsToPreview = new WarpSickness();*/
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int handIndex = p.hand.group.indexOf(this);
        addToBot(new MultiGroupSelectAction(upgraded ? cardStrings.EXTENDED_DESCRIPTION[1] : cardStrings.EXTENDED_DESCRIPTION[0], (cards, groups) -> {
            for (AbstractCard c : cards) {
                CardGroup group = groups.get(c);
                int index = group.group.indexOf(c);

                //Prepare the runnable for the UCA Patch
                onSuccess = () -> {
                    //Actually add the card to the pile
                    group.group.add(index, this);
                    EnterCardGroupPatches.CardAddedToGroup.checkCard(group, this);

                    //Animate the card cosmetically
                    unhover();
                    untip();
                    stopGlowing();
                    if (group == p.drawPile) {
                        shrink();
                        darken(false);
                        AbstractDungeon.getCurrRoom().souls.onToDeck(this, true, true);
                    } else if (group == p.discardPile) {
                        shrink();
                        darken(false);
                        AbstractDungeon.getCurrRoom().souls.discard(this, true);
                    } else if (group == p.exhaustPile) {
                        AbstractDungeon.effectList.add(new ExhaustCardEffect(this));
                    }
                };

                //Unfade the original card in case it came from the Exhaust pile
                c.unfadeOut();

                //Move original card to hand
                c.unhover();
                c.untip();
                c.stopGlowing();
                group.group.remove(c);
                c.lighten(true);
                c.setAngle(0.0F);
                c.drawScale = 0.12F;
                c.targetDrawScale = 0.75F;
                if (handIndex != -1) {
                    p.hand.group.add(handIndex, c);
                    EnterCardGroupPatches.CardAddedToGroup.checkCard(p.hand, c);
                } else {
                    p.hand.addToTop(c);
                }

                //Refresh hand logic given we manually added to hand
                AbstractDungeon.player.hand.refreshHandLayout();
                AbstractDungeon.player.hand.applyPowers();

                //Tell UCA to run the runnable if we are upgraded, else let it go through normal UCA logic
                //This means spoon can make the non-upgraded version discard instead of moving like the upgraded version
                success = upgraded;
            }
        }, 1, CardGroup.CardGroupType.DRAW_PILE, CardGroup.CardGroupType.DISCARD_PILE, CardGroup.CardGroupType.EXHAUST_PILE));
    }

    @Override
    public void upp() {
        exhaust = false;
        uDesc();
    }

    /*public void onTrigger() {
        AbstractCard sickness = new WarpSickness();

        //Since Omamori and other modded Curse blockers are in the Constructor, we can check if the action gets set to done to know if the curse was negated
        ShowCardAndObtainEffect testEffect = new ShowCardAndObtainEffect(sickness, Settings.WIDTH/2f, Settings.HEIGHT/2f);
        if (testEffect.isDone) {
            return;
        }

        //Position the card in the center of the screen for the ShowCardBrieflyEffect
        sickness.current_x = sickness.target_x = Settings.WIDTH/2f;
        sickness.current_y = sickness.target_y = Settings.HEIGHT/2f;
        sickness.shrink();

        //We have to manually account for hooks as we are bypassing the effect that normally does this
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onObtainCard(sickness);
        }

        //Manually obtain the card, because the game effect is too slow and won't be finished by the time the next combat starts, starting us without the card
        AbstractDungeon.getCurrRoom().souls.obtain(sickness, true);
        //Display the card on screen
        MainModfile.safeEffectQueue.add(new ShowCardBrieflyEffect(sickness.makeStatEquivalentCopy(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));

        //Another manual hook call
        for (AbstractRelic r : AbstractDungeon.player.relics) {
            r.onMasterDeckChange();
        }
    }*/
}