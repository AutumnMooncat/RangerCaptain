package RangerCaptain.cards;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;

import static RangerCaptain.MainModfile.makeID;

public class PortalToAnywhere extends AbstractEasyCard {
    public final static String ID = makeID(PortalToAnywhere.class.getSimpleName());

    public PortalToAnywhere() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        baseMagicNumber = magicNumber = 3;
        cardsToPreview = new WarpSickness();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }

    public void onTrigger() {
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
    }
}