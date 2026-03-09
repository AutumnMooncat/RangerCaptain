package RangerCaptain.patches;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.interfaces.PostRenderSubscriber;
import basemod.interfaces.PostUpdateSubscriber;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

import java.util.ArrayList;

@SpireInitializer
public class ClickableTipsPatch implements PostUpdateSubscriber, PostRenderSubscriber {
    public static final ArrayList<TipClickable> clickables = new ArrayList<>();
    public static final String TEST = "[ClickMe]";
    public static final String TEST2 = "[ClickMe2]";
    public static final String TEST3 = "[ClickMe3]";

    @Override
    public void receivePostUpdate() {
        int mx = InputHelper.mX;
        int my = InputHelper.mY;
        if (CardCrawlGame.isPopupOpen) {
            InputHelper.mX = CardCrawlGame.popupMX;
            InputHelper.mY = CardCrawlGame.popupMY;
        }
        clickables.forEach(TipClickable::update);
        InputHelper.mX = mx;
        InputHelper.mY = my;
    }

    @Override
    public void receivePostRender(SpriteBatch sb) {
        clickables.forEach(t -> t.render(sb));
    }

    public ClickableTipsPatch() {
        BaseMod.subscribe(this);
    }

    public static void initialize() {
        new ClickableTipsPatch();
        clickables.add(new TipClickable(TEST, "#yClick #yHere #yTo #yPreview #yFusions") {
            @Override
            public void onclick() {
                SCVPatches.shouldOpen = true;
            }
        });
        clickables.add(new TipClickable(TEST2, "#yClick #yHere #yFor #ySound") {
            @Override
            public void onclick() {
                CardCrawlGame.sound.play("ATTACK_POISON");
            }
        });
        clickables.add(new TipClickable(TEST3, "#b(1/3) - #yClick #yHere #yTo #yCycle NL NL This is my very complicated tooltip.") {
            int index = 0;
            String[] text = new String[] {
                    "#b(1/3) - #yClick #yHere #yTo #yCycle NL NL This is my very complicated tooltip.",
                    "#b(2/3) - #yClick #yHere #yTo #yCycle NL NL It takes several pages to explain how it works. This is a good idea yesyes.",
                    "#b(3/3) - #yClick #yHere #yTo #yCycle NL NL Surely no one will use this power for evil."
            };
            @Override
            public void onclick() {
                index++;
                index %= 3;
                replacement = text[index];
            }
        });
    }

    public static String doReplace(String s) {
        if (s != null) {
            for (TipClickable clickable : clickables) {
                s = s.replace(clickable.key, clickable.replacement);
            }
        }
        return s;
    }

    public static abstract class TipClickable {
        public String key;
        public String replacement;
        public Hitbox hb;
        public boolean active;

        public TipClickable(String key, String replacement) {
            this.key = key;
            this.replacement = replacement;
            this.hb = new Hitbox(0, 0);
        }

        public void setHeight(float h) {
            hb.width = 320.0F * Settings.scale;
            hb.height = h + (3 * 32.0F * Settings.scale);
        }

        public void move(float x, float y) {
            hb.translate(x, y - hb.height + 32f * Settings.scale);
        }

        public void update() {
            if (active) {
                hb.update();
                if (hb.hovered && InputHelper.justClickedLeft) {
                    hb.clickStarted = true;
                } else if (hb.clicked) {
                    hb.clicked = false;
                    onclick();
                }
            } else {
                hb.hovered = false;
                hb.clicked = false;
                hb.clickStarted = false;
            }
            active = false;
        }

        public void render(SpriteBatch sb) {
            if (active) {
                hb.render(sb);
            }
        }

        public abstract void onclick();
    }

    @SpirePatch2(clz = TipHelper.class, method = "renderTipBox")
    public static class DoStuff {
        @SpirePrefixPatch
        public static void plz(float x, float y, SpriteBatch sb, String title, @ByRef String[] description) {
            float height = ReflectionHacks.getPrivateStatic(TipHelper.class, "textHeight");
            for (TipClickable clickable : clickables) {
                if (description[0] != null && description[0].contains(clickable.key)) {
                    description[0] = description[0].replace(clickable.key, clickable.replacement);
                    clickable.active = true;
                    clickable.setHeight(height);
                    clickable.move(x, y);
                }
            }
        }
    }

    @SpirePatch2(clz = TipHelper.class, method = "renderPowerTips")
    public static class ClickMeOwO {
        @SpirePrefixPatch
        public static void plz(float x, float y, SpriteBatch sb, ArrayList<PowerTip> powerTips) {

        }
    }

    @SpirePatch2(clz = TipHelper.class, method = "getPowerTipHeight")
    public static class HeightCheck {
        @SpireInstrumentPatch
        public static ExprEditor plz() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(FontHelper.class.getName()) && m.getMethodName().equals("getSmartHeight")) {
                        m.replace(String.format("$2 = %s.doReplace($2); $_ = $proceed($$);", ClickableTipsPatch.class.getName()));
                    }
                }
            };
        }
    }
}
