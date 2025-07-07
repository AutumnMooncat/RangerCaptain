package RangerCaptain.cards.tokens;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.abstracts.AbstractComponent;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
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
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static RangerCaptain.MainModfile.makeID;

@NoPools
@NoCompendium
public class FusedCard extends AbstractEasyCard implements AbstractComponent.ComponentAmountProvider, CustomSavable<List<AbstractComponent>> {
    public final static String ID = makeID(FusedCard.class.getSimpleName());
    private final List<AbstractComponent> originals = new ArrayList<>();
    private final transient List<AbstractComponent> components = new ArrayList<>();
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
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractComponent component : components) {
            if (!component.isCaptured(components)) {
                List<AbstractComponent> captured = components.stream().filter(component::captures).collect(Collectors.toList());
                component.onTrigger(this, p, m, captured);
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
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public AbstractCard makeCopy() {
        return new FusedCard(originals);
    }

    @Override
    public void upp() {}

    /*@Override
    public void addUpgrades() {
        addUpgradeData(this::upgrade0);
        addUpgradeData(this::upgrade1);
        setExclusions(0, 1);
    }

    public void upgrade0() {
    }

    public void upgrade1() {
    }*/

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
                return component.baseAmount;
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
                    baseDamage = damage = component.baseAmount;
                    break;
                case DAMAGE2:
                    baseSecondDamage = secondDamage = component.baseAmount;
                    break;
                case BLOCK:
                    baseBlock = block = component.baseAmount;
                    break;
                case BLOCK2:
                    baseSecondBlock = secondBlock = component.baseAmount;
                    break;
                case MAGIC:
                    baseMagicNumber = magicNumber = component.baseAmount;
                    break;
                case MAGIC2:
                    baseSecondMagic = secondMagic = component.baseAmount;
                    break;
                case MAGIC3:
                    baseThirdMagic = thirdMagic = component.baseAmount;
                    break;
                default:
                    break;
            }
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
}