package com.cd21.bugarena.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.cd21.bugarena.helpers.B2DCategoryBits;
import com.cd21.bugarena.helpers.GameInfo;

public class Player extends Sprite {

    /* Own objects */
    private Body body;
    private float health;
    private float attack;
    private Vector2 velocity;
    private float rotationAngle;
    private boolean isDashing;

    private PlayerState state;
    private PlayerAnimator animator;


    /* External references */
    private World world;

    public Player(World world, float x, float y) {
        this.world = world;

        health = 0.0f;
        attack = 5.0f;
        velocity = new Vector2(0.0f, 0.0f);
        rotationAngle = 0.0f;
        isDashing = false;
        state = PlayerState.IDLE;
        animator = new PlayerAnimator(this);

        setPosition(x, y);
        setBounds(0, 0, 64 / GameInfo.PPM, 64 / GameInfo.PPM); // width and height
        defineBody();
    }

    public void update(float deltaTime) {
        // Decide on state
        if (isDashing) {
            state = PlayerState.DASHING;
        } else if (!velocity.isZero()) {
            state = PlayerState.MOVING;
        } else {
            state = PlayerState.IDLE;
        }

        animator.update(deltaTime);

        // Set drawable
        setRegion(animator.getCurrentFrame());

        // Move
        if (isDashing) {
            float dashXVel = velocity.x != 0 ? (velocity.x < 0 ? velocity.x - GameInfo.PLAYER_DASH_SPEED_ADDITION : velocity.x + GameInfo.PLAYER_DASH_SPEED_ADDITION) : 0;
            float dashYVel = velocity.y != 0 ? (velocity.y < 0 ? velocity.y - GameInfo.PLAYER_DASH_SPEED_ADDITION : velocity.y + GameInfo.PLAYER_DASH_SPEED_ADDITION) : 0;
            body.setLinearVelocity(dashXVel, dashYVel);
        } else {
            body.setLinearVelocity(velocity);
        }

        // Rotate
        setOriginCenter();
        setRotation(rotationAngle);

        // Decrease & clamp velocity
        if (velocity.x != 0) {
            float newXVel = velocity.x < 0 ? velocity.x + GameInfo.PLAYER_SPEED_DECREASE : velocity.x - GameInfo.PLAYER_SPEED_DECREASE;
            velocity.x = velocity.x < 0 ? MathUtils.clamp(newXVel, -GameInfo.PLAYER_SPEED, 0) : MathUtils.clamp(newXVel, 0, GameInfo.PLAYER_SPEED);
        }

        if (velocity.y != 0) {
            float newYVel = velocity.y < 0 ? velocity.y + GameInfo.PLAYER_SPEED_DECREASE : velocity.y - GameInfo.PLAYER_SPEED_DECREASE;
            velocity.y = velocity.y < 0 ? MathUtils.clamp(newYVel, -GameInfo.PLAYER_SPEED, 0) : MathUtils.clamp(newYVel, 0, GameInfo.PLAYER_SPEED);
        }

        // Sync sprite with body
        setPosition(body.getPosition().x - getWidth() / 2f, body.getPosition().y - getHeight() / 2f);
    }

    public boolean isDashing() {
        return isDashing;
    }

    public void setDashing(boolean dashing) {
        isDashing = dashing;
        velocity.x = 0;
        velocity.y = 0;
    }

    public void setRotationAngle(float rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    public Vector2 getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity.set(velocity);
    }

    public PlayerState getState() {
        return state;
    }

    private void defineBody() {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(getX() + getWidth() / 2f, getY() + getHeight() / 2f);

        body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(getWidth() / 2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.isSensor = false;
        fixtureDef.filter.categoryBits = B2DCategoryBits.PLAYER_BIT;
        fixtureDef.filter.maskBits = B2DCategoryBits.DEFAULT_BIT | B2DCategoryBits.ENEMY_BIT;

        body.createFixture(fixtureDef).setUserData(this);
    }

    public void dispose() {
        getTexture().dispose();
        animator.dispose();
    }
}


















