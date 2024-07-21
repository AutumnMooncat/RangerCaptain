package RangerCaptain;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.vfx.SpeechBubble;

public class RandomChatterHelper {

    private static final CardStrings AttackTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("AttackTextContainer"));

    private static final CardStrings SkillTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("SkillTextContainer"));

    private static final CardStrings PowerTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("PowerTextContainer"));

    private static final CardStrings BattleStartTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("BattleStartTextContainer"));

    private static final CardStrings LowHPBattleStartTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("LowHPBattleStartTextContainer"));

    private static final CardStrings BattleEndTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("BattleEndTextContainer"));

    private static final CardStrings BossFightTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("BossFightTextContainer"));

    private static final CardStrings HealingTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("HealingTextContainer"));

    private static final CardStrings FieldDamageTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("FieldDamageTextContainer"));

    private static final CardStrings LightDamageTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("LightDamageTextContainer"));

    private static final CardStrings HeavyDamageTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("HeavyDamageTextContainer"));

    private static final CardStrings BlockedDamageTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("BlockedDamageTextContainer"));

    private static final CardStrings KOTextContainer = CardCrawlGame.languagePack.getCardStrings(MainModfile.makeID("KOTextContainer"));

    public static String getAttackText() {
        return AttackTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, AttackTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getSkillText() {
        return SkillTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, SkillTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getPowerText() {
        return PowerTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, PowerTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getBattleStartText() {
        return BattleStartTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, BattleStartTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getLowHPBattleStartText() {
        return LowHPBattleStartTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, LowHPBattleStartTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getBattleEndText() {
        return BattleEndTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, BattleEndTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getBossFightText() {
        return BossFightTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, BossFightTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getHealingText() {
        return HealingTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, HealingTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getFieldDamageText() {
        return FieldDamageTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, FieldDamageTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getLightDamageText() {
        return LightDamageTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, LightDamageTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getHeavyDamageText() {
        return HeavyDamageTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, HeavyDamageTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getBlockedDamageText() {
        return BlockedDamageTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, BlockedDamageTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    public static String getKOText() {
        return KOTextContainer.EXTENDED_DESCRIPTION[MathUtils.random(0, KOTextContainer.EXTENDED_DESCRIPTION.length-1)];
    }

    /**
     *
     * @param chatterText - The string to say if we show text
     * @param probability - The chance we actually talk
     * @param conditional - If we actually will show text or not. Mainly used for config stuff
     * @return - Returns true if we talked or false if we did not. Can be used to have additional conditionals on chat
     */
    public static boolean showChatter(String chatterText, int probability, boolean conditional) {
        if(conditional && MathUtils.random(1, 100) <= probability) {
            AbstractDungeon.effectList.add(new SpeechBubble(AbstractDungeon.player.dialogX, AbstractDungeon.player.dialogY, 2.0f, chatterText, true));
            //AbstractDungeon.actionManager.addToTop(new TalkAction(true, chatterText, 0.0f, 2.0f));
            return true;
        }
        return false;
    }
}
