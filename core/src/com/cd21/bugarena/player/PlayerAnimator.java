package com.cd21.bugarena.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

public class PlayerAnimator {

    /* Own objects */
    private final Texture sheet;
    private final static int WIDTH = 64;
    private final static int HEIGHT = 64;

    private final Animation<TextureRegion> idleAnimation;
    private final Animation<TextureRegion> walkAnimation;
    private final Animation<TextureRegion> dashAnimation;

    private Animation<TextureRegion> previousAnimation;
    private Animation<TextureRegion> currentAnimation;

    private float stateTime;

    /* External references */
    private Player player;

    public PlayerAnimator(Player player) {
        this.player = player;
        stateTime = 0.0f;
        sheet = new Texture(Gdx.files.internal("player/player_spritesheet_2.png"));

        Array<TextureRegion> frames = new Array<>();

        frames.add(new TextureRegion(sheet, 0 * WIDTH, 0, WIDTH, HEIGHT));
        idleAnimation = new Animation<TextureRegion>(0.15f, frames, Animation.PlayMode.LOOP);

        frames.clear();
        frames.add(new TextureRegion(sheet, 1 * WIDTH, 0, WIDTH, HEIGHT));
        frames.add(new TextureRegion(sheet, 2 * WIDTH, 0, WIDTH, HEIGHT));
        frames.add(new TextureRegion(sheet, 3 * WIDTH, 0, WIDTH, HEIGHT));
        walkAnimation = new Animation<TextureRegion>(0.15f, frames, Animation.PlayMode.LOOP);

        frames.clear();
        frames.add(new TextureRegion(sheet, 4 * WIDTH, 0, WIDTH, HEIGHT));
        frames.add(new TextureRegion(sheet, 5 * WIDTH, 0, WIDTH, HEIGHT));
        frames.add(new TextureRegion(sheet, 6 * WIDTH, 0, WIDTH, HEIGHT));
        frames.add(new TextureRegion(sheet, 7 * WIDTH, 0, WIDTH, HEIGHT));
        frames.add(new TextureRegion(sheet, 8 * WIDTH, 0, WIDTH, HEIGHT));
        frames.add(new TextureRegion(sheet, 9 * WIDTH, 0, WIDTH, HEIGHT));
        dashAnimation = new Animation<TextureRegion>(0.15f, frames, Animation.PlayMode.NORMAL);

        previousAnimation = currentAnimation = idleAnimation;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;

        Animation<TextureRegion> animation;
        switch (player.getState()) {
            case MOVING:
                animation = walkAnimation;
                break;
            case DASHING:
                if (player.isDashing() && currentAnimation == dashAnimation) {
                    if (currentAnimation.isAnimationFinished(stateTime)) {
                        animation = previousAnimation;
                        player.setDashing(false);
                    } else {
                        animation = currentAnimation;
                    }
                } else {
                    animation = dashAnimation;
                }
                break;
            case IDLE:
            default:
                animation = idleAnimation;
        }

        if (currentAnimation != animation) {
            previousAnimation = currentAnimation;
            currentAnimation = animation;
            stateTime = 0.0f;
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentAnimation.getKeyFrame(stateTime);
    }

    public void dispose() {
        sheet.dispose();
    }
}
