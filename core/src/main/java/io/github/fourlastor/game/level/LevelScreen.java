package io.github.fourlastor.game.level;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;
import io.github.fourlastor.game.component.ActorComponent.Layer;
import io.github.fourlastor.game.level.platform.PlatformFactory;
import io.github.fourlastor.game.route.Router;

public class LevelScreen extends ScreenAdapter {

    private final Engine engine;
    private final Viewport viewport;

    @AssistedInject
    public LevelScreen(
            @Assisted Router router,
            Engine engine,
            Viewport viewport,
            EntitiesFactory entitiesFactory,
            PlatformFactory platformFactory) {
        this.engine = engine;
        this.viewport = viewport;
        engine.addEntity(entitiesFactory.parallaxBackground(0.125f, Layer.BG_PARALLAX, 0));
        engine.addEntity(entitiesFactory.parallaxBackground(0.25f, Layer.BG_PARALLAX, 1));
        engine.addEntity(entitiesFactory.parallaxBackground(0.5f, Layer.FG_PARALLAX, 2));
        engine.addEntity(entitiesFactory.parallaxBackground(1f, Layer.FG_PARALLAX, 3));
        for (int i = 0; i < 6; i++) {
            engine.addEntity(entitiesFactory.makePlatform(platformFactory.nextPlatform()));
        }
        engine.addEntity(entitiesFactory.player());
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void render(float delta) {
        engine.update(delta);
    }

    @AssistedFactory
    public interface Factory {
        LevelScreen create(Router router);
    }
}
