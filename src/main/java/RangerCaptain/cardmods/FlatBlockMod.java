package RangerCaptain.cardmods;

import RangerCaptain.util.TextureScaler;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

import static RangerCaptain.MainModfile.makeID;


public class FlatBlockMod extends AbstractCardModifier {
    public static String ID = makeID(FlatBlockMod.class.getSimpleName());
    public static Texture modIcon = TextureScaler.rescale(AbstractPower.atlas.findRegion("128/channel"), 64, 64);
    public int amount;

    public FlatBlockMod(int amount) {
        this.amount = amount;
    }

    @Override
    public float modifyBaseBlock(float block, AbstractCard card) {
        if (block >= 0) {
            block += amount;
        }
        return block;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(card, ID);
        if (!mods.isEmpty()) {
            FlatBlockMod mod = (FlatBlockMod) mods.get(0);
            mod.amount += amount;
            return false;
        }
        return true;
    }


    @Override
    public void onRender(AbstractCard card, SpriteBatch sb) {
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
        return new FlatBlockMod(amount);
    }
}
