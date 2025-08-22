package RangerCaptain.ui;

import RangerCaptain.MainModfile;
import RangerCaptain.actions.FusionAction;
import RangerCaptain.patches.FusionButtonPatches;
import RangerCaptain.relics.PearFusilli;
import RangerCaptain.util.Wiz;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import com.megacrit.cardcrawl.vfx.EndTurnGlowEffect;
import com.megacrit.cardcrawl.vfx.EndTurnLongPressBarFlashEffect;

import java.util.ArrayList;

public class FusionButton {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(MainModfile.makeID(FusionButton.class.getSimpleName()));
    public static final String[] TIP_TEXT = uiStrings.EXTRA_TEXT;
    public static final String[] BUTTON_TEXT = uiStrings.TEXT;
    private String label;
    public static final String FUSE_TEXT = BUTTON_TEXT[0];
    public static final String ALREADY_FUSED_TEXT = BUTTON_TEXT[1];
    public static final String CANT_AFFORD_TEXT = BUTTON_TEXT[2];
    private static final Color DISABLED_COLOR = new Color(0.7F, 0.7F, 0.7F, 1.0F);
    private static final Color ENABLED_COLOR = new Color(1,1,1,1);
    private static final float SHOW_X = 198.0F * Settings.xScale;//1640.0F * Settings.xScale;
    private static final float SHOW_Y = 320.0F * Settings.yScale;
    private static final float HIDE_X = SHOW_X - 500.0F * Settings.xScale;
    private AbstractMonster hoveredCollider;
    private float fadeTimer = 0f;
    private static final float FADE_TIME = 1.0f;
    private float current_x;
    private float current_y;
    private float target_x;
    private boolean isHidden;
    public boolean enabled;
    private boolean isDisabled;
    public boolean fusedThisTurn;
    private Color textColor;
    private final ArrayList<EndTurnGlowEffect> glowList;
    private static final float GLOW_INTERVAL = 1.2F;
    private float glowTimer;
    public boolean isGlowing;
    private final Hitbox hb;
    private float holdProgress;
    private static final float HOLD_DUR = 0.4F;
    private final Color holdBarColor;
    private boolean justClicked;

    public FusionButton() {
        label = BUTTON_TEXT[0];
        current_x = HIDE_X;
        current_y = SHOW_Y;
        target_x = current_x;
        isHidden = true;
        enabled = false;
        isDisabled = false;
        fusedThisTurn = false;
        glowList = new ArrayList<>();
        glowTimer = 0.0F;
        isGlowing = false;
        hb = new Hitbox(0.0F, 0.0F, 230.0F * Settings.scale, 110.0F * Settings.scale);
        holdProgress = 0.0F;
        holdBarColor = new Color(1.0F, 1.0F, 1.0F, 0.0F);
    }

    public void update() {
        enabled = Wiz.adp().hand.group.stream().filter(Wiz::canBeFused).count() >= 2;
        updateText();
        glow();
        updateHoldProgress();
        if (current_x != target_x) {
            current_x = MathUtils.lerp(current_x, target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(current_x - target_x) < Settings.UI_SNAP_THRESHOLD) {
                current_x = target_x;
            }
        }

        hb.move(current_x, current_y);
        if (enabled) {
            isDisabled = AbstractDungeon.isScreenUp || AbstractDungeon.player.isDraggingCard || AbstractDungeon.player.inSingleTargetMode;

            if (AbstractDungeon.player.hoveredCard == null) {
                hb.update();
            }

            if (!Settings.USE_LONG_PRESS && InputHelper.justClickedLeft && hb.hovered && !isDisabled && !AbstractDungeon.isScreenUp) {
                hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }

            if (hb.hovered && !isDisabled && !AbstractDungeon.isScreenUp && !justClicked) {
                if (hb.justHovered && AbstractDungeon.player.hoveredCard == null) {
                    CardCrawlGame.sound.play("UI_HOVER");

                    for (AbstractCard c : AbstractDungeon.player.hand.group) {
                        if (Wiz.canBeFused(c)) {
                            c.superFlash(Color.GOLD.cpy());
                        }
                    }
                }
            }
        }

        if (holdProgress == HOLD_DUR && !isDisabled && !AbstractDungeon.isScreenUp) {
            trigger();
            holdProgress = 0.0F;
            AbstractDungeon.effectsQueue.add(new EndTurnLongPressBarFlashEffect());
        }

        justClicked = false;
        if ((!Settings.USE_LONG_PRESS) && (hb.clicked && !isDisabled && enabled)) {
            hb.clicked = false;
            if (!AbstractDungeon.isScreenUp) {
                justClicked = true;
                trigger();
            }
        }

        if (Wiz.isInCombat() && !isHidden) {
            if (hoveredCollider == null) {
                for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
                    if (!monster.isDeadOrEscaped() && monster.hb.intersects(hb) && monster.hb.hovered && !hb.hovered) {
                        hoveredCollider = monster;
                        break;
                    }
                }
            } else {
                if (!hoveredCollider.hb.hovered || hb.hovered || hoveredCollider.isDeadOrEscaped()) {
                    hoveredCollider = null;
                }
            }
            if (hoveredCollider != null) {
                fadeTimer += Gdx.graphics.getDeltaTime();
                target_x = SHOW_X + (MathUtils.sin(fadeTimer*MathUtils.PI*4) * 6f * Settings.xScale);
                if (fadeTimer > FADE_TIME) {
                    fadeTimer = FADE_TIME;
                    target_x = HIDE_X;
                }
            } else {
                fadeTimer = 0;
                target_x = SHOW_X; // TODO bugged, didnt appear back in heart fight
            }
        }
    }

    private void updateHoldProgress() {
        if (!Settings.USE_LONG_PRESS && !InputHelper.isMouseDown) {
            holdProgress -= Gdx.graphics.getDeltaTime();
            if (holdProgress < 0.0F) {
                holdProgress = 0.0F;
            }

        } else {
            if ((hb.hovered && InputHelper.isMouseDown) && !isDisabled && enabled) {
                holdProgress += Gdx.graphics.getDeltaTime();
                if (holdProgress > HOLD_DUR) {
                    holdProgress = HOLD_DUR;
                }
            } else {
                holdProgress -= Gdx.graphics.getDeltaTime();
                if (holdProgress < 0.0F) {
                    holdProgress = 0.0F;
                }
            }
        }
    }

    public boolean canAfford() {
        for (AbstractRelic relic : Wiz.adp().relics) {
            if (relic instanceof PearFusilli && !relic.grayscale) {
                return true;
            }
        }
        return EnergyPanel.totalCount >= 1;
    }

    public void enable() {
        enabled = true;
        fusedThisTurn = false;
    }

    public void disable() {
        enabled = false;
        hb.hovered = false;
        isGlowing = false;
        fusedThisTurn = false;
    }

    public void trigger() {
        hb.hovered = false;
        isGlowing = false;
        Wiz.atb(new FusionAction(fusedThisTurn || !canAfford()));
    }

    public void updateText() {
        if (!fusedThisTurn && canAfford()) {
            label = FUSE_TEXT;
        } else if (fusedThisTurn) {
            label = ALREADY_FUSED_TEXT;
        } else {
            label = CANT_AFFORD_TEXT;
        }
    }

    private void glow() {
        if (isGlowing && !isHidden) {
            if (glowTimer < 0.0F) {
                glowList.add(new EndTurnGlowEffect());
                glowTimer = GLOW_INTERVAL;
            } else {
                glowTimer -= Gdx.graphics.getDeltaTime();
            }
        }

        for (EndTurnGlowEffect glow : glowList) {
            glow.update();
        }

        glowList.removeIf(glow -> glow.isDone);
    }

    public void hide() {
        if (!isHidden) {
            target_x = HIDE_X;
            isHidden = true;
        }
    }

    public void show() {
        if (isHidden) {
            target_x = SHOW_X;
            isHidden = false;
            if (isGlowing) {
                glowTimer = -1.0F;
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (!Settings.hideEndTurn) {
            Color origColor = sb.getColor();
            float tmpY = current_y;
            renderHoldEndTurn(sb);
            if (!isDisabled && enabled) {
                if (hb.hovered) {
                    textColor = Color.CYAN;
                } else if (isGlowing) {
                    textColor = Settings.GOLD_COLOR;
                } else {
                    textColor = Settings.CREAM_COLOR;
                }

                if (hb.hovered && !AbstractDungeon.isScreenUp && !Settings.isTouchScreen) {
                    String body = fusedThisTurn ? TIP_TEXT[2] : !canAfford() ? TIP_TEXT[3] : TIP_TEXT[1];
                    float dy = 275f;
                    TipHelper.renderGenericTip(50.0F * Settings.scale, current_y + dy * Settings.scale, TIP_TEXT[0], body);
                }
            } else if (label.equals(ALREADY_FUSED_TEXT)) {
                textColor = Settings.CREAM_COLOR;
            } else {
                textColor = Color.LIGHT_GRAY;
            }

            if (hb.clickStarted && !AbstractDungeon.isScreenUp) {
                tmpY -= 2.0F * Settings.scale;
            } else if (hb.hovered && !AbstractDungeon.isScreenUp) {
                tmpY += 2.0F * Settings.scale;
            }

            if (!enabled) {
                ShaderHelper.setShader(sb, ShaderHelper.Shader.GRAYSCALE);
            }
            if (!isDisabled && (!hb.clickStarted || !hb.hovered) && !fusedThisTurn) {
                sb.setColor(ENABLED_COLOR);
            } else {
                sb.setColor(DISABLED_COLOR);
            }

            Texture buttonImg;
            if (isGlowing && !hb.clickStarted) {
                buttonImg = ImageMaster.END_TURN_BUTTON_GLOW;
            } else {
                buttonImg = ImageMaster.END_TURN_BUTTON;
            }

            if (hb.hovered && !isDisabled && !AbstractDungeon.isScreenUp) {
                sb.draw(ImageMaster.END_TURN_HOVER, current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            }

            sb.draw(buttonImg, current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
            if (!enabled) {
                ShaderHelper.setShader(sb, ShaderHelper.Shader.DEFAULT);
            }

            renderGlowEffect(sb, current_x, current_y);
            if ((hb.hovered || holdProgress > 0.0F) && !isDisabled && !AbstractDungeon.isScreenUp) {
                sb.setBlendFunction(770, 1);
                sb.setColor(Settings.HALF_TRANSPARENT_WHITE_COLOR);
                sb.draw(buttonImg, current_x - 128.0F, tmpY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 256, 256, false, false);
                sb.setBlendFunction(770, 771);
            }

            FontHelper.renderFontCentered(sb, FontHelper.panelEndTurnFont, label, current_x - 0.0F * Settings.scale, tmpY - 3.0F * Settings.scale, textColor);
            sb.setColor(origColor);
            if (!isHidden) {
                hb.render(sb);
            }
        }
    }

    private void renderHoldEndTurn(SpriteBatch sb) {
        if (Settings.USE_LONG_PRESS) {
            holdBarColor.r = 0.0F;
            holdBarColor.g = 0.0F;
            holdBarColor.b = 0.0F;
            holdBarColor.a = holdProgress * 1.5F;
            sb.setColor(holdBarColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, current_x - 107.0F * Settings.scale, current_y + 53.0F * Settings.scale - 7.0F * Settings.scale, 525.0F * Settings.scale * holdProgress + 14.0F * Settings.scale, 20.0F * Settings.scale);
            holdBarColor.r = holdProgress * 2.5F;
            holdBarColor.g = 0.6F + holdProgress;
            holdBarColor.b = 0.6F;
            holdBarColor.a = 1.0F;
            sb.setColor(holdBarColor);
            sb.draw(ImageMaster.WHITE_SQUARE_IMG, current_x - 100.0F * Settings.scale, current_y + 53.0F * Settings.scale, 525.0F * Settings.scale * holdProgress, 6.0F * Settings.scale);
        }
    }

    private void renderGlowEffect(SpriteBatch sb, float x, float y) {
        for (EndTurnGlowEffect e : glowList) {
            e.render(sb, x, y);
        }
    }

    public static FusionButton get() {
        if (AbstractDungeon.overlayMenu != null) {
            return FusionButtonPatches.FusionButtonField.button.get(AbstractDungeon.overlayMenu);
        }
        return null;
    }
}
