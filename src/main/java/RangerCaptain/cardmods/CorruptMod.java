package RangerCaptain.cardmods;

import RangerCaptain.actions.CorruptAction;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.TextureScaler;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

import static RangerCaptain.MainModfile.makeID;


public class CorruptMod extends AbstractCardModifier {
    public static final String ID = makeID(CorruptMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;
    public static final Texture modIcon = TextureScaler.rescale(AbstractPower.atlas.findRegion("128/corruption"), 64, 64);
    private static final Color renderColor = new Color(1, 0, 1, 1);
    public int amount;

    public CorruptMod(int amount) {
        this.amount = amount;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(card, ID);
        if (!mods.isEmpty()) {
            CorruptMod mod = (CorruptMod) mods.get(0);
            mod.amount += amount;
            return false;
        }
        return true;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        for (int i = 0 ; i < amount ; i++) {
            Wiz.atb(new CorruptAction());
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (amount > 1) {
            return FormatHelper.insertAfterText(rawDescription, String.format(TEXT[1], amount));
        }
        return FormatHelper.insertAfterText(rawDescription, TEXT[0]);
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
        renderColor.a = card.transparency;
        ExtraIcons.icon(modIcon).text(String.valueOf(amount)).textOffsetX(3).drawColor(renderColor).render(card);
    }

    @Override
    public void onSingleCardViewRender(AbstractCard card, SpriteBatch sb) {
        renderColor.a = card.transparency;
        ExtraIcons.icon(modIcon).text(String.valueOf(amount)).textOffsetX(6).drawColor(renderColor).render(card);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new CorruptMod(amount);
    }
}
