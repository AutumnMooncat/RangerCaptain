package RangerCaptain.cards;

import RangerCaptain.actions.BetterSelectCardsInHandAction;
import RangerCaptain.cardmods.fusion.FusionComponentHelper;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cardmods.fusion.components.BlockComponent;
import RangerCaptain.cardmods.fusion.components.ExhaustCardsComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.CustomTags;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static RangerCaptain.MainModfile.makeID;

public class Burnace extends AbstractEasyCard {
    public final static String ID = makeID(Burnace.class.getSimpleName());

    static {
        new FusionComponentHelper(MonsterEnum.BURNACE)
                .withCost(1)
                .withFlags(new BlockComponent(3), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .with(new ExhaustCardsComponent(2, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .register();
        new FusionComponentHelper(MonsterEnum.SMOGMAGOG)
                .withCost(1)
                .withFlags(new BlockComponent(5), AbstractComponent.Flag.EXHAUST_FOLLOWUP)
                .with(new ExhaustCardsComponent(2, ExhaustCardsComponent.TargetPile.HAND, true, false))
                .register();
    }

    public Burnace() {
        super(ID, 1, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
        baseBlock = block = 4;
        baseMagicNumber = magicNumber = 3;
        setMonsterData(MonsterEnum.BURNACE);
        tags.add(CustomTags.MAGIC_EXHAUST);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new BetterSelectCardsInHandAction(magicNumber, ExhaustAction.TEXT[0], true, true, c -> true, cards -> {
            for (AbstractCard card : cards) {
                addToTop(new GainBlockAction(p, block));
                addToTop(new ExhaustSpecificCardAction(card, p.hand, true));
            }
        }));
    }

    @Override
    public void upp() {
        upgradeBlock(2);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.SMOGMAGOG);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, DARK_GRAY, WHITE, DARK_GRAY, WHITE, false);
    }
}