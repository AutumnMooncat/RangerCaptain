package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.AddMindMeldComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Mascotoy extends AbstractEasyCard {
    public final static String ID = makeID(Mascotoy.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MASCOTOY)
                .withCost(1)
                .withDamage(8, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new AddMindMeldComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.MASCOTORN)
                .withCost(1)
                .withDamage(10, AbstractGameAction.AttackEffect.SLASH_DIAGONAL)
                .with(new AddMindMeldComponent())
                .register();
    }

    public Mascotoy() {
        super(ID, 1, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = damage = 4;
        setMonsterData(MonsterEnum.MASCOTOY);
        tags.add(CustomTags.MIND_MELD);
        ExtraEffectPatches.EffectFields.mindMeld.set(this, true);
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