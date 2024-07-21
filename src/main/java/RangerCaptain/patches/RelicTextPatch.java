package RangerCaptain.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.screens.charSelect.CharacterOption;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class RelicTextPatch {
    public static BitmapFont outlinedFont;

    @SpirePatch2(clz = CharacterOption.class, method = "renderRelics")
    public static class OutlineText {
        @SpireInstrumentPatch
        public static ExprEditor plz() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().equals(FontHelper.class.getName()) && f.getFieldName().equals("tipBodyFont")) {
                        f.replace("if (this.charInfo.player instanceof Rabbit.TheRabbit) {$_="+RelicTextPatch.class.getName()+".outlinedFont;} else {$_=$proceed();}");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = FontHelper.class, method = "initialize")
    public static class MakeNewFont {
        @SpireInsertPatch(locator = Locator.class)
        public static void plz(FreeTypeFontGenerator.FreeTypeFontParameter ___param) {
            //Body logic
            ___param.borderColor = Color.DARK_GRAY;
            ___param.borderWidth = Settings.scale;
            outlinedFont = FontHelper.prepFont(22.0F, false);
            ___param.borderWidth = 0;
            ___param.borderColor = new Color(0.4F, 0.1F, 0.1F, 1.0F);
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(FontHelper.class, "prepFont");
                return new int[] {LineFinder.findAllInOrder(ctBehavior, m)[9]};
            }
        }
    }
}
