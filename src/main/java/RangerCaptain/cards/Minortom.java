package RangerCaptain.cards;

import RangerCaptain.actions.StashTopCardsAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.components.AddCloseEncounterComponent;
import RangerCaptain.cardmods.fusion.components.StashTopCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.patches.ExtraEffectPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Minortom extends AbstractEasyCard {
    public final static String ID = makeID(Minortom.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.MINORTOM)
                .withCost(1)
                .with(new StashTopCardsComponent(3))
                .with(new AddCloseEncounterComponent())
                .register();
        new FusionComponentHelper(MonsterEnum.MAJORTOM)
                .withCost(1)
                .with(new StashTopCardsComponent(4))
                .with(new AddCloseEncounterComponent())
                .register();
    }

    public Minortom() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE);
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.MINORTOM);
        tags.add(CustomTags.CLOSE_ENCOUNTER);
        ExtraEffectPatches.EffectFields.closeEncounter.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new StashTopCardsAction(magicNumber));
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