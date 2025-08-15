package RangerCaptain.tutorials;

import RangerCaptain.MainModfile;
import RangerCaptain.TheRangerCaptain;
import RangerCaptain.cards.Carniviper;
import RangerCaptain.cards.Elfless;
import RangerCaptain.cards.Nevermort;
import RangerCaptain.util.Wiz;
import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.buttons.GotItButton;
import com.megacrit.cardcrawl.vfx.combat.BattleStartEffect;

import java.io.IOException;

public class FusionTutorial extends FtueTip {
    private static final UIStrings tutorialStrings = CardCrawlGame.languagePack.getUIString(MainModfile.makeID(FusionTutorial.class.getSimpleName()));
    public static final String[] MSG = tutorialStrings.EXTRA_TEXT;
    public static final String[] LABEL = tutorialStrings.TEXT;
    private static final String title1 = LABEL[0];
    private static final String title2 = LABEL[1];
    private static final AbstractCard base = new Elfless();
    private static final AbstractCard donor = new Nevermort();
    private static AbstractCard fusion;
    private static Carniviper card1;
    private static Carniviper card2;
    private static Carniviper card3;
    private static Carniviper card4;
    private static Carniviper card5;
    private final GotItButton button;
    private final int maxSlots = 3;
    private int currentSlot = 0;

    public FusionTutorial() {
        super(title1, MSG[0], Settings.WIDTH / 2.0f - (500.0f * Settings.scale), Settings.HEIGHT / 2.0f, base);
        fusion = Wiz.fuse(base, donor);
        base.current_x = Settings.WIDTH / 2.0f;
        base.current_y = Settings.HEIGHT / 2.0f;
        donor.current_x = Settings.WIDTH / 2.0f + AbstractCard.RAW_W * Settings.xScale;
        donor.current_y = Settings.HEIGHT / 2.0f;
        fusion.current_x = Settings.WIDTH / 2.0f + AbstractCard.RAW_W * Settings.xScale * 2;
        fusion.current_y = Settings.HEIGHT / 2.0f;
        card1 = new Carniviper();
        card2 = new Carniviper();
        card3 = new Carniviper();
        card4 = new Carniviper();
        card5 = new Carniviper();
        card1.current_x = Settings.WIDTH / 2.0f;
        card1.current_y = Settings.HEIGHT / 2.0f;
        card2.current_x = Settings.WIDTH / 2.0f + AbstractCard.RAW_W * Settings.xScale;
        card2.current_y = Settings.HEIGHT / 2.0f;
        card3.current_x = Settings.WIDTH / 2.0f + AbstractCard.RAW_W * Settings.xScale * 2;
        card3.current_y = Settings.HEIGHT / 2.0f;
        card4.current_x = Settings.WIDTH / 2.0f + AbstractCard.RAW_W * Settings.xScale * 2;
        card4.current_y = Settings.HEIGHT / 2.0f;
        card5.current_x = Settings.WIDTH / 2.0f + AbstractCard.RAW_W * Settings.xScale;
        card5.current_y = Settings.HEIGHT / 2.0f;
        card2.upgrade0();
        card2.upgraded = true;
        card3.upgrade0();
        card3.upgrade3();
        card3.upgraded = true;
        card4.upgrade0();
        card4.upgrade2();
        card4.upgraded = true;
        card5.upgrade0();
        card5.upgrade1();
        card5.upgraded = true;
        button = (ReflectionHacks.getPrivateInherited(this, FusionTutorial.class, "button"));
    }

    @Override
    public void update() {
        this.button.update();
        if (button.hb.clicked || CInputActionSet.proceed.isJustPressed()) {
            if (currentSlot < maxSlots) {
                currentSlot++;
                CInputActionSet.proceed.unpress();
                button.hb.clicked = false;
                CardCrawlGame.sound.play("DECK_OPEN");
                ReflectionHacks.setPrivateInherited(this, FusionTutorial.class, "body", MSG[currentSlot]);
                switch (currentSlot) {
                    default:
                        break;
                    case 1:
                        donor.current_x = Settings.WIDTH / 2.0f + AbstractCard.RAW_W * Settings.xScale * 3/4f;
                        break;
                    case 2:
                        ReflectionHacks.setPrivateInherited(this, FusionTutorial.class, "header", title2);
                        ReflectionHacks.setPrivateInherited(this, FusionTutorial.class, "c", card1);
                        break;
                    case 3:
                        card3.current_x = Settings.WIDTH / 2.0f;
                        break;
                }
            } else {
                super.update();
                AbstractDungeon.effectList.clear();
                AbstractDungeon.topLevelEffects.add(new BattleStartEffect(false));
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
        switch (currentSlot) {
            default:
                break;
            case 0:
                donor.render(sb);
                break;
            case 1:
                donor.render(sb);
                fusion.render(sb);
                break;
            case 2:
                card2.render(sb);
                card3.render(sb);
                break;
            case 3:
                card2.render(sb);
                card3.render(sb);
                card4.render(sb);
                card5.render(sb);
                break;
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "preBattlePrep")
    public static class ShowBoosterTutorialPatch {
        public static void Postfix(AbstractPlayer __instance) {
            if (__instance instanceof TheRangerCaptain && MainModfile.playTutorial) {
                if (AbstractDungeon.screen != AbstractDungeon.CurrentScreen.FTUE) {
                    MainModfile.rangerCaptainConfig.setBool(MainModfile.PLAY_TUTORIAL, false);
                    MainModfile.playTutorial = false;
                    MainModfile.playTutorialButton.toggle.enabled = false;
                    try {
                        MainModfile.rangerCaptainConfig.save();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AbstractDungeon.ftue = new FusionTutorial();
                }
            }
        }
    }
}
