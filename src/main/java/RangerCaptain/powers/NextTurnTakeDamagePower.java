package RangerCaptain.powers;

import RangerCaptain.MainModfile;
import RangerCaptain.powers.interfaces.MonsterAtPlayerStartOfTurnPower;
import com.evacipated.cardcrawl.mod.stslib.patches.NeutralPowertypePatch;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import javassist.CtBehavior;

public class NextTurnTakeDamagePower extends AbstractEasyPower implements NonStackablePower, MonsterAtPlayerStartOfTurnPower {
    public static final String POWER_ID = MainModfile.makeID(NextTurnTakeDamagePower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public final AbstractGameAction.AttackEffect effect;
    public final DamageInfo info;

    public NextTurnTakeDamagePower(AbstractCreature owner, DamageInfo info, AbstractGameAction.AttackEffect effect) {
        super(POWER_ID, NAME, NeutralPowertypePatch.NEUTRAL, false, owner, info.base);
        this.info = info;
        this.effect = effect;
        updateDescription();
        LockedField.lockedDamage.set(this.info, this.info.base);
    }

    @Override
    public void updateDescription() {
        if (info == null) {
            this.description = "???";
        } else {
            this.description = DESCRIPTIONS[0] + info.base + DESCRIPTIONS[1];
        }
    }

    @Override
    public void atPlayerStartOfTurn() {
        flash();
        addToBot(new DamageAction(owner, info, effect));
        addToBot(new RemoveSpecificPowerAction(owner, owner, this));
    }

    @SpirePatch2(clz = DamageInfo.class, method = SpirePatch.CLASS)
    public static class LockedField {
        public static SpireField<Integer> lockedDamage = new SpireField<>(() -> null);
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "damage")
    public static class CheckLock {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(DamageInfo info) {
            Integer locked = LockedField.lockedDamage.get(info);
            if (locked != null) {
                info.output = locked;
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.FieldAccessMatcher(DamageInfo.class, "output");
                return new int[] {LineFinder.findAllInOrder(ctBehavior, m)[2]};
            }
        }
    }
}