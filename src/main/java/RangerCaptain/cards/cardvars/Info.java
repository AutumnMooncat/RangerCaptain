package RangerCaptain.cards.cardvars;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import basemod.abstracts.DynamicVariable;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

public class Info extends DynamicVariable {

    @Override
    public String key() {
        return MainModfile.makeID("I");
    }

    @Override
    public boolean isModified(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            return ((AbstractEasyCard) card).isInfoModified;
        }
        return false;
    }

    @Override
    public int value(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            return ((AbstractEasyCard) card).info;
        }
        return -1;
    }

    public void setIsModified(AbstractCard card, boolean v) {
        if (card instanceof AbstractEasyCard) {
            ((AbstractEasyCard) card).isInfoModified = v;
        }
    }

    @Override
    public int baseValue(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            return ((AbstractEasyCard) card).baseInfo;
        }
        return -1;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            return ((AbstractEasyCard) card).upgradedInfo;
        }
        return false;
    }

    @Override
    public Color getNormalColor() {
        return Settings.CREAM_COLOR;
    }

    @Override
    public Color getUpgradedColor() {
        return Settings.CREAM_COLOR;
    }

    @Override
    public Color getIncreasedValueColor() {
        return Settings.CREAM_COLOR;
    }

    @Override
    public Color getDecreasedValueColor() {
        return Settings.CREAM_COLOR;
    }
}