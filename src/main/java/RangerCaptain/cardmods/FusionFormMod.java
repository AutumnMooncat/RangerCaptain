package RangerCaptain.cardmods;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.FusionForm;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class FusionFormMod extends AbstractCardModifier {
    public static final String ID = MainModfile.makeID(FusionFormMod.class.getSimpleName());
    public MonsterEnum monster1;
    public MonsterEnum monster2;
    public transient FusionForm form;

    public FusionFormMod(AbstractCard otherCard) {
        this.priority = -10;
        if (otherCard instanceof AbstractEasyCard) {
            this.monster2 = ((AbstractEasyCard) otherCard).getMonsterData();
        }
    }

    public FusionFormMod(MonsterEnum monsterEnum) {
        this.priority = -10;
        this.monster2 = monsterEnum;
    }

    public static void updateFusionForm(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, FusionFormMod.ID) && card instanceof AbstractEasyCard && ((AbstractEasyCard) card).getMonsterData() != null) {
            FusionFormMod mod = (FusionFormMod) CardModifierManager.getModifiers(card, FusionFormMod.ID).get(0);
            mod.monster1 = ((AbstractEasyCard) card).getMonsterData();
            mod.form = new FusionForm(mod.monster1, mod.monster2);
        }
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (card instanceof AbstractEasyCard) {
            this.monster1 = ((AbstractEasyCard) card).getMonsterData();
            this.form = new FusionForm(monster1, monster2);
            ((AbstractEasyCard) card).removeFusionTip();
        }
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return form.fusionName;
    }


    @Override
    public boolean shouldApply(AbstractCard card) {
        return Wiz.canBeFused(card);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FusionFormMod(monster2);
    }
}
