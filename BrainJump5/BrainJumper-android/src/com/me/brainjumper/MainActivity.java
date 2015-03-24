package com.me.brainjumper;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;


public class MainActivity extends AndroidApplication {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      /*  AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        initialize(new SuperJumper(), false);*/
        initialize(new BrainJumper(), false);
    }
}