package RangerCaptain;

import RangerCaptain.cards.Camping;
import RangerCaptain.cards.Defend;
import RangerCaptain.cards.Levitate;
import RangerCaptain.cards.Strike;
import RangerCaptain.cards.interfaces.SkillAnimationAttack;
import RangerCaptain.relics.EspressoExpress;
import RangerCaptain.util.CustomSounds;
import RangerCaptain.util.GifDecoder;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.brashmonkey.spriter.Player;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.cutscenes.CutscenePanel;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import java.util.ArrayList;
import java.util.List;

import static RangerCaptain.MainModfile.*;

public class TheRangerCaptain extends CustomPlayer {
    private static final String[] orbTextures = {
            modID + "Resources/images/char/mainChar/orb/layer1.png",
            modID + "Resources/images/char/mainChar/orb/layer2.png",
            modID + "Resources/images/char/mainChar/orb/layer3.png",
            modID + "Resources/images/char/mainChar/orb/layer4.png",
            modID + "Resources/images/char/mainChar/orb/layer5.png",
            modID + "Resources/images/char/mainChar/orb/layer6.png",
            modID + "Resources/images/char/mainChar/orb/layer1d.png",
            modID + "Resources/images/char/mainChar/orb/layer2d.png",
            modID + "Resources/images/char/mainChar/orb/layer3d.png",
            modID + "Resources/images/char/mainChar/orb/layer4d.png",
            modID + "Resources/images/char/mainChar/orb/layer5d.png",};
    static final String ID = makeID(TheRangerCaptain.class.getSimpleName());
    static final CharacterStrings characterStrings = CardCrawlGame.languagePack.getCharacterString(ID);
    static final String[] NAMES = characterStrings.NAMES;
    static final String[] TEXT = characterStrings.TEXT;
    static Animation<TextureRegion> gifAnim;


    public TheRangerCaptain(String name, PlayerClass setClass) {
        super(name, setClass, orbTextures, modID + "Resources/images/char/mainChar/orb/vfx.png", new float[]{-20.0F, 20.0F, -40.0F, 40.0F, 0.0F}, new CustomSpriterAnimation(
                modID + "Resources/images/char/mainChar/OJCharacter.scml"));
        Player.PlayerListener listener = new CustomAnimationListener(this);
        ((CustomSpriterAnimation)this.animation).myPlayer.addListener(listener);
        initializeClass(null,
                SHOULDER1,
                SHOULDER2,
                CORPSE,
                getLoadout(), 10.0F, -10.0F, 220.0F, 240.0F, new EnergyManager(3)); // 20.0F, -10.0F, 166.0F, 327.0F


        dialogX = (drawX + 0.0F * Settings.scale);
        dialogY = (drawY + 240.0F * Settings.scale);

        if (gifAnim == null) {
            gifAnim = GifDecoder.loadGIFAnimation(Animation.PlayMode.LOOP, Gdx.files.internal(MainModfile.makePath("images/char/mainChar/CassetteBeasts_Player2.gif")).read());
        }
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(NAMES[0], TEXT[0],
                72,
                72,
                0,
                99,
                5, this, getStartingRelics(),
                getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Strike.ID);
        retVal.add(Levitate.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Defend.ID);
        retVal.add(Camping.ID);
        return retVal;
    }

    public ArrayList<String> getStartingRelics() {
        ArrayList<String> retVal = new ArrayList<>();
        retVal.add(EspressoExpress.ID);
        return retVal;
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.playA("ATTACK_FIRE", MathUtils.random(-0.2F, 0.2F));
        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT,
                false);
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FIRE";
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return Enums.HEADBAND_PURPLE_COLOR;
    }

    @Override
    public Color getCardTrailColor() {
        return MainModfile.HEADBAND_PURPLE_COLOR.cpy();
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontRed;
    }

    @Override
    public String getLocalizedCharacterName() {
        return NAMES[0];
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new Levitate();
    }

    @Override
    public String getTitle(AbstractPlayer.PlayerClass playerClass) {
        return NAMES[1];
    }

    @Override
    public AbstractPlayer newInstance() {
        return new TheRangerCaptain(name, chosenClass);
    }

    @Override
    public Color getCardRenderColor() {
        return MainModfile.HEADBAND_PURPLE_COLOR.cpy();
    }

    @Override
    public Color getSlashAttackColor() {
        return MainModfile.HEADBAND_PURPLE_COLOR.cpy();
    }

    @Override
    public AbstractGameAction.AttackEffect[] getSpireHeartSlashEffect() {
        return new AbstractGameAction.AttackEffect[]{
                AbstractGameAction.AttackEffect.FIRE,
                AbstractGameAction.AttackEffect.BLUNT_HEAVY
        };
    }

    @Override
    public Texture getCutsceneBg() {
        return ImageMaster.loadImage("RangerCaptainResources/images/panels/bkg.png");
    }

    @Override
    public List<CutscenePanel> getCutscenePanels() {
        List<CutscenePanel> panels = new ArrayList<>();
        panels.add(new CutscenePanel("RangerCaptainResources/images/panels/HeartPanel.png", CustomSounds.ITEM_GET_KEY));
        return panels;
    }

    @Override
    public String getSpireHeartText() {
        return TEXT[1];
    }

    @Override
    public String getVampireText() {
        return TEXT[2];
    }

    public static class Enums {
        @SpireEnum
        public static AbstractPlayer.PlayerClass THE_RANGER_CAPTAIN;
        @SpireEnum(name = "HEADBAND_PURPLE_COLOR")
        public static AbstractCard.CardColor HEADBAND_PURPLE_COLOR;
        @SpireEnum(name = "HEADBAND_PURPLE_COLOR")
        @SuppressWarnings("unused")
        public static CardLibrary.LibraryType LIBRARY_COLOR;
    }

    @Override
    public void renderPlayerImage(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        TextureRegion region = gifAnim.getKeyFrame(time);
        sb.draw(region, this.drawX + this.animX - region.getRegionWidth()*Settings.scale/4f, this.drawY + this.animY - region.getRegionWidth()*Settings.scale/16f + AbstractDungeon.sceneOffsetY, region.getRegionWidth()/2f, region.getRegionHeight()/2f);
    }

    @Override
    public void onVictory() {
        super.onVictory();
        playAnimation("happy");
    }

    @Override
    public void useCard(AbstractCard c, AbstractMonster monster, int energyOnUse) {
        super.useCard(c, monster, energyOnUse);
        switch (c.type) {
            case ATTACK:
                RandomChatterHelper.showChatter(RandomChatterHelper.getAttackText(), cardTalkProbability, enableCardBattleTalkEffect);
                if (c instanceof SkillAnimationAttack) {
                    playAnimation("skill");
                } else {
                    playAnimation("slam");
                }
                break;
            case POWER:
                RandomChatterHelper.showChatter(RandomChatterHelper.getPowerText(), cardTalkProbability, enableCardBattleTalkEffect);
                playAnimation("happy");
                break;
            default:
                RandomChatterHelper.showChatter(RandomChatterHelper.getSkillText(), cardTalkProbability, enableCardBattleTalkEffect);
                playAnimation("skill");
                break;
        }
    }

    public void damage(DamageInfo info) {
        boolean hadBlockBeforeSuper = this.currentBlock > 0;
        super.damage(info);
        boolean hasBlockAfterSuper = this.currentBlock > 0;
        boolean tookNoDamage = this.lastDamageTaken == 0;
        if (hadBlockBeforeSuper && (hasBlockAfterSuper || tookNoDamage)) {
            RandomChatterHelper.showChatter(RandomChatterHelper.getBlockedDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
            playAnimation("happy");
        } else {
            if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
                if (info.output >= 15) {
                    RandomChatterHelper.showChatter(RandomChatterHelper.getHeavyDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
                } else {
                    RandomChatterHelper.showChatter(RandomChatterHelper.getLightDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
                }
            } else if (info.type == DamageInfo.DamageType.THORNS && info.output > 0) {
                RandomChatterHelper.showChatter(RandomChatterHelper.getFieldDamageText(), damagedTalkProbability, enableDamagedBattleTalkEffect);
            }
            playAnimation("hurt");
        }
    }

    public CustomSpriterAnimation getAnimation() {
        return (CustomSpriterAnimation) this.animation;
    }

    public void playAnimation(String name) {
        ((CustomSpriterAnimation)this.animation).myPlayer.setAnimation(name);
    }

    public void stopAnimation() {
        CustomSpriterAnimation anim = (CustomSpriterAnimation) this.animation;
        int time = anim.myPlayer.getAnimation().length;
        anim.myPlayer.setTime(time);
        anim.myPlayer.speed = 0;
    }

    public void resetToIdleAnimation() {
        playAnimation("idle");
    }

    @Override
    public void playDeathAnimation() {
        RandomChatterHelper.showChatter(RandomChatterHelper.getKOText(), preTalkProbability, enablePreBattleTalkEffect); // I don't think this works
        playAnimation("ko");
    }

    @Override
    public void heal(int healAmount) {
        if (healAmount > 0) {
            if (RandomChatterHelper.showChatter(RandomChatterHelper.getHealingText(), damagedTalkProbability, enableDamagedBattleTalkEffect)){ //Technically changes your hp, lol
                playAnimation("happy");
            }
        }
        super.heal(healAmount);
    }

    @Override
    public void preBattlePrep() {
        playAnimation("idle");
        super.preBattlePrep();
        boolean bossFight = false;
        for (AbstractMonster mons : AbstractDungeon.getMonsters().monsters) {
            if (mons.type == AbstractMonster.EnemyType.BOSS) {
                bossFight = true;
                break;
            }
        }
        if (AbstractDungeon.getCurrRoom().eliteTrigger || bossFight) {
            RandomChatterHelper.showChatter(RandomChatterHelper.getBossFightText(), preTalkProbability, enablePreBattleTalkEffect);
        } else {
            if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth*0.5f) {
                RandomChatterHelper.showChatter(RandomChatterHelper.getLowHPBattleStartText(), preTalkProbability, enablePreBattleTalkEffect);
            } else {
                RandomChatterHelper.showChatter(RandomChatterHelper.getBattleStartText(), preTalkProbability, enablePreBattleTalkEffect);
            }
        }
    }

    public float[] _lightsOutGetCharSelectXYRI() {
        return new float[] {
                963*Settings.scale, 466*Settings.scale, 500f, 1.5f,
        };
    }

    public Color[] _lightsOutGetCharSelectColor() {
        return new Color[] {
                Color.ORANGE
        };
    }
}
