package RangerCaptain.cardmods;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.FusionForm;
import RangerCaptain.util.MonsterEnum;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class FusionMod extends AbstractCardModifier {
    public static final String ID = MainModfile.makeID(DealDamageMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION;
    private final MonsterEnum monster1;
    private final MonsterEnum monster2;
    public final transient FusionForm form;

    public FusionMod(MonsterEnum monster1, MonsterEnum monster2) {
        this.monster1 = monster1;
        this.monster2 = monster2;
        this.form = new FusionForm(monster1, monster2);
        this.priority = -10;
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return form.fusionName;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return card instanceof AbstractEasyCard && ((AbstractEasyCard) card).getMonsterData() != null && !CardModifierManager.hasModifier(card, ID);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FusionMod(monster1, monster2);
    }
}
