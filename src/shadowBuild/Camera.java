package shadowBuild;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import java.util.ArrayList;
/**
 * This class should be used to restrict the game's view to a subset of the entire world.
 * 
 * You are free to make ANY modifications you see fit.
 * These classes are provided simply as a starting point. You are not strictly required to use them.
 */
public class Camera{
	
	
	private double x = 300;
	private double y = 300;
	private static final double SPEED = 0.4;

	
	private Object target;
	
	// follow the given object
	public void followObject(Object target) {
		if(target instanceof Unit) {
			this.target = (Unit)target;
		}else if(target instanceof Building) {
			this.target = (Building)target;
		}	
	}
	
	
	// if click somewhere else, check if the selected object is deselectable
 	public boolean deselectable(double x, double y) {
 		
		if(target instanceof Unit) {
			if(World.distance(x, y, ((Unit)target).getX(), ((Unit)target).getY()) > 32) {
				return true;
			}
		}else if(target instanceof Building) {
			if(World.distance(x, y, ((Building)target).getX(), ((Building)target).getY()) > 32) {
				return true;
			}
		}
		
		return false;
	}
 	
 	// Deselect the target
 	public void deselect(){
		this.target = null;
	}

	
	public double globalXToScreenX(double x) {
		return x - this.x;
	}
	public double globalYToScreenY(double y) {
		return y - this.y;
	}

	public double screenXToGlobalX(double x) {
		return x + this.x;
	}
	
	public double screenYToGlobalY(double y) {
		return y + this.y;
	}
	
	private boolean unlocked = false;
	public void setUnlocked(boolean unlocked) {
		this.unlocked = unlocked;
	}
	public void update(World world) {
		Input input = world.getInput();
		double delta = world.getDelta();
		//!!! put into separate method 
				// setUnlocked(false);
		
		 if(input.isKeyDown(Input.KEY_W)) {
			 setUnlocked(true);
			 if(y>=0) {
				 y -= delta * SPEED; 
			 } 
			
		 }
		 if(input.isKeyDown(Input.KEY_S)) {
			 setUnlocked(true);
			 if(y <= world.getMapHeight() - App.WINDOW_HEIGHT) {
				 y += delta * SPEED; 
			 } 
			 
		 }
		 if(input.isKeyDown(Input.KEY_A)) {
			 setUnlocked(true);
			 if(x>=0) {
				 x -= delta * SPEED; 
			 } 
		
		 }
		 if(input.isKeyDown(Input.KEY_D)) {
			 setUnlocked(true);
			 if(x<=world.getMapWidth() - App.WINDOW_WIDTH) {
				 x += delta * SPEED; 
			 } 
			
		 }
		 
		 
		 if(!unlocked) {
			 if(target instanceof Unit) {
					
					double targetX = ((Unit)target).getX() - App.WINDOW_WIDTH / 2;
					double targetY = ((Unit)target).getY() - App.WINDOW_HEIGHT / 2;

					x = Math.min(targetX, world.getMapWidth() -	 App.WINDOW_WIDTH);
					x = Math.max(x, 0);
					y = Math.min(targetY, world.getMapHeight() - App.WINDOW_HEIGHT);
					y = Math.max(y, 0);
					
				}else if(target instanceof Building){
				
					double targetX = ((Building)target).getX() - App.WINDOW_WIDTH / 2;
					double targetY = ((Building)target).getY() - App.WINDOW_HEIGHT / 2;

					x = Math.min(targetX, world.getMapWidth() -	 App.WINDOW_WIDTH);
					x = Math.max(x, 0);
					y = Math.min(targetY, world.getMapHeight() - App.WINDOW_HEIGHT);
					y = Math.max(y, 0);
						
				}else {
				
				}
			 
		 }
		
			
			
		
		
		
		
		
		
		
	}
}