package RangerCaptain.cardmods;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.tokens.FusedCard;
import RangerCaptain.util.FusionForm;
import RangerCaptain.util.MonsterEnum;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;

public class FusionFormMod extends AbstractCardModifier {
    public static final String ID = MainModfile.makeID(FusionFormMod.class.getSimpleName());
    public MonsterEnum monster1;
    public MonsterEnum monster2;
    public transient FusionForm form;

    public FusionFormMod(MonsterEnum monster1, MonsterEnum monster2) {
        this.priority = -10;
        this.monster1 = monster1;
        this.monster2 = monster2;
        this.form = new FusionForm(monster1, monster2);
    }

    public static void changeFusionForm(AbstractCard card, MonsterEnum monster1, MonsterEnum monster2) {
        if (CardModifierManager.hasModifier(card, FusionFormMod.ID)) {
            FusionFormMod mod = (FusionFormMod) CardModifierManager.getModifiers(card, FusionFormMod.ID).get(0);
            mod.monster1 = monster1;
            mod.monster2 = monster2;
            mod.form = new FusionForm(mod.monster1, mod.monster2);
        }
    }

    public static void updateFusionForm(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, FusionFormMod.ID)) {
            FusionFormMod mod = (FusionFormMod) CardModifierManager.getModifiers(card, FusionFormMod.ID).get(0);
            mod.form = new FusionForm(mod.monster1, mod.monster2);
        }
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return form.fusionName;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card instanceof FusedCard;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FusionFormMod(monster1, monster2);
    }
}
