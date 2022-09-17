package no.sandramoen.commanderqueen.actors.weapon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.commanderqueen.actors.Barrel;
import no.sandramoen.commanderqueen.actors.characters.Menig;
import no.sandramoen.commanderqueen.actors.utils.baseActors.BaseActor;
import no.sandramoen.commanderqueen.actors.utils.baseActors.BaseActor3D;
import no.sandramoen.commanderqueen.utils.BaseGame;
import no.sandramoen.commanderqueen.utils.GameUtils;

public class Crosshair extends BaseActor {
    public Crosshair(Stage stage) {
        super(0, 0, stage);
        loadImage("crosshair");
        centerAtPosition(Gdx.graphics.getWidth() / 2f, Gdx.graphics.getHeight() / 2f);
        stage.addActor(this);
    }

    public void setColorIfShootable(Array<BaseActor3D> shootable, int index) {
        if (index >= 0) {
            if (shootable.get(index) instanceof Menig || shootable.get(index) instanceof Barrel)
                setColor(BaseGame.redColor);
            else
                setColor(Color.WHITE);
        } else
            setColor(Color.WHITE);
    }
}
