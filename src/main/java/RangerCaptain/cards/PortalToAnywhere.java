package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.curses.AscendersBane;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static RangerCaptain.MainModfile.makeID;

public class PortalToAnywhere extends AbstractEasyCard {
    public final static String ID = makeID(PortalToAnywhere.class.getSimpleName());

    public PortalToAnywhere() {
        super(ID, 1, CardType.SKILL, CardRarity.RARE, CardTarget.NONE);
        baseMagicNumber = magicNumber = 3;
        cardsToPreview = new AscendersBane();
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
    }

    public void onTrigger() {
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new AscendersBane(), (float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F));
    }
}