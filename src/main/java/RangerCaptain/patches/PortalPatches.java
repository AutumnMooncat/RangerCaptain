package RangerCaptain.patches;

import RangerCaptain.cards.PortalToAnywhere;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.WingBoots;
import com.megacrit.cardcrawl.screens.DungeonMapScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

public class PortalPatches {

    public static boolean flightCheck(boolean hasBoots) {
        return hasBoots || AbstractDungeon.player.masterDeck.group.stream().anyMatch(card -> card instanceof PortalToAnywhere);
    }

    @SpirePatch2(clz = DungeonMapScreen.class, method = "updateControllerInput")
    public static class CountPortalInMap {
        @SpireInstrumentPatch
        public static ExprEditor plz() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("hasRelic")) {
                        m.replace("$_ = "+PortalPatches.class.getName()+".flightCheck($proceed($$));");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = MapRoomNode.class, method = "wingedIsConnectedTo")
    public static class CountPortalInNode {
        @SpireInstrumentPatch
        public static ExprEditor plz() {
            return new ExprEditor() {
                @Override
                public void edit(MethodCall m) throws CannotCompileException {
                    if (m.getClassName().equals(ModHelper.class.getName()) && m.getMethodName().equals("isModEnabled")) {
                        m.replace("$_ = "+PortalPatches.class.getName()+".flightCheck($proceed($$));");
                    }
                }
            };
        }
    }

    @SpirePatch2(clz = MapRoomNode.class, method = "update")
    public static class DoPortalEffects {
        @SpireInsertPatch(locator = Locator.class, localvars = {"normalConnection", "wingedConnection"})
        public static void plz(boolean normalConnection, boolean wingedConnection) {
            if (normalConnection || !wingedConnection) {
                return;
            }
            AbstractRelic boots = AbstractDungeon.player.getRelic(WingBoots.ID);
            if (boots != null && boots.counter > 0) {
                return;
            }
            for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                if (card instanceof PortalToAnywhere) {
                    ((PortalToAnywhere) card).onTrigger();
                    break;
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctBehavior) throws Exception {
                Matcher m = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctBehavior, m);
            }
        }
    }
}
