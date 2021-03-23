package shadowBuild;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/**
 * Contains all the unit objects 
 *
 */
public abstract class Unit{
	
	private static final int NEAR_DISTANCE = 32;
	private double x;
	private double y;
	
	private double targetX = -1;
	private double targetY = -1;
	
	
	
	
	public Unit(double x, double y)throws SlickException{
		this.x = x;
		this.y = y;
	}
	
	/**
	 * Create unit object
	 * @param name The unit that is going to be created
	 * @param x 
	 * @param y
	 * @return The created unit
	 * @throws SlickException
	 */
	public static Unit createUnit(String name, double x, double y) throws SlickException {
		switch(name) {
		case("engineer"):
			return new Engineer(x, y);
		case("scout"):
			return new Scout(x, y);
		case("builder"):
			return new Builder(x, y);
		case("truck"):
			return new Truck(x, y);
		}
		return null;
	}

	
	// check if the object should move or not
	private boolean moveLocked = false;
	
	/**
	 * Set moving state
	 * @param state
	 */
	public void setMoveState(String state) {
		switch(state) {
		case("lock"):
			this.moveLocked = true;
			return;
		case("unlock"):
			this.moveLocked = false;
			return;
		}
	}
	
	
	public boolean getMoveState() {
		return moveLocked;
	}
	

	private boolean clickProgress = false;
	private boolean isMoving = false;
	public void setClickProgress(boolean progress) {
		this.clickProgress = progress;
	}
	
	public void setIsMoving(boolean state) {
		this.isMoving = state;
	}
	
	public boolean getIsMoving() {
		return isMoving;
	}
	public abstract void doWork(Input input, int delta, ArrayList<Resource>resourceList, ArrayList<Building>buildingList, ArrayList<Unit> unitList, ResourceHelper resourceInfo, World world) throws SlickException;
	
	public void move(World world) {
		
		if (World.distance(x, y, targetX, targetY) <= getSpeed()) {
			setIsMoving(false);
			resetTarget();
		} else {
			
			// Calculate the appropriate x and y distances
			if((targetX != -1) && (targetY != -1)) {
				setIsMoving(true);
				double theta = Math.atan2(targetY - y, targetX - x);
	
				double dx = (double)Math.cos(theta) * world.getDelta() * getSpeed();
				
				double dy = (double)Math.sin(theta) * world.getDelta() * getSpeed();
				// Check the tile is free before moving; otherwise, we stop moving
				if (!(world.isPositionSolid(x + dx, y + dy))) {
					setIsMoving(true);
					x += dx;
				
					y += dy;
				} else {
					resetTarget();
				}
				
			}
			
		}
	}
	

	
	public void update(World world, ArrayList<Resource> resourceList, ArrayList<Building> buildingList, ArrayList<Unit> unitList, ResourceHelper resourceInfo) throws SlickException {
		
		Input input = world.getInput();
		Camera camera = world.getCamera();
		
		doWork(input, world.getDelta(), resourceList, buildingList, unitList, resourceInfo, world);
		
		if((this instanceof Scout) || (this instanceof Builder) || (this instanceof Truck) ) {
			
			// !!!!if the unit is building something, then don't move
			if(!moveLocked) {
				
				// If the mouse button is being clicked, set the target to the cursor location
				if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
					targetX = camera.screenXToGlobalX(input.getMouseX());
					targetY = camera.screenYToGlobalY(input.getMouseY());
					
				}
				
				// If we're close to our target, reset to our current position
				/*if (World.distance(x, y, targetX, targetY) <= getSpeed()) {
					setIsMoving(false);
					resetTarget();
				} else {
					
					// Calculate the appropriate x and y distances
					if((targetX != -1) && (targetY != -1)) {
						double theta = Math.atan2(targetY - y, targetX - x);
			
						double dx = (double)Math.cos(theta) * world.getDelta() * getSpeed();
						
						double dy = (double)Math.sin(theta) * world.getDelta() * getSpeed();
						// Check the tile is free before moving; otherwise, we stop moving
						if (!(world.isPositionSolid(x + dx, y + dy))) {
							setIsMoving(true);
							x += dx;
						
							y += dy;
						} else {
							resetTarget();
						}
						
					}
					
				}*/
				
			}
			
		}
		
		/*if(this instanceof Engineer) {
			
				if(!moveLocked) {
					
					// If the mouse button is being clicked, set the target to the cursor location
					if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
						targetX = camera.screenXToGlobalX(input.getMouseX());
						targetY = camera.screenYToGlobalY(input.getMouseY());
						setClickProgress(true);
					}
					
					
					// If we're close to our target, reset to our current position
					if (World.distance(x, y, targetX, targetY) <= getSpeed()) {
						resetTarget();
						setClickProgress(false);
					} else {
						
						// Calculate the appropriate x and y distances
						if((targetX != -1) && (targetY != -1)) {
							double theta = Math.atan2(targetY - y, targetX - x);
				
							double dx = (double)Math.cos(theta) * world.getDelta() * getSpeed();
							
							double dy = (double)Math.sin(theta) * world.getDelta() * getSpeed();
							// Check the tile is free before moving; otherwise, we stop moving
							if (!(world.isPositionSolid(x + dx, y + dy))) {
								x += dx;
							
								y += dy;
							} else {
								resetTarget();
							}
							
						}
						
					}
					
				}
				
		
			if(clickProgress) {
				doWork(input, world.getDelta(), resourceList, buildingList, unitList, resourceInfo, world);
			
			}
			
			
		}else {
			
			
			if(!moveLocked) {
				
				// If the mouse button is being clicked, set the target to the cursor location
				if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
					targetX = camera.screenXToGlobalX(input.getMouseX());
					targetY = camera.screenYToGlobalY(input.getMouseY());
				}
				
				// If we're close to our target, reset to our current position
				if (World.distance(x, y, targetX, targetY) <= getSpeed()) {
					resetTarget();
				} else {
					
					// Calculate the appropriate x and y distances
					if((targetX != -1) && (targetY != -1)) {
						double theta = Math.atan2(targetY - y, targetX - x);
			
						double dx = (double)Math.cos(theta) * world.getDelta() * getSpeed();
						
						double dy = (double)Math.sin(theta) * world.getDelta() * getSpeed();
						// Check the tile is free before moving; otherwise, we stop moving
						if (!(world.isPositionSolid(x + dx, y + dy))) {
							x += dx;
						
							y += dy;
						} else {
							resetTarget();
						}
						
					}
					
				}
				
			}
			
			
		}*/
		
		
		
		/*
		 * 
		 */
		// activate Pylon 
		for(Building building:buildingList) {
			if(building instanceof Pylon) {
				
				if(World.distance(x, y, building.getX(), building.getY()) <= NEAR_DISTANCE) {
					if(!((Pylon)building).getActivated()) {
						((Pylon) building).swapActiveImage();
						((Pylon) building).setActivationState(true);
					}
					
				}	
			}
			
		}
		
		

	}
	
	public void resetTarget() {
		targetX = x;
		targetY = y;		
	}
	
	public abstract void drawCommandText(Graphics g);

	public void render(World world) {
		Camera camera = world.getCamera();
		getImage().drawCentered((int)camera.globalXToScreenX(getX()), (int)camera.globalYToScreenY(getY()));
	} 

	public abstract Image getImage();
	
	public abstract void setImage(Image image);
	
	public abstract double getSpeed();

	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}

}
