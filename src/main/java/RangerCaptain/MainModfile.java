package RangerCaptain;

import RangerCaptain.cards.Binvader;
import RangerCaptain.cards.cardvars.*;
import RangerCaptain.cards.interfaces.GlowAdjacentCard;
import RangerCaptain.cards.interfaces.OnOtherCardUpgradedCard;
import RangerCaptain.cards.interfaces.OnReceiveDebuffCard;
import RangerCaptain.patches.GlowChangePatch;
import RangerCaptain.powers.*;
import RangerCaptain.relics.AbstractEasyRelic;
import RangerCaptain.screens.FusionScreen;
import RangerCaptain.ui.StashedCardManager;
import RangerCaptain.util.*;
import RangerCaptain.vfx.ShaderTest;
import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.ModLabeledToggleButton;
import basemod.ModPanel;
import basemod.helpers.CardBorderGlowManager;
import basemod.helpers.RelicType;
import basemod.helpers.ScreenPostProcessorManager;
import basemod.interfaces.*;
import basemod.patches.com.megacrit.cardcrawl.cards.AbstractCard.DynamicTextBlocks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.mod.stslib.patches.cardInterfaces.MultiUpgradePatches;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class MainModfile implements
        EditCardsSubscriber,
        EditRelicsSubscriber,
        EditStringsSubscriber,
        EditKeywordsSubscriber,
        EditCharactersSubscriber,
        PostInitializeSubscriber,
        PostUpdateSubscriber,
        AddAudioSubscriber,
        PostPowerApplySubscriber, PostRenderSubscriber, StartGameSubscriber{

    public static final String modID = "RangerCaptain";
    public static final Logger logger = LogManager.getLogger(MainModfile.class.getName());

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public static final Color HEADBAND_PURPLE_COLOR = CardHelper.getColor(117, 41, 219); //7529db

    public static final String SHOULDER1 = modID + "Resources/images/char/mainChar/shoulder2.png";
    public static final String SHOULDER2 = modID + "Resources/images/char/mainChar/shoulder.png";
    public static final String CORPSE = modID + "Resources/images/char/mainChar/corpse.png";
    public static final String ATTACK_S_ART = modID + "Resources/images/512/attack_tree3.png";
    public static final String SKILL_S_ART = modID + "Resources/images/512/skill_tree3.png";
    public static final String POWER_S_ART = modID + "Resources/images/512/power_tree3.png";
    public static final String ATTACK_L_ART = modID + "Resources/images/1024/attack_tree3.png";
    public static final String SKILL_L_ART = modID + "Resources/images/1024/skill_tree3.png";
    public static final String POWER_L_ART = modID + "Resources/images/1024/power_tree3.png";
    private static final String CARD_ENERGY_S = modID + "Resources/images/512/energy.png";
    private static final String TEXT_ENERGY = modID + "Resources/images/512/energy_text.png";
    private static final String CARD_ENERGY_L = modID + "Resources/images/1024/energy_large.png";
    private static final String CHARSELECT_BUTTON = modID + "Resources/images/charSelect/charButton.png";
    private static final String CHARSELECT_PORTRAIT = modID + "Resources/images/charSelect/charBG2.png";

    public static final String BADGE_IMAGE = modID + "Resources/images/Badge.png";

    public static UIStrings uiStrings;
    public static String[] TEXT;
    public static String[] EXTRA_TEXT;
    private static final String AUTHOR = "Mistress Autumn";

    // Mod-settings settings. This is if you want an on/off savable button
    public static SpireConfig rangerCaptainConfig;
    public static ModLabeledToggleButton playTutorialButton;

    public static final String SHOW_DESCRIPTORS = "showDescriptors";
    public static boolean showDescriptors = true;

    public static final String PLAY_TUTORIAL = "playTutorial";
    public static boolean playTutorial = true;

    public static final ArrayList<AbstractGameEffect> safeEffectQueue = new ArrayList<>();

    public static final HashMap<String, Float> sfxCooldowns = new HashMap<>();

    public MainModfile() {
        BaseMod.subscribe(this);

        BaseMod.addColor(TheRangerCaptain.Enums.HEADBAND_PURPLE_COLOR, HEADBAND_PURPLE_COLOR, HEADBAND_PURPLE_COLOR, HEADBAND_PURPLE_COLOR,
                HEADBAND_PURPLE_COLOR, HEADBAND_PURPLE_COLOR, HEADBAND_PURPLE_COLOR, HEADBAND_PURPLE_COLOR,
                ATTACK_S_ART, SKILL_S_ART, POWER_S_ART, CARD_ENERGY_S,
                ATTACK_L_ART, SKILL_L_ART, POWER_L_ART,
                CARD_ENERGY_L, TEXT_ENERGY);

        Properties defaultSettings = new Properties();
        defaultSettings.setProperty(SHOW_DESCRIPTORS, Boolean.toString(showDescriptors));
        defaultSettings.setProperty(PLAY_TUTORIAL, Boolean.toString(playTutorial));
        try {
            rangerCaptainConfig = new SpireConfig(modID, modID+"Config", defaultSettings);
            showDescriptors = rangerCaptainConfig.getBool(SHOW_DESCRIPTORS);
            playTutorial = rangerCaptainConfig.getBool(PLAY_TUTORIAL);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static void initialize() {
        MainModfile thismod = new MainModfile();
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new TheRangerCaptain(TheRangerCaptain.characterStrings.NAMES[1], TheRangerCaptain.Enums.THE_RANGER_CAPTAIN),
                CHARSELECT_BUTTON, CHARSELECT_PORTRAIT, TheRangerCaptain.Enums.THE_RANGER_CAPTAIN);
        PotionLoader.loadContent();
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID)
                .packageFilter(AbstractEasyRelic.class)
                .any(AbstractEasyRelic.class, (info, relic) -> {
                    if (relic.color == null) {
                        BaseMod.addRelic(relic, RelicType.SHARED);
                    } else {
                        BaseMod.addRelicToCustomPool(relic, relic.color);
                    }
                    if (!info.seen) {
                        UnlockTracker.markRelicAsSeen(relic.relicId);
                    }
                });
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addDynamicVariable(new Info());
        BaseMod.addDynamicVariable(new SecondMagicNumber());
        BaseMod.addDynamicVariable(new ThirdMagicNumber());
        BaseMod.addDynamicVariable(new SecondDamage());
        BaseMod.addDynamicVariable(new SecondBlock());
        BaseMod.addDynamicVariable(new DynvarInterfaceManager());

        new AutoAdd(modID)
                .packageFilter(modID+".cards")
                .setDefaultSeen(true)
                .cards();
    }

    private void loadLangKeywords(String language) {
        Gson gson = new Gson();
        Keyword[] keywords = null;
        try {
            String json = Gdx.files.internal(modID + "Resources/localization/eng/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
            keywords = gson.fromJson(json, Keyword[].class);
        } catch (GdxRuntimeException e) {
            if (!e.getMessage().startsWith("File not found:")) {
                throw e;
            }
        }

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID.toLowerCase(), keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
                if(keyword.NAMES.length > 0 && !keyword.ID.isEmpty()) {
                    KeywordManager.setupKeyword(keyword.ID, keyword.NAMES[0]);
                }
            }
        }
    }

    @Override
    public void receiveEditKeywords() {
        String language = Settings.language.name().toLowerCase();
        loadLangKeywords("eng");
        if (!language.equals("eng")) {
            loadLangKeywords(language);
        }
    }

    private void loadLangStrings(String language) {
        String path = modID+"Resources/localization/" + language + "/";

        tryLoadStringsFile(CardStrings.class,path + "Cardstrings.json");
        tryLoadStringsFile(RelicStrings.class, path + "Relicstrings.json");
        tryLoadStringsFile(CharacterStrings.class, path + "Charstrings.json");
        tryLoadStringsFile(PowerStrings.class, path + "Powerstrings.json");
        tryLoadStringsFile(UIStrings.class, path + "CardModstrings.json");
        tryLoadStringsFile(UIStrings.class, path + "Componentstrings.json");
        tryLoadStringsFile(CardStrings.class, path + "Chatterstrings.json");
        tryLoadStringsFile(UIStrings.class, path + "UIstrings.json");
        tryLoadStringsFile(PotionStrings.class, path +"Potionstrings.json");
        tryLoadStringsFile(UIStrings.class, path + "CardAugmentstrings.json");
    }

    private void tryLoadStringsFile(Class<?> stringType, String filepath) {
        try {
            BaseMod.loadCustomStringsFile(stringType, filepath);
        } catch (GdxRuntimeException e) {
            // Ignore file not found
            if (!e.getMessage().startsWith("File not found:")) {
                throw e;
            }
        }
    }

    @Override
    public void receiveEditStrings() {
        String language = Settings.language.name().toLowerCase();
        loadLangStrings("eng");
        if (!language.equals("eng")) {
            loadLangStrings(language);
        }
    }

    @Override
    public void receivePostInitialize() {
        uiStrings = CardCrawlGame.languagePack.getUIString(makeID("ModConfigs"));
        EXTRA_TEXT = uiStrings.EXTRA_TEXT;
        TEXT = uiStrings.TEXT;
        // Create the Mod Menu
        ModPanel settingsPanel = new ModPanel();
        float currentYposition = 740f;
        float spacingY = 55f;

        // Load the Mod Badge
        Texture badgeTexture = TexLoader.getTexture(BADGE_IMAGE);
        BaseMod.registerModBadge(badgeTexture, EXTRA_TEXT[0], AUTHOR, EXTRA_TEXT[1], settingsPanel);

        //Add Powers
        BaseMod.addPower(ToxinPower.class, ToxinPower.POWER_ID);
        BaseMod.addPower(ConductivePower.class, ConductivePower.POWER_ID);
        BaseMod.addPower(BurnedPower.class, BurnedPower.POWER_ID);
        BaseMod.addPower(ResonancePower.class, ResonancePower.POWER_ID);
        BaseMod.addPower(TapeJamPower.class, TapeJamPower.POWER_ID);
        BaseMod.addPower(MultitargetPower.class, MultitargetPower.POWER_ID);
        BaseMod.addPower(BoobyTrappedPower.class, BoobyTrappedPower.POWER_ID);

        //Wide Potions
        if (Loader.isModLoaded("widepotions")) {
            WidePotionLoader.loadCrossoverContent();
        }

        //Chimera Cards
        if (Loader.isModLoaded("CardAugments")) {
            ChimeraHelper.registerAugments();
            ChimeraHelper.applyBans();
        }

        //Add Config stuff
        playTutorialButton = new ModLabeledToggleButton(TEXT[0],350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont,
                rangerCaptainConfig.getBool(PLAY_TUTORIAL), settingsPanel, (label) -> {}, (button) -> {
            rangerCaptainConfig.setBool(PLAY_TUTORIAL, button.enabled);
            playTutorial = button.enabled;
            try {rangerCaptainConfig.save();} catch (IOException e) {e.printStackTrace();}
        });
        currentYposition -= spacingY;

        ModLabeledToggleButton useDescriptorsButton = new ModLabeledToggleButton(TEXT[1],350.0f, currentYposition, Settings.CREAM_COLOR, FontHelper.charDescFont,
                rangerCaptainConfig.getBool(SHOW_DESCRIPTORS), settingsPanel, (label) -> {}, (button) -> {
            rangerCaptainConfig.setBool(SHOW_DESCRIPTORS, button.enabled);
            showDescriptors = button.enabled;
            try {rangerCaptainConfig.save();} catch (IOException e) {e.printStackTrace();}
        });
        currentYposition -= spacingY;

        settingsPanel.addUIElement(playTutorialButton);
        settingsPanel.addUIElement(useDescriptorsButton);

        //Other Setup stuff
        BaseMod.addCustomScreen(new FusionScreen());

        DynamicTextBlocks.registerCustomCheck(makeID("Binvader"), c -> {
            if (Wiz.isInCombat() && Wiz.adp().hand.contains(c)) {
                if (Binvader.binvasionCount() == 1) {
                    return 1;
                }
                return 2;
            }
            return 0;
        });

        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
            private final Color c = Color.RED.cpy();
            @Override
            public boolean test(AbstractCard card) {
                if (Wiz.adp().hoveredCard instanceof GlowAdjacentCard && ((GlowAdjacentCard) Wiz.adp().hoveredCard).glowAdjacent(card)) {
                    if (GlowChangePatch.GlowCheckField.lastChecked.get(card) != Wiz.adp().hoveredCard) {
                        GlowChangePatch.GlowCheckField.lastChecked.set(card, Wiz.adp().hoveredCard);
                        if (((GlowAdjacentCard) Wiz.adp().hoveredCard).flashAdjacent(card)) {
                            card.superFlash(((GlowAdjacentCard) Wiz.adp().hoveredCard).getGlowColor(card));
                        }
                    }
                    return true;
                }
                GlowChangePatch.GlowCheckField.lastChecked.set(card, Wiz.adp().hoveredCard);
                return false;
            }

            @Override
            public Color getColor(AbstractCard card) {
                if (Wiz.adp().hoveredCard instanceof GlowAdjacentCard) {
                    return ((GlowAdjacentCard) Wiz.adp().hoveredCard).getGlowColor(card);
                }
                return c;
            }

            @Override
            public String glowID() {
                return makeID("AdjacentGlow");
            }
        });

        CardBorderGlowManager.addGlowInfo(new CardBorderGlowManager.GlowInfo() {
            private final Color c = Color.RED.cpy();

            @Override
            public boolean test(AbstractCard abstractCard) {
                return MultiUpgradePatches.MultiUpgradeFields.glowRed.get(abstractCard);
            }

            @Override
            public Color getColor(AbstractCard abstractCard) {
                return c;
            }

            @Override
            public String glowID() {
                return makeID("ExclusionGlow");
            }
        });

        if (ShaderTest.shaderTest) {
            ScreenPostProcessorManager.addPostProcessor(new ShaderTest());
        }

        if (TSCNFrameDataProcessor.SHOULD_PROCESS) {
            TSCNFrameDataProcessor.process();
        }
        if (FusionGifCreator.SHOULD_PROCESS) {
            FusionGifCreator.process();
        }
        if (TSCNFusionDataProcessor.SHOULD_PROCESS) {
            TSCNFusionDataProcessor.process();
        }
        if (TRESPaletteDataProcessor.SHOULD_PROCESS) {
            TRESPaletteDataProcessor.process();
        }
        FusionNodeData loadPlz = new FusionNodeData();
    }

    public static Color getRainbowColor() {
        return new Color(
                (MathUtils.cosDeg((float)(System.currentTimeMillis() / 10L % 360L)) + 1.25F) / 2.3F,
                (MathUtils.cosDeg((float)((System.currentTimeMillis() + 1000L) / 10L % 360L)) + 1.25F) / 2.3F,
                (MathUtils.cosDeg((float)((System.currentTimeMillis() + 2000L) / 10L % 360L)) + 1.25F) / 2.3F,
                1.0f);
    }

    public static float time = 0f;
    @Override
    public void receivePostUpdate() {
        time += Gdx.graphics.getRawDeltaTime();
        for (AbstractGameEffect effect : safeEffectQueue) {
            effect.update();
        }
        safeEffectQueue.removeIf(effect -> effect.isDone);
        sfxCooldowns.replaceAll((s, v) -> Math.max(0, v - Gdx.graphics.getRawDeltaTime()));
    }

    @Override
    public void receivePostRender(SpriteBatch sb) {
        for (AbstractGameEffect effect : safeEffectQueue) {
            effect.render(sb);
        }
        if (FusionController.RENDER_TEST) {
            FusionController.renderTest(sb);
        }
    }

    @Override
    public void receiveAddAudio() {
        BaseMod.addAudio(CustomSounds.ITEM_GET_KEY, CustomSounds.ITEM_GET_PATH);
    }

    public static void onUpgradeTrigger(AbstractCard card, boolean permanent) {
        if (permanent) {
            for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
                if (c instanceof OnOtherCardUpgradedCard) {
                    ((OnOtherCardUpgradedCard) c).onOtherCardUpgraded(card);
                }
            }
        } else {
            for (AbstractCard c : Wiz.getAllCardsInCardGroups(true, true)) {
                if (c instanceof OnOtherCardUpgradedCard) {
                    ((OnOtherCardUpgradedCard) c).onOtherCardUpgraded(card);
                }
            }
        }
    }

    @Override
    public void receivePostPowerApplySubscriber(AbstractPower power, AbstractCreature target, AbstractCreature source) {
        if (target == Wiz.adp() && power.type == AbstractPower.PowerType.DEBUFF) {
            for (AbstractCard card : Wiz.getAllCardsInCardGroups(true, true)) {
                if (card instanceof OnReceiveDebuffCard) {
                    ((OnReceiveDebuffCard) card).onDebuffed();
                }
            }
        }
    }

    @Override
    public void receiveStartGame() {
        StashedCardManager.EmptyCards.yeet();
    }
}
