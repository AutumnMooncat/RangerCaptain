package RangerCaptain.cardmods;

import RangerCaptain.util.TextureScaler;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.mod.stslib.util.extraicons.ExtraIcons;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.ArrayList;

import static RangerCaptain.MainModfile.makeID;


public class FlatDamageMod extends AbstractCardModifier {
    public static String ID = makeID(FlatDamageMod.class.getSimpleName());
    public static Texture modIcon = TextureScaler.rescale(AbstractPower.atlas.findRegion("128/rupture"), 64, 64);
    public int amount;

    public FlatDamageMod(int amount) {
        this.amount = amount;
    }

    @Override
    public float modifyBaseDamage(float damage, DamageInfo.DamageType type, AbstractCard card, AbstractMonster target) {
        if (damage >= 0) {
            damage += amount;
        }
        return damage;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        ArrayList<AbstractCardModifier> mods = CardModifierManager.getModifiers(card, ID);
        if (!mods.isEmpty()) {
            FlatDamageMod mod = (FlatDamageMod) mods.get(0);
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
        return new FlatDamageMod(amount);
    }
}
