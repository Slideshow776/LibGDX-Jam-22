package no.sandramoen.commanderqueen.screens.shell;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.github.tommyettinger.textra.TypingLabel;

import no.sandramoen.commanderqueen.screens.gameplay.LevelScreen;
import no.sandramoen.commanderqueen.utils.BaseGame;
import no.sandramoen.commanderqueen.utils.BaseScreen;
import no.sandramoen.commanderqueen.utils.GameUtils;

public class MenuScreen extends BaseScreen {
    @Override
    public void initialize() {
        TypingLabel titleLabel = new TypingLabel("Terfenstein 3D", new Label.LabelStyle(BaseGame.mySkin.get("arcade64", BitmapFont.class), null));
        uiTable.add(titleLabel)
                .padBottom(Gdx.graphics.getHeight() * .09f)
                .row();

        uiTable.defaults().width(Gdx.graphics.getWidth() * .125f).height(Gdx.graphics.getHeight() * .075f).spaceTop(Gdx.graphics.getHeight() * .01f);

        TextButton startButton = new TextButton("Start", BaseGame.mySkin);
        startButton.addListener(
                (Event event) -> {
                    if (GameUtils.isTouchDownEvent(event))
                        BaseGame.setActiveScreen(new LevelScreen());
                    return false;
                }
        );
        uiTable.add(startButton).row();

        TextButton optionsButton = new TextButton("Options", BaseGame.mySkin);
        optionsButton.addListener(
                (Event event) -> {
                    if (GameUtils.isTouchDownEvent(event))
                        BaseGame.setActiveScreen(new OptionsScreen());
                    return false;
                }
        );
        uiTable.add(optionsButton).row();

        TextButton exitButton = new TextButton("Exit", BaseGame.mySkin);
        exitButton.addListener(
                (Event event) -> {
                    if (GameUtils.isTouchDownEvent(event))
                        Gdx.app.exit();
                    return false;
                }
        );
        uiTable.add(exitButton);
    }

    @Override
    public void update(float delta) {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.Q)
            Gdx.app.exit();
        return super.keyDown(keycode);
    }
}
