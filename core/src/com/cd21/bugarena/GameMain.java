package com.cd21.bugarena;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cd21.bugarena.screens.GameScreen;


public class GameMain extends Game {

	private SpriteBatch spriteBatch;

	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}

	@Override
	public void create () {
		spriteBatch = new SpriteBatch();

		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {
		getScreen().dispose();
		spriteBatch.dispose();
	}
}
