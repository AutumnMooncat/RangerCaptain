package RangerCaptain.commands;

import RangerCaptain.util.Wiz;
import basemod.BaseMod;
import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;

import java.util.ArrayList;

public class Fusion extends ConsoleCommand {
    private static final ArrayList<String> monsterIDs = new ArrayList<>();

    public Fusion() {
        requiresPlayer = true;
        minExtraTokens = 2;
        maxExtraTokens = 2;
    }

    @Override
    protected void execute(String[] strings, int depth) {
        if (strings.length > depth + 1) {
            String baseID = unpackCardName(strings[depth]);
            String donorID = unpackCardName(strings[depth + 1]);
            AbstractCard base = CardLibrary.getCard(baseID);
            AbstractCard donor = CardLibrary.getCard(donorID);
            if (base == null) {
                DevConsole.log("could not find card " + baseID);
                return;
            }
            if (donor == null) {
                DevConsole.log("could not find card " + donorID);
                return;
            }
            if (!Wiz.canBeFused(base)) {
                DevConsole.log(baseID + " cannot be fused");
                return;
            }
            if (!Wiz.canBeFused(donor)) {
                DevConsole.log(donorID + " cannot be fused");
                return;
            }
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(Wiz.fuse(base, donor), true));
            DevConsole.log("adding fusion of " + baseID + " and " + donorID);
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        if (monsterIDs.isEmpty()) {
            populateIDs();
        }
        if (tokens.length > depth + 1) {
            if (monsterIDs.contains(tokens[depth]) && monsterIDs.contains(tokens[depth + 1])) {
                complete = true;
            }
        }
        return monsterIDs;
    }

    private void populateIDs() {
        for (String key : CardLibrary.cards.keySet()) {
            if (Wiz.canBeFused(CardLibrary.getCard(key))) {
                monsterIDs.add(key.replace(' ', '_'));
            }
        }
    }

    public static String unpackCardName(String cardName) {
        if (BaseMod.underScoreCardIDs.containsKey(cardName)) {
            cardName = BaseMod.underScoreCardIDs.get(cardName);
        }
        return cardName;
    }

    @Override
    protected void errorMsg() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* [card id] [card id]");
    }
}
