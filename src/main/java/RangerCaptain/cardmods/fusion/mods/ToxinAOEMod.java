package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.powers.ToxinPower;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;

public class ToxinAOEMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(ToxinAOEMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public ToxinAOEMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card.hasTag(CustomTags.MAGIC_TOXIN_AOE)) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public String getModDescription() {
        return String.format(DESCRIPTION_TEXT[0], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!card.hasTag(CustomTags.MAGIC_TOXIN_AOE)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!card.hasTag(CustomTags.MAGIC_TOXIN_AOE)) {
            Wiz.forAllMonstersLiving(mon -> Wiz.applyToEnemy(mon, new ToxinPower(mon, val)));
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ToxinAOEMod(baseVal);
    }
}
