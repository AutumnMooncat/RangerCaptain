package RangerCaptain.cards.abstracts;

import RangerCaptain.MainModfile;
import RangerCaptain.patches.TypeOverridePatch;
import RangerCaptain.util.ImageHelper;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.cards.tempCards.Miracle;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractPowerCard extends AbstractEasyCard {
    public AbstractPower storedPower;

    public AbstractPowerCard(AbstractPower power) {
        super(MainModfile.makeID(power.ID+"Card"), -2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        TypeOverridePatch.TypeOverrideField.typeOverride.set(this, TEXT[2]);
        this.storedPower = power;
        this.name = power.name;
        this.portrait = getPortrait();
        processDescription();
        initializeTitle();
    }

    public void processDescription() {
        //String prefixID = power.ID.split(":")[0];
        StringBuilder sb = new StringBuilder();
        for (String s : storedPower.description.split(" ")) {
            if (s.startsWith("#") && s.length() > 2) {
                switch(s.charAt(1)) {
                    case 'b':
                        s = "[#87CEEB]" + s.substring(2);
                        break;
                    case 'g':
                        s = "[#7FFF00]" + s.substring(2);
                        break;
                    case 'p':
                        s = "[#EE82EE]" + s.substring(2);
                        break;
                    case 'r':
                        s = "[#FF6563]" + s.substring(2);
                        break;
                    case 'y':
                        s = "[#EFC851]" + s.substring(2);
                        break;
                }
            }
            /*if (GameDictionary.keywords.containsKey(prefixID+":"+s)) {
                s = prefixID+":"+s;
            }*/
            sb.append(s).append(" ");
        }
        rawDescription = sb.toString().trim();
        initializeDescription();
    }

    private TextureAtlas.AtlasRegion getPortrait() {
        TextureAtlas.AtlasRegion cardBack = storedPower.type == AbstractPower.PowerType.DEBUFF ? CardLibrary.getCard(VoidCard.ID).portrait : CardLibrary.getCard(Miracle.ID).portrait;
        TextureAtlas.AtlasRegion powerIcon = storedPower.region128;
        powerIcon.flip(false, true);
        cardBack.flip(false, true);
        FrameBuffer fb = ImageHelper.createBuffer(250, 190);
        OrthographicCamera og = new OrthographicCamera(250, 190);
        SpriteBatch sb = new SpriteBatch();
        sb.setProjectionMatrix(og.combined);
        ImageHelper.beginBuffer(fb);
        sb.setColor(Color.WHITE);
        sb.begin();
        sb.draw(cardBack, -125, -95);
        sb.draw(powerIcon, -powerIcon.packedWidth/2F, -powerIcon.packedHeight/2F);
        sb.end();
        fb.end();
        cardBack.flip(false, true);
        powerIcon.flip(false, true);
        TextureRegion a = ImageHelper.getBufferTexture(fb);
        return new TextureAtlas.AtlasRegion(a.getTexture(), 0, 0, 250, 190);
    }

    @Override
    public void upp() {}

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {}
}
