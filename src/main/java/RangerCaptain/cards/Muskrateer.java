package RangerCaptain.cards;

import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.HavocComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.PlayTopCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Muskrateer extends AbstractEasyCard {
    public final static String ID = makeID(Muskrateer.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MUSKRATEER)
                .withCost(2)
                .withDamage(4, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .with(new HavocComponent(1))
                .register();
        new FusionComponentHelper(MonsterEnum.MUSKRATEER)
                .withCost(2)
                .withDamage(6, AbstractGameAction.AttackEffect.SLASH_HORIZONTAL)
                .with(new HavocComponent(1))
                .register();
    }

    public Muskrateer() {
        super(ID, 2, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 11;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.MUSKRATEER);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, upgraded ? AbstractGameAction.AttackEffect.BLUNT_HEAVY : AbstractGameAction.AttackEffect.SLASH_HORIZONTAL);
        for (int i = 0 ; i  < magicNumber ; i++) {
            addToBot(new PlayTopCardAction(AbstractDungeon.getCurrRoom().monsters.getRandomMonster(null, true, AbstractDungeon.cardRandomRng), true));
        }
    }

    @Override
    public void upp() {
        upgradeDamage(4);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.RATCOUSEL);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, AZURE, WHITE, AZURE, WHITE, false);
    }
}