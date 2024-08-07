package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.TexLoader;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.screens.SingleCardViewPopup;

import static com.badlogic.gdx.graphics.GL20.GL_DST_COLOR;
import static com.badlogic.gdx.graphics.GL20.GL_ZERO;

public class AnimatedCardPatches {
    private static final Texture attackTex = TexLoader.getTexture(MainModfile.makeImagePath("masks/AttackMask.png"));
    private static final Texture skillTex = TexLoader.getTexture(MainModfile.makeImagePath("masks/SkillMask.png"));
    private static final Texture powerTex = TexLoader.getTexture(MainModfile.makeImagePath("masks/PowerMask.png"));
    private static final TextureRegion attackMask = new TextureRegion(attackTex, attackTex.getWidth(), attackTex.getHeight());
    private static final TextureRegion skillMask = new TextureRegion(skillTex, skillTex.getWidth(), skillTex.getHeight());
    private static final TextureRegion powerMask = new TextureRegion(powerTex, powerTex.getWidth(), powerTex.getHeight());
    private static final FrameBuffer fbo = new FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, false);


    public static TextureRegion getMask(AbstractCard card) {
        switch (card.type) {
            case SKILL:
            case STATUS:
            case CURSE:
                return skillMask;
            case ATTACK:
                return attackMask;
            case POWER:
                return powerMask;
        }
        return skillMask;
    }

    @SpirePatch2(clz = AbstractCard.class, method = "renderPortrait")
    public static class LetThereBeGifs {
        @SpirePostfixPatch
        public static void plz(AbstractCard __instance, SpriteBatch sb) {
            if (__instance instanceof AbstractEasyCard) {
                if (((AbstractEasyCard) __instance).getGifOverlay() != null) {
                    prepFrameBuffer(sb);
                    drawCardAnimation(sb, (AbstractEasyCard) __instance);
                    maskAnimation(sb, (AbstractEasyCard) __instance);
                    finalizeAndDraw(sb);
                }
            }
        }
    }

    private static void prepFrameBuffer(SpriteBatch sb) {
        sb.end();
        fbo.begin();
        Gdx.gl.glClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        Gdx.gl.glColorMask(true, true, true, true);
        sb.begin();
    }

    private static void drawCardAnimation(SpriteBatch sb, AbstractEasyCard card) {
        TextureRegion anim = card.getGifOverlay().getKeyFrame(MainModfile.time);
        float scale = Math.min(190f / anim.getRegionHeight(), 250f / anim.getRegionWidth());
        float drawX = card.current_x - anim.getRegionWidth() / 2.0F;
        float drawY = card.current_y - anim.getRegionHeight() / 2.0F;
        sb.draw(anim, drawX, drawY + 72.0F/scale, anim.getRegionWidth() / 2.0F, anim.getRegionHeight() / 2.0F - 72.0F/scale, anim.getRegionWidth(), anim.getRegionHeight(), card.drawScale * Settings.scale * scale, card.drawScale * Settings.scale * scale, card.angle);
    }

    private static void maskAnimation(SpriteBatch sb, AbstractEasyCard card) {
        int srcfunc = sb.getBlendSrcFunc();
        int dstfunc = sb.getBlendDstFunc();
        Color color = sb.getColor();
        sb.setBlendFunction(GL_DST_COLOR, GL_ZERO);
        sb.setColor(Color.WHITE);
        TextureRegion mask = getMask(card);
        float maskX = card.current_x - mask.getRegionWidth() / 2.0F;
        float maskY = card.current_y - mask.getRegionHeight() / 2.0F;
        sb.draw(mask, maskX, maskY + 72.0F, mask.getRegionWidth()/2f, mask.getRegionHeight()/2f - 72.0F, mask.getRegionWidth(), mask.getRegionHeight(), card.drawScale * Settings.scale, card.drawScale * Settings.scale, card.angle);
        sb.setBlendFunction(srcfunc, dstfunc);
        sb.setColor(color);
    }

    private static void finalizeAndDraw(SpriteBatch sb) {
        sb.end();
        fbo.end();
        sb.begin();
        TextureRegion drawTex = new TextureRegion(fbo.getColorBufferTexture());
        drawTex.flip(false, true);
        sb.draw(drawTex, -Settings.VERT_LETTERBOX_AMT, -Settings.HORIZ_LETTERBOX_AMT);
    }

    @SpirePatch2(clz = SingleCardViewPopup.class, method = "renderPortrait")
    public static class WorkOnSCVToo {
        @SpirePostfixPatch
        public static void plz(SingleCardViewPopup __instance, SpriteBatch sb, AbstractCard ___card) {
            if (___card instanceof AbstractEasyCard) {
                if (((AbstractEasyCard) ___card).getGifOverlay() != null) {
                    prepFrameBuffer(sb);
                    drawCardAnimationSCV(sb, (AbstractEasyCard) ___card);
                    maskAnimationSCV(sb, (AbstractEasyCard) ___card);
                    finalizeAndDraw(sb);
                }
            }
        }
    }

    private static void drawCardAnimationSCV(SpriteBatch sb, AbstractEasyCard card) {
        TextureRegion anim = card.getGifOverlay().getKeyFrame(MainModfile.time);
        float scale = Math.min(380f / anim.getRegionHeight(), 500f / anim.getRegionWidth());
        float drawX = Settings.WIDTH/2f - anim.getRegionWidth()/2f;
        float drawY = Settings.HEIGHT/2f - anim.getRegionHeight()/2f;
        sb.draw(anim, drawX, drawY + 136.0F * Settings.scale, anim.getRegionWidth()/2f, anim.getRegionHeight()/2f, anim.getRegionWidth(), anim.getRegionHeight(), Settings.scale * scale, Settings.scale * scale, 0.0F);
    }

    private static void maskAnimationSCV(SpriteBatch sb, AbstractEasyCard card) {
        int srcfunc = sb.getBlendSrcFunc();
        int dstfunc = sb.getBlendDstFunc();
        Color color = sb.getColor();
        sb.setBlendFunction(GL_DST_COLOR, GL_ZERO);
        sb.setColor(Color.WHITE);
        TextureRegion mask = getMask(card);
        float maskX = Settings.WIDTH/2f - mask.getRegionWidth() / 2.0F;
        float maskY = Settings.HEIGHT/2f - mask.getRegionHeight() / 2.0F;
        sb.draw(mask, maskX, maskY + 136.0F * Settings.scale, mask.getRegionWidth()/2f, mask.getRegionHeight()/2f, mask.getRegionWidth(), mask.getRegionHeight(), Settings.scale*2, Settings.scale*2, 0.0F);
        sb.setBlendFunction(srcfunc, dstfunc);
        sb.setColor(color);
    }
}
