package RangerCaptain.util;

import basemod.helpers.VfxBuilder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglGraphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.OverlayMenu;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import javassist.CtBehavior;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class GameSpeedController {
    private static float deltaDivisor = 1f;
    private static final ArrayList<SlowMotionEffect> slowMotionList = new ArrayList<>();

    public static void addSlowMotionEffect(SlowMotionEffect e) {
        slowMotionList.add(e);
        calculateDeltaDivisor();
    }

    public static float getDeltaDivisor() {
        return deltaDivisor;
    }

    public static float getEffectiveGameSpeed() {
        return 1f/deltaDivisor;
    }

    private static void calculateDeltaDivisor() {
        deltaDivisor = 1f;
        for (SlowMotionEffect e : slowMotionList) {
            deltaDivisor *= e.getSpeedDivisor();
        }
        if (CardCrawlGame.dungeon != null && AbstractDungeon.player != null) {
            for (AbstractPower p : AbstractDungeon.player.powers) {
                if (p instanceof GameSpeedModifyingObject) {
                    deltaDivisor *= ((GameSpeedModifyingObject) p).getGameSpeedDivisor();
                }
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                if (r instanceof GameSpeedModifyingObject) {
                    deltaDivisor *= ((GameSpeedModifyingObject) r).getGameSpeedDivisor();
                }
            }
        }
    }

    @SpirePatch(clz = LwjglGraphics.class, method = "getDeltaTime")
    @SpirePatch(clz = LwjglGraphics.class, method = "getRawDeltaTime")
    public static class WhatTheHell {
        @SpirePostfixPatch
        public static float speedAdjustment(float result, LwjglGraphics self) {
            return result / deltaDivisor;
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "update")
    public static class UpdateSlowMotion {
        @SpirePostfixPatch
        public static void update() {
            for (SlowMotionEffect e : slowMotionList) {
                e.update();
            }
            slowMotionList.removeIf(e -> e.isDone);
            calculateDeltaDivisor();
        }
    }

    @SpirePatch(clz = AbstractDungeon.class, method = "render")
    public static class RenderSlowMotion {
        @SpireInsertPatch(locator = Locator.class)
        public static void render(AbstractDungeon __instance, SpriteBatch sb) {
            for (SlowMotionEffect e : slowMotionList) {
                e.render(sb);
            }
        }
        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(OverlayMenu.class, "render");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "nextRoomTransition", paramtypez = SaveFile.class)
    public static class ClearList {
        @SpirePostfixPatch
        public static void die() {
            slowMotionList.clear();
            calculateDeltaDivisor();
        }
    }

    @SpirePatch2(clz = AbstractDungeon.class, method = "reset")
    public static class ClearList2 {
        @SpirePostfixPatch
        public static void die() {
            slowMotionList.clear();
            calculateDeltaDivisor();
        }
    }

    @SpirePatch2(clz = Sfx.class, method = "play", paramtypez = {float.class, float.class, float.class})
    public static class PitchShift {
        @SpirePrefixPatch
        public static void pls(Sfx __instance, float volume, @ByRef float[] y, float z) {
            y[0] /= (float)Math.sqrt(deltaDivisor);
        }
    }

    @SpirePatch2(clz = Sfx.class, method = "play", paramtypez = {float.class})
    public static class DeferToPitchAdjusted {
        @SpirePrefixPatch
        public static SpireReturn<?> pls(Sfx __instance, float volume) {
            if (deltaDivisor != 1) {
                return SpireReturn.Return(__instance.play(volume, (float) (1f/Math.sqrt(deltaDivisor)), 0f));
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = VfxBuilder.class, method = "velocity")
    public static class FixVelocity {
        @SpirePrefixPatch
        public static SpireReturn<VfxBuilder> plz(VfxBuilder __instance, float angle, float speed, List<Predicate<Float>> ___updaters) {
            float rads = 0.017453292F * angle;
            float scaledSpeed = speed * Settings.scale * Gdx.graphics.getDeltaTime();
            float dx = MathUtils.cos(rads) * scaledSpeed;
            float dy = MathUtils.sin(rads) * scaledSpeed;
            ___updaters.add(t -> {
                __instance.x += dx * getEffectiveGameSpeed();
                __instance.y += dy * getEffectiveGameSpeed();
                return false;
            });
            return SpireReturn.Return(__instance);
        }
    }

    @SpirePatch2(clz = VfxBuilder.class, method = "gravity")
    public static class FixGravity {
        @SpirePrefixPatch
        public static SpireReturn<VfxBuilder> plz(VfxBuilder __instance, float strength,float ___duration, List<Predicate<Float>> ___updaters) {
            ___updaters.add((t) -> {// 433
                __instance.y -= Settings.scale * strength * getEffectiveGameSpeed() * t / ___duration;
                return false;// 435
            });
            return SpireReturn.Return(__instance);
        }
    }

    public interface GameSpeedModifyingObject {
        float getGameSpeedDivisor();
    }

    public abstract static class AbstractRealTimeGameAction extends AbstractGameAction {
        @Override
        protected void tickDuration() {
            this.duration -= Gdx.graphics.getDeltaTime() * GameSpeedController.getDeltaDivisor();
            if (this.duration < 0.0F) {
                this.isDone = true;
            }
        }
    }

    public abstract static class AbstractRealTimeGameEffect extends AbstractGameEffect {
        @Override
        public void update() {
            this.duration -= Gdx.graphics.getDeltaTime() * GameSpeedController.getDeltaDivisor();
            if (this.duration < 0.0F) {
                this.isDone = true;
            }
        }
    }

    public static class SlowMotionAction extends AbstractGameAction {
        SlowMotionEffect effect;
        boolean added;
        boolean blocking;

        public SlowMotionAction(float speedDivisor, float duration, boolean blocking) {
            this.effect = new SlowMotionEffect(speedDivisor, duration);
            this.blocking = blocking;
        }

        @Override
        public void update() {
            if (!added) {
                added = true;
                addSlowMotionEffect(effect);
                if (!blocking) {
                    this.isDone = true;
                    return;
                }
            }
            this.isDone = effect.isDone;
        }
    }

    public static class SlowMotionEffect extends AbstractRealTimeGameEffect {
        protected float speedDivisor;

        public SlowMotionEffect(float speedDivisor, float duration) {
            this.startingDuration = this.duration = duration;
            this.speedDivisor = speedDivisor;
        }

        public float getSpeedDivisor() {
            return speedDivisor;
        }

        public void render(SpriteBatch sb) {}

        public void dispose() {}
    }
}
