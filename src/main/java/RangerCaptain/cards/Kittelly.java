package RangerCaptain.cards;

import RangerCaptain.cardfusion.FusionComponentHelper;
import RangerCaptain.cardfusion.components.MadnessComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.patches.YeetCardPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static RangerCaptain.MainModfile.makeID;

public class Kittelly extends AbstractEasyCard {
    public final static String ID = makeID(Kittelly.class.getSimpleName());
    private AbstractCard lastCard;

    static {
        new FusionComponentHelper(MonsterEnum.KITTELLY)
                .withCost(0)
                .with(new MadnessComponent(1))
                .withExhaust()
                .register();
        new FusionComponentHelper(MonsterEnum.CATFIVE)
                .withCost(0)
                .with(new MadnessComponent(2))
                .withExhaust()
                .register();
    }

    public Kittelly() {
        super(ID, 0, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.NONE); // TODO Proper component?
        setMonsterData(MonsterEnum.KITTELLY);
        setElementalType(ElementalType.LIGHTNING);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        Wiz.atb(new SFXAction("ORB_DARK_EVOKE", 0.05F));
        ArrayList<AbstractCard> validCards = AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().filter(card -> !(card instanceof Kittelly) && !card.purgeOnUse).collect(Collectors.toCollection(ArrayList::new));
        if (!validCards.isEmpty()) {
            AbstractCard card = validCards.get(validCards.size()-1).makeStatEquivalentCopy();
            if (upgraded) {
                card.setCostForTurn(0);
            }
            Wiz.atb(new MakeTempCardInHandAction(card, false, true));
            Wiz.atb(new AbstractGameAction() {
                @Override
                public void update() {
                    YeetCardPatches.YeetField.yeet.set(Kittelly.this, true);
                    this.isDone = true;
                }
            });
        }
        this.rawDescription = baseDesc();
        lastCard = null;
        this.initializeDescription();
    }

    public void triggerOnGlowCheck() {
        if (AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().allMatch(card -> card instanceof Kittelly || card.purgeOnUse)) {
            this.glowColor = Settings.RED_TEXT_COLOR.cpy();
        } else {
            this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        }
    }

    public void applyPowers() {
        super.applyPowers();
        ArrayList<AbstractCard> validCards = AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().filter(card -> !(card instanceof Kittelly) && !card.purgeOnUse).collect(Collectors.toCollection(ArrayList::new));
        if (!validCards.isEmpty()) {
            AbstractCard preview = validCards.get(validCards.size()-1);
            if (preview != lastCard) {
                lastCard = preview;
                cardsToPreview = lastCard.makeStatEquivalentCopy();
                this.rawDescription = baseDesc() + cardStrings.EXTENDED_DESCRIPTION[1] + FormatHelper.prefixWords(CardModifierManager.onRenderTitle(lastCard, lastCard.name), "[#efc851]", "[]") + cardStrings.EXTENDED_DESCRIPTION[2];
                this.initializeDescription();
            }
        }
    }

    private String baseDesc() {
        return upgraded ? cardStrings.UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
    }

    @Override
    public void upp() {
        uDesc();
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.CATFIVE);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(BLUE), WHITE, darken(BLUE), WHITE, false);
    }
}