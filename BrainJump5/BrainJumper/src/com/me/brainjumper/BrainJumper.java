package com.me.brainjumper;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.me.brainjumper.screen.Splash;
import com.me.brainjumper.set.Assets;
import com.me.brainjumper.set.Settings;


public class BrainJumper extends Game {
	boolean firstTimeCreate = true;
	FPSLogger fps;

	public void create () {
		Settings.load();
		Assets.load();
		//Assets.setBackGround(1);
		setScreen(new  Splash(this));
		fps = new FPSLogger();
	}
	
	@Override
	public void render() {
		super.render();
		fps.log();
	}

	/** {@link Game#dispose()} only calls {@link Screen#hide()} so you need to override {@link Game#dispose()} in order to call
	 * {@link Screen#dispose()} on each of your screens which still need to dispose of their resources. SuperJumper doesn't
	 * actually have such resources so this is only to complete the example. */
	@Override
	public void dispose () {
		super.dispose();

		getScreen().dispose();
	}
}
