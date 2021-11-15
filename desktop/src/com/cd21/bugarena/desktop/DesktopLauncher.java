package com.cd21.bugarena.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cd21.bugarena.GameMain;
import com.cd21.bugarena.helpers.GameInfo;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.width = GameInfo.SCREEN_WIDTH;
		config.height = GameInfo.SCREEN_HEIGHT;

		config.resizable = false;

		new LwjglApplication(new GameMain(), config);
	}
}
