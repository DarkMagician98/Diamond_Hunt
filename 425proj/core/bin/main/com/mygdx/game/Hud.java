package com.mygdx.game;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;


public class Hud {

    private Stage stage;
    private FitViewport stageViewport;
    private Table table;
    private Label label;
    private int diamondTouch;

    public Hud(SpriteBatch spriteBatch, int dCount) {
    	
   
        stageViewport = new FitViewport(500,500);
        stage = new Stage(stageViewport, spriteBatch); //create stage with the stageViewport and the SpriteBatch given in Constructor
        
        Label.LabelStyle label1Style = new Label.LabelStyle();
        BitmapFont myFont = new BitmapFont();
        label1Style.font = myFont;
        label1Style.fontColor = Color.RED;
        
        diamondTouch = dCount;
        
        label = new Label("Diamond(s) Left: " + diamondTouch,label1Style);
        label.setColor(0, 0, 0, 0.6f);
        label.setSize(Gdx.graphics.getWidth(),50);
        
        //label.setScale(20, 20);
        
        table = new Table();
        //table.setFillParent(true);
        table.setPosition(60, DiamondHunt.HEIGHT/2-10);
        //label.setPosition(Gdx.graphics.getWidth() - 20,Align.topLeft);
        
        table.add(label);
        stage.addActor(table);
    }
    
    public void updateScore(int upd) {
    	diamondTouch = upd;
    	
    	if(diamondTouch == 0) {
    		label.setColor(1, 0, 0, 1.0f);
    		label.setText("YOU WON!");
    		label.setPosition(150,-240);
    	}
    	else if(DiamondHunt.zombieTouch == 1) {
    	    label.setColor(1, 0, 0, 1.0f);
    		label.setText("YOU LOST");
    		label.setPosition(150,-240);
    	}
    	else {
    		label.setText("Diamond(s) Left: " + diamondTouch);
    	}
    	
    	stage.act();
    	stage.draw();
    }

    public Stage getStage() { return stage; }

    public void dispose(){
        stage.dispose();
    }
}