package com.mygdx.game;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DiamondHunt extends ApplicationAdapter implements InputProcessor {
	
	private static final float PLAYER_STEP = 1;
	//SPRITES
	SpriteBatch batch;
	Sprite sprite;
	
	//TEXTURE
	Texture img;
	
	//ATTRIBUTES
	public static int HEIGHT = 1000;
 	public static int WIDTH = 1000;
	public static int GAME_WORLD_HEIGHT = 100;
	public static  int GAME_WORLD_WIDTH  = 100;
    int tile_width = 2;
    int tile_height = 2;
	int aspectRatio = HEIGHT/WIDTH; //relative to the width.
	public static int diamondTouch = 0;

	//MAP
	PerlinNoise perlin;
	Pixmap pmap;
	private OrthogonalTiledMapRenderer renderer;
	
	//CAMERA
	Viewport viewport;
	OrthographicCamera camera;
	
	//TiledMapBench tiled;
	TiledMap map = new TiledMap();
	MapLayers layers = map.getLayers();
	
    BitmapFont font;
    
    Player player;

	public TextureAtlas zAtlas;
	public TextureAtlas atlas;
	public Sprite mc;
	public TextureRegion mcStand;
	public TextureRegion mcWalk1;
	public TextureRegion mcWalk2;//right step left step
	public TextureRegion mcRights;
	public TextureRegion mcRight1;
	public TextureRegion mcRight2;
	public TextureRegion mcLefts;
	public TextureRegion mcLeft1;
	public TextureRegion mcLeft2;
	public TextureRegion mcUps;
	public TextureRegion mcUp1;
	public TextureRegion mcUp2;
	public boolean left=true;

	public TextureRegion zStand;
	public TextureRegion zWalk1;
	public TextureRegion zWalk2;//right step left step
	public TextureRegion zRights;
	public TextureRegion zRight1;
	public TextureRegion zRight2;
	public TextureRegion zLefts;
	public TextureRegion zLeft1;
	public TextureRegion zLeft2;
	public TextureRegion zUps;
	public TextureRegion zUp1;
	public TextureRegion zUp2;
	private Music music_level1;
	private Music success;
	private Music hurt;

	Sprite [] gems;
	Texture gT;
	public Zombie [] zombs = new Zombie [80];
	Hud diamondLeftHUD;
	
	Texture tiles;
	
	BitmapFont win,lose;
	static int zombieTouch;

	@Override
	public void create () {

		zombieTouch = 0;
		
		music_level1 = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
		music_level1.setLooping(true);
		music_level1.setVolume((float) 0.25);
		music_level1.play();
		success = Gdx.audio.newMusic(Gdx.files.internal("success.mp3"));
		hurt = Gdx.audio.newMusic(Gdx.files.internal("hurt.mp3"));
		
		
		pmap = PerlinNoise.generatePixmap(WIDTH/tile_width,HEIGHT/tile_height, 0, 254, 4);
		
		
		
		sprite = new Sprite(new Texture(pmap));
		sprite.setPosition(0, 0);
		sprite.setSize(GAME_WORLD_WIDTH, GAME_WORLD_HEIGHT);

		batch = new SpriteBatch();
		
		//CAMERA
		camera = new OrthographicCamera(GAME_WORLD_WIDTH,GAME_WORLD_HEIGHT*aspectRatio);
		viewport = new StretchViewport(GAME_WORLD_WIDTH,GAME_WORLD_HEIGHT*aspectRatio,camera);
		viewport.apply();
		camera.position.set(GAME_WORLD_WIDTH/2,GAME_WORLD_HEIGHT/2,0);
		
		Gdx.input.setInputProcessor(this);
		
		 atlas=new TextureAtlas("MC.pack");
		 zAtlas = new TextureAtlas("skeleton.pack");
		 gT=new Texture(Gdx.files.internal("gem_01_blue.png"));
		
		 Random rand = new Random();
		 
		 //gems = new Sprite[rand.nextInt(8)+1];
		 gems = new Sprite[3];
		 
		 for(int i=0;i<gems.length;i++) {
			 int x = rand.nextInt(GAME_WORLD_WIDTH-10);
			 x=x+5;
			 int y = rand.nextInt(GAME_WORLD_HEIGHT-10);
			 y=y+5;
			 gems[i]=new Sprite(gT);
			 gems[i].setBounds(0, 0, 2, 2);
			 gems[i].setPosition(x, y);
		 }


		
		 for(int i =0;i<zombs.length;i++) {
			 
			 zombs[i] = new Zombie(zAtlas.findRegion("skeleton"));
			 
			 zStand= new TextureRegion(zombs[i].getTexture(),181,480,156,210);
			 zWalk1 = new TextureRegion(zombs[i].getTexture(),0,480,156,210);//leftfoot
//			 zWalk2 = new TextureRegion(zombs[i].getTexture(),370,480,156,210);
			 
			 zRights = new TextureRegion(zombs[i].getTexture(),199,253,156,210);
			 zRight1 = new TextureRegion(zombs[i].getTexture(),34,261,156,210);
			 zRight2 = new TextureRegion(zombs[i].getTexture(),370,262,156,210);
			 
//			 zUps = new TextureRegion(zombs[i].getTexture(),0,64,16,32);
//			 zUp1 = new TextureRegion(zombs[i].getTexture(),17,64,16,32);
//			 zUp2 = new TextureRegion(zombs[i].getTexture(),49,64,16,32);
			 
			 zLefts = new TextureRegion(zombs[i].getTexture(),199,708,156,210);
			 zLeft1 = new TextureRegion(zombs[i].getTexture(),15,708,156,210);
			 zLeft2 = new TextureRegion(zombs[i].getTexture(),349,708,156,210);
			 
			 zombs[i].setBounds(0, 0, 1, 2);
			 zombs[i].setRegion(zLeft2);
			 int x = rand.nextInt(GAME_WORLD_WIDTH-10);
			 x=x+5;
			 int y = rand.nextInt(GAME_WORLD_HEIGHT-10);
			 y=y+5;
			 
			 zombs[i].setPosition(x, y);
			 
			 
			 for(int j =0;j<gems.length;j++) {
					while(gems[j].getBoundingRectangle().overlaps(zombs[i].getBoundingRectangle())) {
						
						x = rand.nextInt(GAME_WORLD_WIDTH-10);
						 x=x+5;
						 y = rand.nextInt(GAME_WORLD_HEIGHT-10);
						 y=y+5;
						 
						 zombs[i].setPosition(x, y);
						
					}
				}
			
			 
		 }




		 mc = new Sprite(atlas.findRegion("character"));          		 
		 
		 
		 
		 mcStand= new TextureRegion(mc.getTexture(),0,0,16,32);
		 mc.setBounds(0, 0, 1, 2);
		 mcWalk1 = new TextureRegion(mc.getTexture(),17,0,16,32);
		 mcWalk2 = new TextureRegion(mc.getTexture(),49,0,16,32);
		 
		 mcRights = new TextureRegion(mc.getTexture(),0,32,16,32);
		 mcRight1 = new TextureRegion(mc.getTexture(),17,32,16,32);
		 mcRight2 = new TextureRegion(mc.getTexture(),49,32,16,32);
		 
		 mcUps = new TextureRegion(mc.getTexture(),0,64,16,32);
		 mcUp1 = new TextureRegion(mc.getTexture(),17,64,16,32);
		 mcUp2 = new TextureRegion(mc.getTexture(),49,64,16,32);
		 
		 mcLefts = new TextureRegion(mc.getTexture(),0,96,16,32);
		 mcLeft1 = new TextureRegion(mc.getTexture(),17,96,16,32);
		 mcLeft2 = new TextureRegion(mc.getTexture(),49,96,16,32);
		 
		 mc.setRegion(mcStand);
		 mc.setPosition(GAME_WORLD_WIDTH/2, GAME_WORLD_HEIGHT/2);
		  
		 diamondTouch = gems.length;
		 diamondLeftHUD = new Hud(batch,gems.length);
		 
			TiledMap map = new TiledMap();
			MapLayers layers = map.getLayers();

		//	Cell cell = new Cell();

			/*for(int i=0;i<WIDTH;i+=tile_width) {
				TiledMapTileLayer layer1 = new TiledMapTileLayer(WIDTH, HEIGHT, tile_width, tile_height);
				for(int j =0;j<HEIGHT;j+=tile_height) {
					//cell.setTile(new StaticTiledMapTile(mc));
					Cell cell = new Cell();
					cell.setTile(new StaticTiledMapTile(zWalk1));
					layer1.setCell(i, j, cell);
					//layer1.setCell(i, j, cell);	
				}
				layers.add(layer1);		
			}*/
			
			/*tiles = new Texture(Gdx.files.internal("tiles.jpg"));
			TextureRegion[][] splitTiles = TextureRegion.split(tiles, 4, 4);
			float [][] generateNoise = PerlinNoise.generatePerlinNoise(250, 250, 4);
			map = new TiledMap();
		    layers = map.getLayers();

				TiledMapTileLayer layer = new TiledMapTileLayer(WIDTH,HEIGHT, tile_width, tile_height);
				for (int x = 0; x < layer.getHeight(); x++) { 
					for (int y = 0; y < layer.getWidth(); y++) {
						int ty = (int)(Math.random() * splitTiles.length);
						int tx = (int)(Math.random() * splitTiles[ty].length);
						Cell cell = new Cell();
						cell.setTile(new StaticTiledMapTile(splitTiles[tx][ty]));
						layer.setCell(x, y, cell);
					}
				}
				
				//System.out.println(layer.getOffsetX() + " | " + layer.getWidth());
				 
				
				layers.add(layer);*/
			
			/*cell.setTile(new StaticTiledMapTile(mc));					
			layer1.setCell(0,5, cell);	
			layers.add(layer1);*/	
			//MapProperties mp = map.getProperties();
			
		    //renderer = new OrthogonalTiledMapRenderer(map);
			font = new BitmapFont();
			font.getData().setScale(.25f);
	}
	

	
	@Override
	public void resize(int width,int height) {
		viewport.update(width, height);
		camera.viewportWidth = GAME_WORLD_WIDTH/4;
		camera.viewportHeight = GAME_WORLD_HEIGHT/4;
	}

	
	@Override
	public void render () {
		Gdx.gl.glClearColor(0,0,0, .5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

	    camera.position.set(mc.getX(),mc.getY(),0);			
	    camera.update();
		

		if(diamondTouch == 0) {
			
			batch.begin();
			batch.setProjectionMatrix(camera.combined);		
			batch.end();
			
			diamondLeftHUD.updateScore(diamondTouch);
	
			Timer.schedule(new Task(){
			    @Override
			    public void run() {
			    	System.exit(0);
			        // Do your work
			    }
			}, 5);
			
		}
		else if(zombieTouch == 1) {

			batch.begin();
			batch.setProjectionMatrix(camera.combined);		
			//font.draw(batch, "YOU LOST!", 40, 50, camera.viewportWidth/3, Align.left, false);
			batch.end();
			
			diamondLeftHUD.updateScore(diamondTouch);
			diamondLeftHUD.getStage().act();
			diamondLeftHUD.getStage().draw();
			
			Timer.schedule(new Task(){
			    @Override
			    public void run() {
			    	System.exit(0);
			        // Do your work
			    }
			}, 5);
		}
		else {
		
		for(int i =0;i<gems.length;i++) {
			if(gems[i].getBoundingRectangle().overlaps(mc.getBoundingRectangle())) {
				diamondTouch--;
				diamondLeftHUD.updateScore(diamondTouch);
				gems[i].translate(9999, 9999);
				success.setLooping(false);
				success.play();

			}
		}

		for(int i =0;i<zombs.length;i++) {
			if(zombs[i].getBoundingRectangle().overlaps(mc.getBoundingRectangle())) {
				//gameoverlol
				zombieTouch = 1;
				hurt.play();
			}
		}
	
		//renderer.setView(camera.combined, 0, 0, WIDTH, HEIGHT);
		//renderer.render();
		batch.begin();
		batch.setProjectionMatrix(camera.combined);		
		sprite.draw(batch);	
		//player.draw(batch);
		for(Sprite g : gems) {
			g.draw(batch);
		}
		mc.draw(batch);
		for(Zombie z : zombs) {
			z.draw(batch);
		}

		batch.end();
		
		diamondLeftHUD.getStage().act();
		diamondLeftHUD.getStage().draw();
		
		}
       // spriteBatch.setProjectionMatrix(hud.getStage().getCamera().combined); //set the spriteBatch to draw what our stageViewport sees
	   // hud.getStage().act(Gdx.graphics.getDeltaTime()); //act the Hud
	   // hud.getStage().draw(); //draw the Hud
	
		//font.draw(batch, "SCORE : 20", 0, HEIGHT+50);
	}
	
	@Override
	public void dispose () {
		sprite.getTexture().dispose();
		batch.dispose();
		//img.dispose();
	}

	@Override
	public boolean keyDown(int keycode) {
		float x = mc.getX();
		float y = mc.getY();
		if(keycode == Keys.LEFT){
		//	viewport.setScreenPosition(-1, 0);
			//player.xPosition -= 3;
			if(x-PLAYER_STEP >= 0)
			mc.setPosition(x-PLAYER_STEP, y);
			
			if(left==true){
				mc.setRegion(mcLeft1);
				left=false;
			}
			else {
				mc.setRegion(mcLeft2);
				left=true;
			}

			//render();
			
		}
		if(keycode == Keys.RIGHT){
			//player.xPosition += 3;
			if(x+PLAYER_STEP < GAME_WORLD_WIDTH)
			mc.setPosition(x+PLAYER_STEP, y);
			if(left==true){
				mc.setRegion(mcRight1);
				left=false;
			}
			else {
				mc.setRegion(mcRight2);
				left=true;
			}

			//render();
		}
		if(keycode == Keys.UP){
			//player.yPosition += 3;
			if(y+PLAYER_STEP < GAME_WORLD_HEIGHT)
			mc.setPosition(x, y+PLAYER_STEP);
			if(left==true){
				mc.setRegion(mcUp1);
				left=false;
			}
			else {
				mc.setRegion(mcUp2);
				left=true;
			}

		//render();
		}
		if(keycode == Keys.DOWN){
			
			if(y-PLAYER_STEP >= 0)
			mc.setPosition(x, y-PLAYER_STEP);
			//player.yPosition -= 3;
			if(left==true){
				mc.setRegion(mcWalk1);
				left=false;
			}
			else {
				mc.setRegion(mcWalk2);
				left =true;
			}
		//render();
		}
		
		

		for(Zombie z : zombs) {
			if(z.lS==true) {
			z.lS=false;
			if(z.left==true) {
				z.setPosition(z.getX()-1, z.getY());
				z.maxSteps++;
				if(z.maxSteps==5) {
					z.maxSteps=0;
					z.left = false;
					z.setRegion(zRight1);
				}
				
			}
			else {
				z.setPosition(z.getX()+1, z.getY());
				z.maxSteps++;
				if(z.maxSteps==5) {
					z.maxSteps=0;
					z.left = true;
					z.setRegion(zLeft1);
				}
				
			}
				
			}
			else {
				z.lS=true;
				if(z.left==true) {
					z.setPosition(z.getX()-1, z.getY());
					z.maxSteps++;
					if(z.maxSteps==5) {
						z.maxSteps=0;
						z.left = false;
						z.setRegion(zRight2);
					}
					
				}
				else {
					z.setPosition(z.getX()+1, z.getY());
					z.maxSteps++;
					if(z.maxSteps==5) {
						z.maxSteps=0;
						z.left = true;
						z.setRegion(zLeft2);
					}
					
				}
				
				
				
			}
			
		}
		render();
		
		
		// TODO Auto-generated method stub
		return false;
	
	}
	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub

		if(keycode == Keys.LEFT)
			mc.setRegion(mcLefts);
			

		if(keycode == Keys.RIGHT)
		mc.setRegion(mcRights);
			
		if(keycode == Keys.UP)
		mc.setRegion(mcUps);
			
		if(keycode == Keys.DOWN)
		mc.setRegion(mcStand);
			

	



		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(float amountX, float amountY) {
		// TODO Auto-generated method stub
		return false;
	}
}
