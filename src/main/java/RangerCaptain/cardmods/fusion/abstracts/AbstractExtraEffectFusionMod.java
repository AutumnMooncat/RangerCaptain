package RangerCaptain.cardmods.fusion.abstracts;

import RangerCaptain.actions.ApplyCardModifierAction;
import RangerCaptain.cardmods.DynvarInterface;
import RangerCaptain.cardmods.PurgeMod;
import RangerCaptain.powers.interfaces.InfusionBoostingPower;
import RangerCaptain.util.CalcHelper;
import RangerCaptain.util.FormatHelper;
import RangerCaptain.util.PortraitHelper;
import RangerCaptain.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractExtraEffectFusionMod extends AbstractFusionMod implements DynvarInterface {
    public enum InfusionType {
        DAMAGE_DIRECT,
        DAMAGE_RANDOM,
        DAMAGE_ALL,
        BLOCK,
        MAGIC
    }
    public InfusionType type;
    public int val;
    public int[] multiVal;
    public int baseVal;
    public boolean valModified;
    public boolean valUpgraded;

    public AbstractExtraEffectFusionMod(String identifier, String modDescription, String cardText, InfusionType type, int baseAmount) {
        super(identifier, modDescription, cardText);
        this.type = type;
        this.baseVal = this.val = baseAmount;
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        if (type == InfusionType.DAMAGE_ALL || type == InfusionType.DAMAGE_DIRECT || type == InfusionType.DAMAGE_RANDOM) {
            if (card.type != AbstractCard.CardType.ATTACK) {
                if (card.type == AbstractCard.CardType.POWER) {
                    Wiz.att(new ApplyCardModifierAction(card, new PurgeMod()));
                }
                card.type = AbstractCard.CardType.ATTACK;
                PortraitHelper.setMaskedPortrait(card);
            }
        }
    }

    @Override
    public String modifyDescription(String rawDescription, AbstractCard card) {
        if (priority < 0) {
            return FormatHelper.insertBeforeText(rawDescription, String.format(cardText, descriptionKey()));
        } else {
            return FormatHelper.insertAfterText(rawDescription, String.format(cardText, descriptionKey()));
        }
    }

    @Override
    public void onApplyPowers(AbstractCard card) {
        int base = baseVal;
        switch (type) {
            case DAMAGE_DIRECT:
            case DAMAGE_RANDOM:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).damageBoost(card);
                    }
                }
                val = CalcHelper.applyPowers(base);
                break;
            case DAMAGE_ALL:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).damageBoost(card);
                    }
                }
                multiVal = CalcHelper.applyPowersMulti(base);
                val = multiVal[0];
                break;
            case BLOCK:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).blockBoost(card);
                    }
                }
                val = CalcHelper.applyPowersToBlock(base);
                break;
            case MAGIC:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        val += ((InfusionBoostingPower) p).magicBoost(card);
                    }
                }
                break;
        }
        valModified = val != baseVal;
    }

    @Override
    public void onCalculateCardDamage(AbstractCard card, AbstractMonster mo) {
        int base = baseVal;
        switch (type) {
            case DAMAGE_DIRECT:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).damageBoost(card);
                    }
                }
                val = CalcHelper.calculateCardDamage(base, mo);
                break;
            case DAMAGE_RANDOM:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).damageBoost(card);
                    }
                }
                val = CalcHelper.calculateCardDamage(base, null);
                break;
            case DAMAGE_ALL:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).damageBoost(card);
                    }
                }
                multiVal = CalcHelper.calculateCardDamageMulti(base);
                val = multiVal[0];
                break;
            case BLOCK:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        base += ((InfusionBoostingPower) p).blockBoost(card);
                    }
                }
                val = CalcHelper.applyPowersToBlock(base);
                break;
            case MAGIC:
                for (AbstractPower p : Wiz.adp().powers) {
                    if (p instanceof InfusionBoostingPower) {
                        val += ((InfusionBoostingPower) p).magicBoost(card);
                    }
                }
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
