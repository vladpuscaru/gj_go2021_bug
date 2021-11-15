package com.cd21.bugarena.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.cd21.bugarena.helpers.GameInfo;
import com.cd21.bugarena.player.Player;

public class InputManager {

    /* External references */
    private Player player;
    private OrthographicCamera camera;

    public InputManager(Player player, OrthographicCamera camera) {
        this.player = player;
        this.camera = camera;
    }

    public void handleInput(float deltaTime) {
        // Player keys
        if (Gdx.input.isKeyPressed(Input.Keys.W))
            player.setVelocity(new Vector2(player.getVelocity().x, GameInfo.PLAYER_SPEED * deltaTime));

        if (Gdx.input.isKeyPressed(Input.Keys.A))
            player.setVelocity(new Vector2(-GameInfo.PLAYER_SPEED * deltaTime, player.getVelocity().y));

        if (Gdx.input.isKeyPressed(Input.Keys.S))
            player.setVelocity(new Vector2(player.getVelocity().x, -GameInfo.PLAYER_SPEED * deltaTime));

        if (Gdx.input.isKeyPressed(Input.Keys.D))
            player.setVelocity(new Vector2(GameInfo.PLAYER_SPEED * deltaTime, player.getVelocity().y));

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE) && !player.isDashing())
            player.setDashing(true);

        // Player rotation
        Vector3 vec = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        vec = camera.unproject(vec);

        float mouseX = vec.x;
        float mouseY = vec.y;

        float pX = player.getX() + player.getWidth() / 2;
        float pY = player.getY() + player.getHeight() / 2;

        float angle = MathUtils.atan2(mouseY - pY, mouseX - pX);
        angle = angle * (180 / MathUtils.PI) + 90;

        if (angle < 0) {
            angle = 360 - (-angle);
        }

        player.setRotationAngle(angle);
    }
}
