package com.kohmeow.game;


import com.badlogic.gdx.Game;
// import com.badlogic.gdx.graphics.g2d.BitmapFont; 
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.kohmeow.game.screen.ManuScreen;


public class KohMeowGame extends Game {


    public static final float UNIT_SCALE = 1/32f;

    public SpriteBatch batch;
    //So, sprite batching is just any system that lets you draw multiple sprites
    // at once and hopefully gain some efficiency from it



    // AssetManager is Class to help
	@Override
	public void create () {
        batch = new SpriteBatch();
        
        
        System.out.println("GameScreen");
        System.out.println("Loading Assets");
        System.out.println("-------------------------------------");
      
        setScreen(new ManuScreen(this));
	}

    @Override
    public void render(){
        super.render();
    }


	

}
