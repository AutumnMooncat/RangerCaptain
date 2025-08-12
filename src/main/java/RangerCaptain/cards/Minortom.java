package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.AddCloseEncounterComponent;
import RangerCaptain.cardfusion.components.DrawComponent;
import RangerCaptain.cardfusion.components.StashNextCardComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.powers.CloseEncounterPower;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Minortom extends AbstractEasyCard {
    public final static String ID = makeID(Minortom.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MINORTOM)
                .withCost(1)
                .with(new DrawComponent(2), new StashNextCardComponent(1), new AddCloseEncounterComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.MAJORTOM)
                .withCost(1)
                .with(new DrawComponent(3), new StashNextCardComponent(1), new AddCloseEncounterComponent())
                .register();
    }

    public Minortom() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 2;
        baseSecondMagic = secondMagic = 1;
        setMonsterData(MonsterEnum.MINORTOM);
        tags.add(CustomTags.CLOSE_ENCOUNTER);
        ExtraEffectPatches.EffectFields.closeEncounter.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DrawCardAction(magicNumber));
        Wiz.applyToSelf(new CloseEncounterPower(p, secondMagic));
    }

    @Override
    public void upp() {
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.MAJORTOM);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, RED, WHITE, RED, WHITE, false);
    }
}