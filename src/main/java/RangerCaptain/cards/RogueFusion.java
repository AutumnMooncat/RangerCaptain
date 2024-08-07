package RangerCaptain.cards;

import RangerCaptain.cardmods.FusionMod;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class RogueFusion extends AbstractEasyCard {
    public final static String ID = makeID(RogueFusion.class.getSimpleName());

    public RogueFusion() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE);
        setMonsterData(MonsterEnum.ADEPTILE);
        CardModifierManager.addModifier(this, new FusionMod(MonsterEnum.ADEPTILE, MonsterEnum.SIRENADE));
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {

    }

    @Override
    public void upp() {

    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, BLACK, WHITE, BLACK, WHITE, false);
    }
}