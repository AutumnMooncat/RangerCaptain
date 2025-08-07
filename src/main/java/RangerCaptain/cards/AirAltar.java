package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.EnterCardGroupPatches;
import RangerCaptain.powers.ResonancePower;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class AirAltar extends AbstractEasyCard implements EnterCardGroupPatches.OnEnterCardGroupCard {
    public final static String ID = makeID(AirAltar.class.getSimpleName());

    public AirAltar() {
        super(ID, -2, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 1;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {}

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        cantUseMessage = cardStrings.EXTENDED_DESCRIPTION[0];
        return false;
    }

    @Override
    public void upp() {
        uDesc();
    }

    @Override
    public void onEnter(CardGroup g) {
        if (g == Wiz.adp().hand) {
            superFlash();
            for (int i = AbstractDungeon.getMonsters().monsters.size() - 1; i >= 0; i--) {
                Wiz.applyToEnemyTop(AbstractDungeon.getMonsters().monsters.get(i), new ResonancePower(AbstractDungeon.getMonsters().monsters.get(i), Wiz.adp(), magicNumber));
            }
            if (!upgraded) {
                addToTop(new ExhaustSpecificCardAction(this, AbstractDungeon.player.hand));
            }
        }
    }
}