package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import RangerCaptain.util.TexLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BobEffect;

public class IntentPatches {
    @SpireEnum
    public static AbstractMonster.Intent RANGER_BOMB;

    private static String[] TEXT;
    private static Texture bombIntentTexture;
    private static Texture bombTipTexture;

    @SpirePatch2(clz = AbstractMonster.class, method = "renderDamageRange")
    public static class RenderDamage {
        @SpirePostfixPatch
        public static void plz(AbstractMonster __instance, SpriteBatch sb, int ___intentDmg, BobEffect ___bobEffect, Color ___intentColor) {
            if (__instance.intent == RANGER_BOMB) {
                FontHelper.renderFontLeftTopAligned(sb, FontHelper.topPanelInfoFont, Integer.toString(___intentDmg), __instance.intentHb.cX - 30.0F * Settings.scale, __instance.intentHb.cY + ___bobEffect.y - 12.0F * Settings.scale, ___intentColor);
            }
        }
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "getIntentImg")
    public static class HaveImage {
        @SpirePrefixPatch
        public static SpireReturn<Texture> plz(AbstractMonster __instance) {
            if (__instance.intent == RANGER_BOMB) {
                if (bombIntentTexture == null) {
                    bombIntentTexture = TexLoader.getTexture(MainModfile.makeImagePath("ui/IntentBombL.png"));
                }
                return SpireReturn.Return(bombIntentTexture);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = AbstractMonster.class, method = "updateIntentTip")
    public static class HaveTipImage {
        @SpirePrefixPatch
        public static SpireReturn<Void> plz(AbstractMonster __instance, PowerTip ___intentTip, int ___intentDmg) {
            if (__instance.intent == RANGER_BOMB) {
                if (bombTipTexture == null) {
                    bombTipTexture = TexLoader.getTexture(MainModfile.makeImagePath("ui/IntentBomb.png"));
                    TEXT = CardCrawlGame.languagePack.getUIString(MainModfile.makeID("IntentPatches")).TEXT;
                }
                ___intentTip.header = TEXT[0];
                ___intentTip.body = TEXT[1] + ___intentDmg + TEXT[2];
                ___intentTip.img = bombTipTexture;
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
