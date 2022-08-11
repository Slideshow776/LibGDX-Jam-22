package no.sandramoen.commanderqueen.actors.pickups;

import com.badlogic.gdx.utils.Array;

import no.sandramoen.commanderqueen.actors.Tile;
import no.sandramoen.commanderqueen.actors.characters.Player;
import no.sandramoen.commanderqueen.utils.GameUtils;
import no.sandramoen.commanderqueen.utils.Stage3D;

public class Bullets extends Pickup {

    public Bullets(float y, float z, Stage3D s, int amount, Player player, Array<Tile> tiles) {
        super(y, z, s, player, tiles);
        this.amount = amount;
        buildModel(.4f, .5f, .001f, true);
        loadImage("pickups/bullet");

        setPosition(GameUtils.getPositionRelativeToFloor(.5f), y, z);
        checkIfIlluminated(tiles);
        setBaseRectangle();
    }
}
