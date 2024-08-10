package RangerCaptain.cardmods;

import RangerCaptain.MainModfile;
import RangerCaptain.cardmods.fusion.abstracts.AbstractFusionMod;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.FusionForm;
import RangerCaptain.util.MonsterEnum;
import RangerCaptain.util.Wiz;
import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.MultiCardPreview;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardSave;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import java.util.ArrayList;

public class FusionMod extends AbstractCardModifier {
    public static final String ID = MainModfile.makeID(DealDamageMod.class.getSimpleName());
    public static final String[] TEXT = CardCrawlGame.languagePack.getCardStrings(ID).EXTENDED_DESCRIPTION;
    public MonsterEnum monster1;
    public MonsterEnum monster2;
    public transient FusionForm form;
    private final transient AbstractCard otherCard;
    private final CardSave otherCardData;
    private final ArrayList<AbstractCardModifier> otherCardModifiers = new ArrayList<>();
    private boolean inherentHack = true;

    public FusionMod(AbstractCard otherCard) {
        this.priority = -10;
        this.otherCard = otherCard;
        this.otherCardData = new CardSave(otherCard.cardID, otherCard.timesUpgraded, otherCard.misc);
        for (AbstractCardModifier cardmod : CardModifierManager.modifiers(otherCard)) {
            if (!cardmod.isInherent(otherCard)) {
                otherCardModifiers.add(cardmod.makeCopy());
            }
        }
    }

    private FusionMod(CardSave otherCardData, ArrayList<AbstractCardModifier> otherCardModifiers) {
        this(recoverCard(otherCardData, otherCardModifiers));
    }

    private static AbstractCard recoverCard(CardSave otherCardData, ArrayList<AbstractCardModifier> otherCardModifiers) {
        AbstractCard copy = CardLibrary.getCopy(otherCardData.id, otherCardData.upgrades, otherCardData.misc);
        for (AbstractCardModifier cardmod : otherCardModifiers) {
            CardModifierManager.addModifier(copy, cardmod);
        }
        return copy;
    }

    public static void updateFusionForm(AbstractCard card) {
        if (CardModifierManager.hasModifier(card, FusionMod.ID) && card instanceof AbstractEasyCard && ((AbstractEasyCard) card).getMonsterData() != null) {
            FusionMod mod = (FusionMod) CardModifierManager.getModifiers(card, FusionMod.ID).get(0);
            mod.monster1 = ((AbstractEasyCard) card).getMonsterData();
            mod.form = new FusionForm(mod.monster1, mod.monster2);
        }
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        AbstractCard preview1 = card.makeStatEquivalentCopy();
        AbstractCard preview2 = otherCard.makeStatEquivalentCopy();
        inherentHack = false;
        MultiCardPreview.add(card, preview1, preview2);
        MultiCardPreview.horizontalOnly(card);
        if (card instanceof AbstractEasyCard && otherCard instanceof AbstractEasyCard) {
            this.monster1 = ((AbstractEasyCard) card).getMonsterData();
            this.monster2 = ((AbstractEasyCard) otherCard).getMonsterData();
            this.form = new FusionForm(monster1, monster2);
            if (card.cost == -1 || otherCard.cost == -1) {
                card.cost = card.costForTurn = -1;
            } else {
                card.cost = card.costForTurn = Math.max(card.cost, otherCard.cost);
            }
            if (otherCard.target == AbstractCard.CardTarget.ENEMY) {
                if (card.target == AbstractCard.CardTarget.SELF) {
                    card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
                } else {
                    card.target = AbstractCard.CardTarget.ENEMY;
                }
            } else if (otherCard.target == AbstractCard.CardTarget.SELF_AND_ENEMY) {
                card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
            }
            if (otherCard.exhaust && card.type != AbstractCard.CardType.POWER) {
                card.exhaust = true;
            }
            if (otherCard.type == AbstractCard.CardType.POWER && card.type != AbstractCard.CardType.POWER) {
                card.type = AbstractCard.CardType.POWER;
                ((AbstractEasyCard) card).rollerKey += "Power";
                CardArtRoller.computeCard((AbstractEasyCard) card);
            } else if (otherCard.type == AbstractCard.CardType.ATTACK && card.type != AbstractCard.CardType.ATTACK && card.type != AbstractCard.CardType.POWER) {
                card.type = AbstractCard.CardType.ATTACK;
                ((AbstractEasyCard) card).rollerKey += "Attack";
                CardArtRoller.computeCard((AbstractEasyCard) card);
            }
        }
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        otherCard.applyPowers();
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        otherCard.calculateCardDamage(mo);
    }

    @Override
    public void onUse(AbstractCard card, AbstractCreature target, UseCardAction action) {
        otherCard.use(Wiz.adp(), (AbstractMonster) target);
    }

    @Override
    public String modifyName(String cardName, AbstractCard card) {
        return form.fusionName;
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        return card.name + " NL + NL " + otherCard.name;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return Wiz.canBeFused(card);
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean isInherent(AbstractCard card) {
        return inherentHack;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new FusionMod(otherCardData, otherCardModifiers);
    }
}
