package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

public class AnimatedCardPatches {
    @SpirePatch2(clz = AbstractCard.class, method = "renderPortrait")
    public static class LetThereBeGifs {
        @SpirePostfixPatch
        public static void plz(AbstractCard __instance, SpriteBatch sb) {
            if (__instance instanceof AbstractEasyCard) {
                if (((AbstractEasyCard) __instance).getGifOverlay() != null) {
                    TextureRegion anim = ((AbstractEasyCard) __instance).getGifOverlay().getKeyFrame(MainModfile.time);
                    float scale = Math.min(190f / anim.getRegionHeight(), 250f / anim.getRegionWidth());
                    float drawX = __instance.current_x - anim.getRegionWidth() / 2.0F;
                    float drawY = __instance.current_y - anim.getRegionHeight() / 2.0F;
                    sb.draw(anim, drawX, drawY + 72.0F/scale, anim.getRegionWidth() / 2.0F, anim.getRegionHeight() / 2.0F - 72.0F/scale, anim.getRegionWidth(), anim.getRegionHeight(), __instance.drawScale * Settings.scale * scale, __instance.drawScale * Settings.scale * scale, __instance.angle);
                }
            }
        }
    }

    @SpirePatch2(clz = SingleCardViewPopup.class, method = "renderPortrait")
    public static class WorkOnSCVToo {
        @SpirePostfixPatch
        public static void plz(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof AbstractEasyCard) {
                if (((AbstractEasyCard) ___card).getGifOverlay() != null) {
                    TextureRegion anim = ((AbstractEasyCard) ___card).getGifOverlay().getKeyFrame(MainModfile.time);
                    float scale = Math.min(380f / anim.getRegionHeight(), 500f / anim.getRegionWidth());
                    float drawX = Settings.WIDTH/2f - anim.getRegionWidth()/2f;
                    float drawY = Settings.HEIGHT/2f - anim.getRegionHeight()/2f;
                    sb.draw(anim, drawX, drawY + 136.0F * Settings.scale, anim.getRegionWidth()/2f, anim.getRegionHeight()/2f, anim.getRegionWidth(), anim.getRegionHeight(), Settings.scale * scale, Settings.scale * scale, 0.0F);
                }
            }
        }
    }
}
