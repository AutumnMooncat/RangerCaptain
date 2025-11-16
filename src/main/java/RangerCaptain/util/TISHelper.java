package RangerCaptain.util;

import RangerCaptain.patches.AttackEffectPatches;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireTogether.actions.CustomDamageAction;
import spireTogether.network.P2P.P2PPlayer;
import spireTogether.util.SpireHelp;

public class TISHelper {
    public static void doToxinTeamkill(AbstractCreature owner, int amount) {
        for (P2PPlayer p2p : SpireHelp.Multiplayer.Players.GetPlayers(true, true)) {
            AbstractDungeon.actionManager.addToBottom(new CustomDamageAction(p2p.GetEntity(), new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AttackEffectPatches.RANGER_CAPTAIN_TOXIN));
        }
    }
}
