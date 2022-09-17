package no.sandramoen.commanderqueen.actors.characters;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import no.sandramoen.commanderqueen.actors.Tile;
import no.sandramoen.commanderqueen.actors.characters.enemy.Enemy;
import no.sandramoen.commanderqueen.actors.characters.enemy.Sprite;
import no.sandramoen.commanderqueen.actors.hud.HUD;
import no.sandramoen.commanderqueen.utils.BaseGame;
import no.sandramoen.commanderqueen.utils.GameUtils;
import no.sandramoen.commanderqueen.utils.Stage3D;
import no.sandramoen.commanderqueen.utils.pathFinding.TileGraph;

public class Sersjant extends Enemy {
    private Sound shootSound = BaseGame.shotgunSound;
    private long shootSoundID;

    public Sersjant(float y, float z, Stage3D s, Player player, Float rotation, TileGraph tileGraph, Array<Tile> floorTiles, Stage stage, HUD hud, DecalBatch batch) {
        super(y, z, s, player, rotation, tileGraph, floorTiles, stage, hud, batch);
        movementSpeed = Player.movementSpeed / 140f;
        setHealth(30);
        shootImageDelay = .5f;
        attackStateChangeFrequency = 1.5f * shootImageDelay;
        minDamage = 3;
        maxDamage = 15;
        score = 10;
        painChance = .78f;
        numShots = 3;

        initializeAnimations();
    }

    @Override
    public void die() {
        if (!isDead) {
            GameUtils.playSoundRelativeToDistance(BaseGame.menigDeathSound, distanceBetween(player), VOCAL_RANGE);
            shootSound.stop(shootSoundID);
        }
        super.die();
    }

    @Override
    public void decrementHealth(int amount) {
        if (health - amount > 0 && amount > 0)
            GameUtils.playSoundRelativeToDistance(BaseGame.menigHurtSound, distanceBetween(player), VOCAL_RANGE);
        super.decrementHealth(amount);
    }

    @Override
    protected void shootSound() {
        shootSoundID = GameUtils.playSoundRelativeToDistance(shootSound, distanceBetween(player), VOCAL_RANGE, .6f);
        super.shootSound();
    }

    @Override
    protected void meleeSound() {
        BaseGame.menigMeleeSound.play(BaseGame.soundVolume);
        super.meleeSound();
    }

    @Override
    protected void playActivateSound() {
        GameUtils.playSoundRelativeToDistance(BaseGame.menigActiveSound, distanceBetween(player) * 10, VOCAL_RANGE);
        super.playActivateSound();
    }

    private void initializeAnimations() {
        initializeIdleAnimations();
        initializeWalkingAnimations();
        initializeShootAnimation();
        initializeHurtAnimation();
        initializeMeleeAnimation();
        initializeGibAnimation();
        initializeDeathAnimation();
        setDirectionalAnimation();
    }

    private void initializeIdleAnimations() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/idle front 0"));
        idleFrontAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/idle front side left 0"));
        idleFrontSideLeftAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/idle front side right 0"));
        idleFrontSideRightAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/idle side left 0"));
        idleSideLeftAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/idle side right 0"));
        idleSideRightAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/idle back side left 0"));
        idleBackSideLeftAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/idle back side right 0"));
        idleBackSideRightAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/idle back 0"));
        idleBackAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
    }

    private void initializeWalkingAnimations() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        for (int i = 0; i < 4; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/walk front " + i));
        walkFrontAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        for (int i = 0; i < 4; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/walk front side left " + i));
        walkFrontSideLeftAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        for (int i = 0; i < 4; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/walk front side right " + i));
        walkFrontSideRightAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        for (int i = 0; i < 4; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/walk side left " + i));
        walkSideLeftAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        for (int i = 0; i < 4; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/walk side right " + i));
        walkSideRightAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        for (int i = 0; i < 4; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/walk back side left " + i));
        walkBackSideLeftAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        for (int i = 0; i < 4; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/walk back side right " + i));
        walkBackSideRightAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();

        for (int i = 0; i < 4; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/walk back " + i));
        walkBackAnimation = new Animation(.2f, animationImages, Animation.PlayMode.LOOP);
        animationImages.clear();
    }

    private void initializeShootAnimation() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/shoot 0"));
        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/shoot 1"));
        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/shoot 0"));
        shootAnimation = new Animation(shootImageDelay, animationImages, Animation.PlayMode.NORMAL);
    }

    private void initializeMeleeAnimation() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/melee 0"));
        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/melee 1"));
        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/melee 0"));
        meleeAnimation = new Animation(shootImageDelay, animationImages, Animation.PlayMode.NORMAL);
    }

    private void initializeGibAnimation() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        for (int i = 0; i < 7; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/gib " + i));
        gibAnimation = new Animation(.15f, animationImages, Animation.PlayMode.NORMAL);
    }

    private void initializeHurtAnimation() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/hurt 0"));
        hurtAnimation = new Animation(.25f, animationImages, Animation.PlayMode.NORMAL);
    }

    private void initializeDeathAnimation() {
        Array<TextureAtlas.AtlasRegion> animationImages = new Array();
        for (int i = 0; i < 5; i++)
            animationImages.add(BaseGame.textureAtlas.findRegion("enemies/sersjant/die " + i));
        dieAnimation = new Animation(.25f, animationImages, Animation.PlayMode.NORMAL);
    }
}
