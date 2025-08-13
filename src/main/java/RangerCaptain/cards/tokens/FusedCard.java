package RangerCaptain.cards.tokens;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardfusion.abstracts.AbstractComponent;
import RangerCaptain.cardfusion.components.BinvasionComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.cards.interfaces.ManuallySizeAdjustedCard;
import RangerCaptain.cards.interfaces.OnOtherCardStashedCard;
import RangerCaptain.patches.CantUpgradeFieldPatches;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.Wiz;
import basemod.abstracts.CustomSavable;
import basemod.patches.com.megacrit.cardcrawl.dungeons.AbstractDungeon.NoPools;
import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import java.util.ArrayList;
import java.util.List;

import static RangerCaptain.MainModfile.makeID;

@NoPools
@NoCompendium
public class FusedCard extends AbstractEasyCard implements AbstractComponent.ComponentAmountProvider, CustomSavable<List<AbstractComponent>>, OnOtherCardStashedCard, ManuallySizeAdjustedCard {
    public final static String ID = makeID(FusedCard.class.getSimpleName());
    private final List<AbstractComponent> originals = new ArrayList<>();
    private final transient List<AbstractComponent> components = new ArrayList<>();
    private boolean delayedUpgrade;
    public Color anchor1;
    public Color anchor2;
    public Color target1;
    public Color target2;
    public boolean flipX = false;
    private boolean doMultiDamage;

    public FusedCard() {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE);
    }

    public FusedCard(List<AbstractComponent> components) {
        super(ID, 0, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE);
        processComponents(components);
        CantUpgradeFieldPatches.CantUpgradeField.preventUpgrades.set(this, true);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractComponent component : components) {
            if (!component.wasCaptured) {
                component.onTrigger(this, p, m, component.capturedComponents);
            }
        }
        if (cost == -1) {
            addToBot(new DoAction(() -> {
                if (!this.freeToPlayOnce) {
                    AbstractDungeon.player.energy.use(EnergyPanel.totalCount);
                }
            }));
        }
    }

    @Override
    public void triggerOnExhaust() {
        for (AbstractComponent component : components) {
            if (!component.wasCaptured) {
                component.triggerOnExhaust(this, component.capturedComponents);
            }
        }
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        for (AbstractComponent component : components) {
            if (!component.canUse(this, p, m)) {
                return false;
            }
        }
        return super.canUse(p, m);
    }

    @Override
    public void applyPowers() {
        if (doMultiDamage) {
            isMultiDamage = true;
            super.applyPowers();
            isMultiDamage = false;
        }
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (doMultiDamage) {
            isMultiDamage = true;
            super.calculateCardDamage(mo);
            isMultiDamage = false;
        }
        super.calculateCardDamage(mo);
    }

    @Override
    public void triggerOnGlowCheck() {
        // TODO allow multiple glows
        glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR;
        for (AbstractComponent component : components) {
            component.glowCheck(this);
        }
    }

    @Override
    public AbstractCard makeCopy() {
        FusedCard copy = new FusedCard(originals);
        for (AbstractComponent component : components) {
            component.onMakeCopy(this, copy);
        }
        return copy;
    }

    @Override
    public boolean canUpgrade() {
        return super.canUpgrade() && (getUpgradeMult() != 1f || cost > 0);
    }

    @Override
    public void upp() {
        if (components.isEmpty()) {
            delayedUpgrade = true;
        } else {
            delayedUpgrade = false;
            float mult = getUpgradeMult();

            if (mult != 1f) {
                if (baseDamage > 0) {
                    upgradeDamage((int) Math.max(1, baseDamage * mult));
                }
                if (baseSecondDamage > 0) {
                    upgradeSecondDamage((int) Math.max(1, baseSecondDamage * mult));
                }
                if (baseBlock > 0) {
                    upgradeBlock((int) Math.max(1, baseBlock * mult));
                }
                if (baseSecondBlock > 0) {
                    upgradeSecondBlock((int) Math.max(1, baseSecondBlock * mult));
                }
                if (baseMagicNumber > 0) {
                    upgradeMagicNumber((int) Math.max(1, baseMagicNumber * mult));
                }
                if (baseSecondMagic > 0) {
                    upgradeSecondMagic((int) Math.max(1, baseSecondMagic * mult));
                }
                if (baseThirdMagic > 0) {
                    upgradeThirdMagic((int) Math.max(1, baseThirdMagic * mult));
                }
            } else {
                upgradeBaseCost(cost - 1);
            }
        }
    }

    private float getUpgradeMult() {
        boolean hasDamage = baseDamage > 0 || baseSecondDamage > 0;
        boolean hasBlock = baseBlock > 0 || baseSecondBlock > 0;
        boolean hasMagic = baseMagicNumber > 0 || baseSecondMagic > 0 || baseThirdMagic > 0;
        float mult = 1f;
        if (hasDamage && hasBlock && hasMagic) {
            mult = 1.1f;
        } else if ((hasDamage && hasBlock) || (hasDamage && hasMagic) || (hasBlock && hasMagic)) {
            mult = 1.2f;
        } else if (hasDamage || hasBlock || hasMagic) {
            mult = 1.3f;
        }
        return mult;
    }

    public int binvasionCount() {
        return components.stream().filter(c -> c instanceof BinvasionComponent).map(c -> ((BinvasionComponent) c).binvaderCount).reduce(0, Integer::sum);
    }

    @Override
    public int getAmount(AbstractComponent component) {
        int effect = 1;
        if (cost == -1) {
            effect = EnergyPanel.totalCount;
            if (this.energyOnUse != -1) {
                effect = this.energyOnUse;
            }

            if (Wiz.adp().hasRelic("Chemical X")) {
                effect += 2;
                Wiz.adp().getRelic("Chemical X").flash();
            }
        }
        switch (component.dynvar) {
            case DAMAGE:
                return damage * effect;
            case DAMAGE2:
                return secondDamage * effect;
            case BLOCK:
                return block * effect;
            case BLOCK2:
                return secondBlock * effect;
            case MAGIC:
                return magicNumber * effect;
            case MAGIC2:
                return secondMagic * effect;
            case MAGIC3:
                return thirdMagic * effect;
            case FLAT:
                return component.workingAmount;
            default:
                return -1;
        }
    }

    private void processComponents(List<AbstractComponent> toAdd) {
        this.originals.clear();
        this.components.clear();
        this.originals.addAll(toAdd);
        this.components.addAll(AbstractComponent.resolve(this, toAdd));
        initializeDescription();
        doMultiDamage = components.stream().anyMatch(c -> c.target == AbstractComponent.ComponentTarget.ENEMY_AOE && (c.dynvar == AbstractComponent.DynVar.DAMAGE || c.dynvar == AbstractComponent.DynVar.DAMAGE2));
        for (AbstractComponent component : components) {
            switch (component.dynvar) {
                case DAMAGE:
                    baseDamage = damage = component.workingAmount;
                    break;
                case DAMAGE2:
                    baseSecondDamage = secondDamage = component.workingAmount;
                    break;
                case BLOCK:
                    baseBlock = block = component.workingAmount;
                    break;
                case BLOCK2:
                    baseSecondBlock = secondBlock = component.workingAmount;
                    break;
                case MAGIC:
                    baseMagicNumber = magicNumber = component.workingAmount;
                    break;
                case MAGIC2:
                    baseSecondMagic = secondMagic = component.workingAmount;
                    break;
                case MAGIC3:
                    baseThirdMagic = thirdMagic = component.workingAmount;
                    break;
                default:
                    break;
            }
        }
        if (delayedUpgrade) {
            upp();
        }
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        if (anchor1 != null && anchor2 != null && target1 != null && target2 != null) {
            return new CardArtRoller.ReskinInfo(ID, anchor1, anchor2, target1, target2, flipX);
        }
        return new CardArtRoller.ReskinInfo(ID, DARK_GRAY, WHITE, DARK_GRAY, WHITE, flipX);
    }

    @Override
    public List<AbstractComponent> onSave() {
        return originals;
    }

    @Override
    public void onLoad(List<AbstractComponent> components) {
        if (components != null) {
            processComponents(components);
        }
    }

    @Override
    public void triggerWhenDrawn() {
        for (AbstractComponent component : components) {
            component.triggerWhenDrawn(this);
        }
    }

    @Override
    public void onStashedOther(AbstractCard card) {
        for (AbstractComponent component : components) {
            component.triggerOnOtherCardStashed(this, card);
        }
    }

    @Override
    public void atTurnStart() {
        for (AbstractComponent component : components) {
            component.atTurnStart(this);
        }
    }

    @Override
    public float getAdjustedScale() {
        float scale = 1f;
        if (components != null) {
            for (AbstractComponent component : components) {
                scale = Math.min(scale, component.textSize(this));
            }
        }
        return scale;
    }
}