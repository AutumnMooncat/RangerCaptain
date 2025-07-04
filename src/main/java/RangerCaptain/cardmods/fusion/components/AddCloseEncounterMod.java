package RangerCaptain.cardmods.fusion.components;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

public class AddCloseEncounterMod extends AbstractFusionMod {
    public static final String ID = MainModfile.makeID(AddCloseEncounterMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;


    public AddCloseEncounterMod() {
        super(ID);
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return !ExtraEffectPatches.EffectFields.closeEncounter.get(card);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        ExtraEffectPatches.EffectFields.closeEncounter.set(card, true);
    }

    @Override
    public String getModDescription() {
        return DESCRIPTION_TEXT[0];
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        return FormatHelper.insertAfterText(rawDescription, CARD_TEXT[0]);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new AddCloseEncounterMod();
    }
}
