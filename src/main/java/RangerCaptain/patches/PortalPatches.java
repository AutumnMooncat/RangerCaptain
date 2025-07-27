package RangerCaptain.patches;

import RangerCaptain.cards.PortalToAnywhere;
import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.HandCheckAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CtBehavior;

public class PortalPatches {

    @SpirePatch2(clz = UseCardAction.class, method = "update")
    public static class FixUCA {
        @SpireInsertPatch(locator = Locator2.class)
        public static SpireReturn<?> plz(UseCardAction __instance, AbstractCard ___targetCard, float ___duration) {
            if (___targetCard instanceof PortalToAnywhere) {
                PortalToAnywhere card = (PortalToAnywhere) ___targetCard;
                if (card.success) {
                    card.onSuccess.run();
                    card.success = false;

                    ReflectionHacks.setPrivate(__instance, AbstractGameAction.class, "duration", ___duration - Gdx.graphics.getDeltaTime());

                    AbstractDungeon.player.cardInUse = null;
                    ___targetCard.exhaustOnUseOnce = false;
                    ___targetCard.dontTriggerOnUseCard = false;
                    AbstractDungeon.actionManager.addToBottom(new HandCheckAction());

                    return SpireReturn.Return();
                }
            }

            return SpireReturn.Continue();
        }

        private static class Locator2 extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(UseCardAction.class, "reboundCard");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    // Old effect acted as Wing boots
    /*public static boolean flightCheck(boolean hasBoots) {
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
    }*/
}
