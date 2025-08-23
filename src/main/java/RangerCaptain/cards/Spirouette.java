package RangerCaptain.cards;

import RangerCaptain.actions.FormalComplaintAction;
import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.YeetWeakVulnComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Spirouette extends AbstractEasyCard {
    public final static String ID = makeID(Spirouette.class.getSimpleName());

    static {
        // 9 -> 13
        new FusionComponentHelper(MonsterEnum.SPIROUETTE)
                .withCost(1)
                .withBlock(6.5f)
                .with(new YeetWeakVulnComponent())
                .register();
        // 12 -> 16
        new FusionComponentHelper(MonsterEnum.REGENSEA)
                .withCost(1)
                .withBlock(8)
                .with(new YeetWeakVulnComponent())
                .register();
    }

    public Spirouette() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF_AND_ENEMY);
        baseBlock = block = 9;
        setMonsterData(MonsterEnum.SPIROUETTE);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        blck();
        addToBot(new FormalComplaintAction(m));
    }

    @Override
    public void upp() {
        upgradeBlock(3);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.REGENSEA);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(Color.PINK), WHITE, darken(Color.PINK), WHITE, false);
    }
}