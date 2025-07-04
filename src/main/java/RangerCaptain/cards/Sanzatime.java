package RangerCaptain.cards;

import RangerCaptain.actions.DoAction;
import RangerCaptain.cardmods.fusion.FusionModHelper;
import RangerCaptain.cardmods.fusion.mods.IncreaseDebuffsMod;
import RangerCaptain.cards.abstracts.AbstractEasyCard;
import RangerCaptain.util.CardArtRoller;
import RangerCaptain.util.MonsterEnum;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static RangerCaptain.MainModfile.makeID;

public class Sanzatime extends AbstractEasyCard {
    public final static String ID = makeID(Sanzatime.class.getSimpleName());

    static {
        new FusionModHelper(MonsterEnum.SANZATIME)
                .with(new IncreaseDebuffsMod(1))
                .register();
        new FusionModHelper(MonsterEnum.FORTIWINX)
                .with(new IncreaseDebuffsMod(1))
                .register();
    }

    public Sanzatime() {
        super(ID, 1, CardType.ATTACK, CardRarity.COMMON, CardTarget.ENEMY);
        baseDamage = damage = 5;
        baseMagicNumber = magicNumber = 1;
        setMonsterData(MonsterEnum.SANZATIME);
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        dmg(m, AbstractGameAction.AttackEffect.BLUNT_LIGHT);
        if (m != null) {
            addToBot(new DoAction(() -> {
                for (AbstractPower pow : m.powers) {
                    if (pow.type == AbstractPower.PowerType.DEBUFF && !(pow instanceof NonStackablePower)) {
                        if (pow.amount > 0) {
                            pow.stackPower(magicNumber);
                        } else if (pow.canGoNegative) {
                            pow.stackPower(-magicNumber);
                        }
                        pow.updateDescription();
                    }
                }
            }));
        }

    }

    @Override
    public void upp() {
        upgradeDamage(1);
        upgradeMagicNumber(1);
        name = originalName = cardStrings.EXTENDED_DESCRIPTION[0];
        initializeTitle();
        setMonsterData(MonsterEnum.FORTIWINX);
    }

    @Override
    public String cardArtCopy() {
        return Miracle.ID;
    }

    @Override
    public CardArtRoller.ReskinInfo reskinInfo(String ID) {
        return new CardArtRoller.ReskinInfo(ID, darken(GOLD), WHITE, darken(GOLD), WHITE, false);
    }
}