package RangerCaptain.cards;

import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.MindMeldPatches;
import RangerCaptain.powers.MindMeldPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Mascotoy extends AbstractEasyCard {
    public final static String ID = makeID(Mascotoy.class.getSimpleName());

    public Mascotoy() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 4;
        setMonsterData(MonsterEnum.MASCOTOY);
        MindMeldPatches.MindMeldField.mindMeldCount.set(this, MindMeldPatches.MindMeldField.mindMeldCount.get(this) + 1);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
    }

    @Override
    public void upp() {
        upgradeDamage(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MASCOTORN);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, Color.PINK, WHITE, Color.PINK, WHITE, false);
    }
}