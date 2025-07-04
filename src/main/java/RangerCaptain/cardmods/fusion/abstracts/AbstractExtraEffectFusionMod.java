package RangerCaptain.cardmods.fusion.abstracts;

import RangerCaptain.cardmods.DynvarInterface;
import RangerCaptain.cardmods.PurgeMod;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.cardvars.DynvarInterfaceManager;
import RangerCaptain.powers.interfaces.InfusionBoostingPower;
import RangerCaptain.util.CalcHelper;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import basemod.helpers.CardModifierManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashSet;

public abstract class AbstractExtraEffectFusionMod extends AbstractFusionMod implements DynvarInterface {
    private final static HashSet<String> registered = new HashSet<>();
    public enum VarType {
        DAMAGE_DIRECT,
        DAMAGE_RANDOM,
        DAMAGE_ALL,
        BLOCK,
        MAGIC
    }
    public VarType type;
    public int val;
    public int[] multiVal;
    public int baseVal;
    public boolean valModified;
    public boolean valUpgraded;

    public AbstractExtraEffectFusionMod(String identifier, VarType type, int baseAmount) {
        super(identifier);
        this.type = type;
        this.baseVal = this.val = baseAmount;
        if (registered.add(identifier)) {
            DynvarInterfaceManager.registerDynvarCarrier(identifier);
        }
    }

    public void makeTargeted(AbstractCard card) {
        if (card.target == AbstractCard.CardTarget.ALL_ENEMY || card.target == AbstractCard.CardTarget.NONE) {
            card.target = AbstractCard.CardTarget.ENEMY;
        }
        else if (card.target == AbstractCard.CardTarget.SELF || card.target == AbstractCard.CardTarget.ALL) {
            card.target = AbstractCard.CardTarget.SELF_AND_ENEMY;
        }
    }

    public void makeAttack(AbstractCard card) {
        /*if (card.type != AbstractCard.CardType.ATTACK) {
                if (card.type == AbstractCard.CardType.POWER) {
                    Wiz.att(new ApplyCardModifierAction(card, new PurgeMod()));
                }
                card.type = AbstractCard.CardType.ATTACK;
                PortraitHelper.setMaskedPortrait(card);
            }*/
        if (card.type != AbstractCard.CardType.ATTACK && card instanceof AbstractEasyCard) {
            if (card.type == AbstractCard.CardType.POWER) {
                CardModifierManager.addModifier(card, new PurgeMod());
            }
            card.type = AbstractCard.CardType.ATTACK;
            ((AbstractEasyCard) card).rollerKey += "Attack";
            CardArtRoller.computeCard((AbstractEasyCard) card);
        }
    }

    public int getModifiedBase(AbstractCard card) {
        int base = baseVal;
        switch (type) {
            case DAMAGE_DIRECT:
            case DAMAGE_RANDOM:
            case DAMAGE_ALL:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).damageBoost(card);
                    }
                }
                break;
            case BLOCK:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).blockBoost(card);
                    }
                }
                break;
            case MAGIC:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).magicBoost(card);
                    }
                }
                break;
        }
        return base;
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        int base = getModifiedBase(card);
        switch (type) {
            case DAMAGE_DIRECT:
            case DAMAGE_RANDOM:
                val = CalcHelper.applyPowers(base);
                break;
            case DAMAGE_ALL:
                multiVal = CalcHelper.applyPowersMulti(base);
                val = multiVal[0];
                break;
            case BLOCK:
                val = CalcHelper.applyPowersToBlock(base);
                break;
            case MAGIC:
                val = base;
                break;
        }
        valModified = val != baseVal;
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        int base = getModifiedBase(card);
        switch (type) {
            case DAMAGE_DIRECT:
                val = CalcHelper.calculateCardDamage(base, mo);
                break;
            case DAMAGE_RANDOM:
                val = CalcHelper.calculateCardDamage(base, null);
                break;
            case DAMAGE_ALL:
                multiVal = CalcHelper.calculateCardDamageMulti(base);
                val = multiVal[0];
                break;
            case BLOCK:
                val = CalcHelper.applyPowersToBlock(base);
                break;
            case MAGIC:
                val = base;
                break;
        }
        valModified = val != baseVal;
    }

    @Override
    public boolean removeAtEndOfTurn(AbstractCard card) {
        val = baseVal;
        valModified = false;
        return false;
    }

    public String descriptionKey() {
        return "!"+identifier+"!";
    }

    @Override
    public String key() {
        return identifier;
    }

    @Override
    public int val(AbstractCard card) {
        return val;
    }

    @Override
    public int baseVal(AbstractCard card) {
        return baseVal;
    }

    @Override
    public boolean modified(AbstractCard card) {
        return valModified;
    }

    @Override
    public boolean upgraded(AbstractCard card) {
        return valUpgraded;
    }
}
