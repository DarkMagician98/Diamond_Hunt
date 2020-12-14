package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

public class Player {

	private int movementSpeed;
	
	public int xPosition, yPosition;
	private int width, height;
	
	private Texture playerTexture;
	
	
	
	public Player(int xpos, int ypos, int movementSpeed, int width,int height, Texture playerTexture) {
		this.xPosition = xpos;
		this.yPosition = ypos;
		this.movementSpeed = movementSpeed;
		this.playerTexture = playerTexture;
		this.width = width;
		this.height = height;
	}
	
	public int getX() { return xPosition;}
	public int getY() { return yPosition;}
	
	public void update(float delta) {
		
	}
	
	public void draw(Batch batch) {
		
		batch.draw(playerTexture, xPosition, yPosition,width,height);
	}
	
}
