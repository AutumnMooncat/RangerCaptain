package RangerCaptain.cardmods;

import RangerCaptain.MainModfile;
import RangerCaptain.powers.FervorPower;
import RangerCaptain.util.KeywordManager;
import RangerCaptain.util.TexLoader;
import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import java.util.ArrayList;
import java.util.List;

import static RangerCaptain.MainModfile.makeID;


public class CarrotMod extends AbstractCardModifier {
    public static String ID = makeID(CarrotMod.class.getSimpleName());
    public static Texture modIcon = TexLoader.getTexture(MainModfile.makeImagePath("icons/carrot.png"));
    private static List<TooltipInfo> tips;
    private static TooltipInfo carrotTip;
    private static TooltipInfo fervorTip;
    public int amount;

    public CarrotMod(int amount) {
        this.amount = amount;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        Wiz.applyToSelf(new FervorPower(Wiz.adp(), amount));
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        if (tips == null) {
            tips = new ArrayList<>();
            carrotTip = new TooltipInfo(BaseMod.getKeywordTitle(KeywordManager.CARROT), BaseMod.getKeywordDescription(KeywordManager.CARROT));
            fervorTip = new TooltipInfo(BaseMod.getKeywordTitle(KeywordManager.FERVOR), BaseMod.getKeywordDescription(KeywordManager.FERVOR));
        }
        tips.clear();
        if (!card.keywords.contains(KeywordManager.CARROT)) {
            tips.add(carrotTip);
        }
        if (!card.keywords.contains(KeywordManager.FERVOR)) {
            tips.add(fervorTip);
        }
        return tips;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(card, ID);
        if (!mods.isEmpty()) {
            CarrotMod mod = (CarrotMod) mods.get(0);
            mod.amount += amount;
            return false;
        }
        return true;
    }


    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        if (SingleCardViewPopup.isViewingUpgrade && !card.upgraded) {
            return;
        }
        ExtraIcons.icon(modIcon).text(String.valueOf(amount)).textOffsetX(3).drawColor(new Color(1, 1, 1, card.transparency)).render(card);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        ExtraIcons.icon(modIcon).text(String.valueOf(amount)).textOffsetX(6).drawColor(new Color(1, 1, 1, card.transparency)).render(card);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CarrotMod(amount);
    }
}
