package RangerCaptain.patches;

import RangerCaptain.screens.FusionScreen;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;

public class FusionScreenPatches {
    public static AbstractDungeon.CurrentScreen screenHack(AbstractDungeon.CurrentScreen screen) {
        if (screen == FusionScreen.Enum.FUSION_SCREEN) {
            return AbstractDungeon.CurrentScreen.HAND_SELECT;
        }
        return screen;
    }

    @SpirePatch2(clz = CardGroup.class, method = "glowCheck")
    public static class StopGlowing {
        @SpireInstrumentPatch
        public static ExprEditor plz() {
            return new ExprEditor() {
                @Override
                public void edit(FieldAccess f) throws CannotCompileException {
                    if (f.getClassName().equals(AbstractDungeon.class.getName()) && f.getFieldName().equals("screen")) {
                        f.replace("$_ = "+FusionScreenPatches.class.getName()+".screenHack($proceed($$));");
                    }
                }
            };
        }
    }
}
