package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;

public class Zombie extends Sprite{

	public boolean left;
	public int maxSteps;
	public boolean lS=true;
	
	public Zombie() {
		Random rand = new Random();
		left=rand.nextBoolean();
		maxSteps=0;
	}

	public Zombie(AtlasRegion findRegion) {
		// TODO Auto-generated constructor stub
		super(findRegion);
		Random rand = new Random();
		left=rand.nextBoolean();
		maxSteps=0;
		
	}
	
}
