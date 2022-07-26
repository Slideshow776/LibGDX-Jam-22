package no.sandramoen.commanderqueen.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.commanderqueen.actors.Barrel;
import no.sandramoen.commanderqueen.actors.hud.HUD;
import no.sandramoen.commanderqueen.actors.characters.Ghoul;
import no.sandramoen.commanderqueen.actors.characters.Player;
import no.sandramoen.commanderqueen.actors.Tile;
import no.sandramoen.commanderqueen.actors.hud.Weapon;
import no.sandramoen.commanderqueen.actors.pickups.Ammo;
import no.sandramoen.commanderqueen.actors.pickups.Pickup;
import no.sandramoen.commanderqueen.actors.utils.BaseActor3D;
import no.sandramoen.commanderqueen.actors.utils.Enemy;
import no.sandramoen.commanderqueen.utils.MapLoader;
import no.sandramoen.commanderqueen.actors.utils.TilemapActor;
import no.sandramoen.commanderqueen.utils.BaseGame;
import no.sandramoen.commanderqueen.utils.BaseScreen3D;
import no.sandramoen.commanderqueen.utils.GameUtils;

public class LevelScreen extends BaseScreen3D {
    private Player player;
    private Weapon weapon;
    private HUD hud;
    private Array<Tile> tiles;
    private Array<Enemy> enemies;
    private Array<Pickup> pickups;
    private Array<BaseActor3D> shootable;

    private Label debugLabel;
    private Label gameLabel;
    private Label statusLabel;

    private boolean isGameOver = false;
    private TilemapActor tilemap;

    private MapLoader mapLoader;

    @Override
    public void initialize() {
        /*GameUtils.playLoopingMusic(BaseGame.level0Music);*/
        GameUtils.playLoopingMusic(BaseGame.metalWalkingMusic, 0);
        initializeMap();
        initializeActors();
        initializeUI();
    }

    @Override
    public void update(float dt) {
        if (isGameOver) return;
        if (hud.getHealth() == 0) {
            setGameOver();
            return;
        }
        updateTiles();
        updateEnemies();
        updatePickups();
        updateWeapon();

        debugLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond() + "\nVisible: " + mainStage3D.visibleCount);

        if (!Gdx.input.isCursorCatched())
            Gdx.input.setCursorCatched(true);

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE || keycode == Keys.Q)
            Gdx.app.exit();
        if (keycode == Keys.R)
            BaseGame.setActiveScreen(new LevelScreen());
        if (keycode == Keys.NUM_1)
            hud.decrementHealth(10);
        if (keycode == Keys.NUM_2)
            hud.incrementHealth(10);
        if (keycode == Keys.NUM_3)
            hud.setInvulnerable();
        return super.keyDown(keycode);
    }

    @Override
    public void dispose() {
        mainStage3D.dispose();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT && !isGameOver)
            shoot();
        return super.touchDown(screenX, screenY, pointer, button);
    }

    private void determineConsequencesOfPick(int index) {
        if (index >= 0) {
            if (shootable.get(index).getClass().getSimpleName().equals("Ghoul")) {
                Ghoul ghoul = (Ghoul) shootable.get(index);
                removeEnemy(ghoul);
                hud.incrementScore(10, false);
                hud.setKillFace();
                activateEnemies(15, ghoul);
            } else if (shootable.get(index).getClass().getSimpleName().equals("Barrel")) {
                Barrel barrel = (Barrel) shootable.get(index);
                checkExplosionRange(barrel);
                barrel.explode();
                shootable.removeValue(barrel, false);
                activateEnemies(45, barrel);
            }
        }
    }

    private void setCrosshairColorIfEnemy(int index) {
        if (index >= 0) {
            if (shootable.get(index).getClass().getSimpleName().equals("Ghoul") || shootable.get(index).getClass().getSimpleName().equals("Barrel"))
                weapon.crosshair.setColor(BaseGame.redColor);
            else
                weapon.crosshair.setColor(Color.WHITE);
        }
    }

    private void updateTiles() {
        for (Tile tile : tiles) {
            if (tile.type == "walls" && player.overlaps(tile)) {
                player.preventOverlap(tile);
            }
        }
    }

    private void updateEnemies() {
        for (int i = 0; i < enemies.size; i++) {
            if (player.overlaps(enemies.get(i))) {
                player.preventOverlap(enemies.get(i));
                if (enemies.get(i).getClass().getSimpleName().equals("Ghoul")) {
                    Ghoul ghoul = (Ghoul) enemies.get(i);
                    if (ghoul.isReadyToAttack()) {
                        hud.decrementHealth(10);
                        GameUtils.playSoundRelativeToDistance(BaseGame.ghoulDeathSound, ghoul.distanceBetween(player), ghoul.VOCAL_RANGE, 1.5f);
                    }
                }

                for (int j = 0; j < enemies.size; j++) {
                    if (enemies.get(i) != enemies.get(j))
                        enemies.get(i).preventOverlap(enemies.get(j));
                }
            }

            for (Tile tile : tiles) {
                if (tile.type == "walls" && enemies.get(i).overlaps(tile))
                    enemies.get(i).preventOverlap(tile);

                if (enemies.get(i).overlaps(tile) && tile.type == "floors" && tile.illuminated)
                    enemies.get(i).setColor(Color.WHITE);
                else if (enemies.get(i).overlaps(tile) && tile.type == "floors")
                    enemies.get(i).setColor(enemies.get(i).originalColor);
            }
        }
    }

    private void updatePickups() {
        for (Pickup pickup : pickups) {
            if (player.overlaps(pickup)) {
                if (pickup.getClass().getSimpleName().equals("Ammo"))
                    hud.incrementAmmo(1);
                if (pickup.getClass().getSimpleName().equals("Armor"))
                    hud.incrementArmor(25);
                if (pickup.getClass().getSimpleName().equals("Health"))
                    hud.incrementHealth(10);
                pickups.removeValue(pickup, false);
                pickup.remove();
            }
        }
    }

    private void updateWeapon() {
        weapon.sway(player.isMoving);
        setCrosshairColorIfEnemy(GameUtils.getRayPickedListIndex(
                Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2,
                shootable,
                mainStage3D.camera
        ));
    }

    private void setGameOver() {
        if (!isGameOver) {
            mainStage3D.camera.position.x = -Tile.height * .48f;
            hud.setDeadFace();
            BaseGame.metalWalkingMusic.stop();
            weapon.moveDown();
            gameLabel.setText("G A M E   O V E R !");
            player.isPause = true;
            for (Enemy enemy : enemies)
                enemy.isPause = true;
            isGameOver = true;
        }
    }

    private void checkExplosionRange(final BaseActor3D source) {
        if (player.isWithinDistance(3f, source))
            hud.decrementHealth(100);
        else if (player.isWithinDistance(7f, source))
            hud.decrementHealth(50);
        else if (player.isWithinDistance(10f, source))
            hud.decrementHealth(25);
        else
            hud.setKillFace();

        if (player.isWithinDistance(10f, source))
            player.forceMoveAwayFrom(source);

        for (Enemy enemy : enemies) {
            if (enemy.isWithinDistance(3f, source))
                enemy.decrementHealth(100);
            else if (enemy.isWithinDistance(7f, source))
                enemy.decrementHealth(50);
            else if (enemy.isWithinDistance(10f, source))
                enemy.decrementHealth(25);

            if (enemy.isWithinDistance(10f, source))
                enemy.forceMoveAwayFrom(source);
        }

        for (Enemy enemy : enemies)
            if (enemy.isDead)
                removeEnemy(enemy);
    }

    private void removeEnemy(Enemy enemy) {
        enemy.die();
        pickups.add(new Ammo(enemy.position.y, enemy.position.z, mainStage3D, player));
        enemies.removeValue(enemy, false);
        shootable.removeValue(enemy, false);
        statusLabel.setText("enemies left: " + enemies.size);
    }

    private void shoot() {
        player.shoot();
        weapon.shoot();
        hud.decrementAmmo();
        int index = GameUtils.getRayPickedListIndex(
                Gdx.graphics.getWidth() / 2,
                Gdx.graphics.getHeight() / 2,
                shootable,
                mainStage3D.camera
        );
        determineConsequencesOfPick(index);
        activateEnemies(45, player);
    }

    private void activateEnemies(float range, BaseActor3D baseActor3D) {
        for (Enemy enemy : enemies)
            if (enemy.isWithinDistance(range, baseActor3D))
                enemy.activate();
    }

    private void initializeMap() {
        pickups = new Array();
        shootable = new Array();
        tiles = new Array();
        enemies = new Array();
        tilemap = new TilemapActor(BaseGame.level0Map, mainStage3D);
        mapLoader = new MapLoader(tilemap, tiles, mainStage3D, player, shootable, pickups, enemies);
    }

    private void initializeActors() {
        player = mapLoader.player;
        hud = new HUD(uiStage);
        weapon = new Weapon(uiStage);
    }

    private void initializeUI() {
        statusLabel = new Label("enemies left: " + enemies.size, BaseGame.label26Style);
        uiTable.add(statusLabel)
                .row();

        debugLabel = new Label(" ", BaseGame.label26Style);
        uiTable.add(debugLabel)
                .expandX()
                .top()
                .left()
                .padTop(Gdx.graphics.getHeight() * .01f)
                .padLeft(Gdx.graphics.getWidth() * .01f)
                .row();


        gameLabel = new Label("", BaseGame.label26Style);
        gameLabel.setColor(Color.RED);
        gameLabel.setFontScale(2f);
        uiTable.add(gameLabel)
                .expand()
                .center()
                .colspan(2);

        /*uiTable.setDebug(true);*/
    }
}
