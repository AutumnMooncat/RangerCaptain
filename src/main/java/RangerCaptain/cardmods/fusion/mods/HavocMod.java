package RangerCaptain.cardmods.fusion.mods;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractExtraEffectFusionMod;
import RangerCaptain.cards.Muskrateer;
import RangerCaptain.util.FormatHelper;
import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class HavocMod extends AbstractExtraEffectFusionMod {
    public static final String ID = MainModfile.makeID(HavocMod.class.getSimpleName());
    public static final String[] DESCRIPTION_TEXT = CardCrawlGame.languagePack.getUIString(ID).TEXT;
    public static final String[] CARD_TEXT = CardCrawlGame.languagePack.getUIString(ID).EXTRA_TEXT;

    public HavocMod(int base) {
        super(ID, VarType.MAGIC, base);
    }

    @Override
    public float modifyBaseMagic(float magic, AbstractCard card) {
        if (card instanceof Muskrateer) {
            magic += getModifiedBase(card);
        }
        return magic;
    }

    @Override
    public String getModDescription() {
        return baseVal == 1 ? DESCRIPTION_TEXT[0] : String.format(DESCRIPTION_TEXT[1], baseVal);
    }

    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (!(card instanceof Muskrateer)) {
            rawDescription = FormatHelper.insertAfterText(rawDescription, String.format(CARD_TEXT[0], descriptionKey()));
        }
        return rawDescription;
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        if (!(card instanceof Muskrateer)) {
            for (int i = 0 ; i < val ; i++) {
                addToBot(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), true));
            }
        }
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new HavocMod(baseVal);
    }
}
