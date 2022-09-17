package no.sandramoen.commanderqueen.actors.pickups;

import com.badlogic.gdx.math.MathUtils;

import no.sandramoen.commanderqueen.actors.characters.Player;
import no.sandramoen.commanderqueen.utils.BaseGame;
import no.sandramoen.commanderqueen.utils.GameUtils;
import no.sandramoen.commanderqueen.utils.Stage3D;

public class RocketLauncher extends Pickup {
    private float totalTime;

    public RocketLauncher(float y, float z, Stage3D s, int amount, Player player) {
        super(y, z, s, player);
        this.amount = amount;

        buildModel(3f, 1f, .001f, true);
        setPosition(GameUtils.getPositionRelativeToFloor(1f), y, z);
        setBaseRectangle();

        loadImage("pickups/rocketLauncher");
    }

    @Override
    public void act(float dt) {
        super.act(dt);
        totalTime += dt;

        setScale(
                scale.x * MathUtils.cos(2 * totalTime) * .1f + 1,
                scale.y * MathUtils.sin(2 * totalTime) * .1f + 1,
                .001f
        );
    }

    @Override
    public void playSound() {
        super.playSound();
        BaseGame.ammoPickupSound.play(BaseGame.soundVolume);
    }
}
