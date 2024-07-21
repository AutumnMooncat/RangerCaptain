package RangerCaptain.patches;

import RangerCaptain.MainModfile;
import RangerCaptain.util.TexLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;

public class AttackEffectPatches {
    private static TextureAtlas.AtlasRegion bleedRegion;

    @SpireEnum
    public static AbstractGameAction.AttackEffect RANGER_CAPTAIN_BLEED;

    @SpirePatch(clz = FlashAtkImgEffect.class, method = "loadImage")
    public static class ImagePatch {
        @SpirePrefixPatch
        public static SpireReturn<TextureAtlas.AtlasRegion> plz(FlashAtkImgEffect __instance, AbstractGameAction.AttackEffect ___effect) {
            if (___effect == RANGER_CAPTAIN_BLEED) {
                if (bleedRegion == null) {
                    Texture bleedTex = TexLoader.getTexture(MainModfile.makeImagePath("vfx/bleed.png"));
                    bleedRegion = new TextureAtlas.AtlasRegion(bleedTex, 0, 0, bleedTex.getWidth(), bleedTex.getHeight());
                }
                return SpireReturn.Return(bleedRegion);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = FlashAtkImgEffect.class, method = "playSound")
    public static class SoundPatch {
        @SpirePrefixPatch
        public static SpireReturn<Void> plz(FlashAtkImgEffect __instance, AbstractGameAction.AttackEffect effect) {
            if (effect == RANGER_CAPTAIN_BLEED) {
                CardCrawlGame.sound.play("ATTACK_POISON");
                return SpireReturn.Return();
            }
            return SpireReturn.Continue();
        }
    }
}
