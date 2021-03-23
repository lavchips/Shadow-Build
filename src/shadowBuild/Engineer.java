package shadowBuild;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

/** This class contains all the engineer objects and their interactions with other classes
 */
public class Engineer extends Unit{
	
	private static final double SPEED = 0.1;
	private static final String ENGINEER_PATH = "assets/units/engineer.png";

	private static final int NEAR_RESOURCE = 32; 
	private static final int TIME_NEEDED = 5000;
	
	private static final int INITIAL_CAPACITY = 2;
	private Image image = new Image(ENGINEER_PATH);

	public Engineer(double x, double y) throws SlickException {
		super(x, y);
	}
	
	/** Find the closest command centre
     * @param buildingList A list of all the buildings on map.
     * @return The closest command centre
     */
	public Building findClosestCommandCentre(ArrayList<Building>buildingList) {
		double distance = -1;
		Building buildingTarget = null;
		for(Building building: buildingList) {
			if(building instanceof CommandCentre) {
				if(distance == -1) {
					distance = World.distance(this.getX(), this.getY(), building.getX(), building.getY());
					buildingTarget = building;
				}else {
					if(distance > World.distance(this.getX(), this.getY(), building.getX(), building.getY())) {
						distance = World.distance(this.getX(), this.getY(), building.getX(), building.getY());
						buildingTarget = building;
					}	
				}
				
			}
		}
		return buildingTarget;
	}

	
	private int nearResourceTime = 0;
	private boolean autoMoving = false;
	
	public boolean getAutoMoving() {
		return autoMoving;
	}
	public void setAutoMoving(boolean state) {
		this.autoMoving = state;
	}

	private double fromX = -1;
	private double fromY = -1;
	
	private boolean isMining = false;
	private boolean clicked = false;
	private boolean isMoving = false;
	
	/** Get the mining state of the engineer
	 * @return Return true if the engineer is mining else false
	 */
	private boolean getMiningState() {
		return isMining;
	}
	
	/** Set the mining state of the engineer
	 * @param state Another mining state
	 */
	private void setMiningState(boolean state) {
		this.isMining = state;
	}
	/** Get the state of right mouse
	 * @return Return true if mouse right is clicked else false
	 */
	private boolean getClickState() {
		return clicked;
	}
	/** Set the state of the right mouse
	 * @return Return true if mouse right is clickes else false
	 */
	private void setClickState(boolean state) {
		this.clicked = state;
	}
	
	private boolean minedMetal = false;
	
	public boolean getMinedMetal() {
		return minedMetal;
	}
	
	public void setMinedMetal(boolean state) {
		this.minedMetal = state;
	}
	
	/** Get the moving state of the engineer
	 * @return Return true if the engineer is moving else false
	 */
	public boolean getMovingState() {
		return isMoving;
	}
	public void setMovingState(boolean state) {
		this.isMoving = state;
	}
	
	private boolean backToResource = false;
	
	
	private double targetX = -1;
	private double targetY = -1;
	
	
	/** Update the engineer's mining process
     * @param delta Time passed since last frame (milliseconds).
     * @param resourceList A list of all the resources on map
     * @param buildingList A list of all the buildings on map.
     */
	public void mining(ArrayList<Resource>resourceList, ArrayList<Building>buildingList, int delta) {
		for(Resource resource:resourceList) {
			if(World.distance(this.getX(), this.getY(), resource.getX(), resource.getY()) <= NEAR_RESOURCE) {
				if(resource instanceof Metal) {
					setMinedMetal(true);
				}
				if(resource instanceof Unobtainium){
					setMinedMetal(false);
				}
				nearResourceTime += delta;
				if(nearResourceTime >= TIME_NEEDED) {
					targetX = findClosestCommandCentre(buildingList).getX();
					targetY = findClosestCommandCentre(buildingList).getY();
					fromX = getX();
					fromY = getY();
					setMovingState(true);
					nearResourceTime = 0;
					setMiningState(true);
				}
				
			}
		}
	}
	
	/** Update the movement of the engineer
     * @param resourceList A list of all the resources on map
     * @param buildingList A list of all the buildings on map.
     * @param unitList A list of all the units on map
     * @param resourceInfo The info of the amount of all the resources
     * @param world Game world
     */
	public void engineerMove(World world, ArrayList<Resource>resourceList, ArrayList<Building>buildingList, ArrayList<Unit>unitList, ResourceHelper resourceInfo) {
		
		// get the capacity of the engineer
		int capacity = 2 + world.getActivatedPylonCount();
		
		
		if (World.distance(getX(), getY(), targetX, targetY) <= getSpeed()) {
			if(getClickState()) {
				setClickState(false);
				setMovingState(false);	
			}
			

			if(getMiningState()) {
				if(minedMetal) {
					if(resourceInfo.mapHasEnoughMetal(resourceInfo.getOwnedMetal() + capacity)) {
						resourceInfo.setOwnedMetal(resourceInfo.getOwnedMetal() + capacity);
					}else {
						resourceInfo.setOwnedMetal(resourceInfo.getTotalMetal());
					}
					
				}else {
					if(resourceInfo.mapHasEnoughUnobtainium(resourceInfo.getOwnedUnobtainium() + capacity)) {
						resourceInfo.setOwnedUnobtainium(resourceInfo.getOwnedUnobtainium() + capacity);
					}else {
						resourceInfo.setOwnedUnobtainium(resourceInfo.getTotalUnobtainium());
					}
					
				}
				
				targetX = fromX;
				targetY = fromY;
				setMiningState(false);
				backToResource = true;
		
			}
			
			if(backToResource) {
				mining(resourceList, buildingList, world.getDelta());
				
			}
			
		} else {
			
			// Calculate the appropriate x and y distances
			if((targetX != -1) && (targetY != -1)) {
				setMovingState(true);
				double theta = Math.atan2(targetY - getY(), targetX - getX());
	
				double dx = (double)Math.cos(theta) * world.getDelta() * getSpeed();
				
				double dy = (double)Math.sin(theta) * world.getDelta() * getSpeed();
				// Check the tile is free before moving; otherwise, we stop moving
				if (!(world.isPositionSolid(getX() + dx, getY() + dy))) {
					setX(getX()+dx);
					setY(getY()+dy);
				} else {
					//resetTarget();
				}
				
			}
			
		}
		
	}
	
	/** If mouse is clicked somewhere else, interrupt the mining process and move to the target
     * @param input Input from the system
     * @param delta Time passed since last frame (milliseconds).
     * @param resourceList A list of all the resources on map
     * @param buildingList A list of all the buildings on map.
     * @param unitList A list of all the units on map
     * @param resourceInfo The info of the amount of all the resources
     * @param world Game world
     */
	@Override
	public void doWork(Input input, int delta, ArrayList<Resource> resourceList, ArrayList<Building> buildingList,ArrayList<Unit> unitList,
			ResourceHelper resourceInfo, World world) throws SlickException {
		
		Camera camera = world.getCamera();
		
		// If the mouse button is being clicked, set the target to the cursor location
		if (input.isMousePressed(Input.MOUSE_RIGHT_BUTTON)) {
			
			targetX = camera.screenXToGlobalX(input.getMouseX());
			targetY = camera.screenYToGlobalY(input.getMouseY());
			
			setClickState(true);
			setMiningState(false);
			nearResourceTime = 0;
			backToResource = false;
		}
		
		// if it reaches a resource, then start mining
		if(!getClickState()) {
			mining(resourceList, buildingList, delta);
		}
	}
	
	
	public double getSpeed() {
		return SPEED;
	}
	
	public Image getImage() {
		return image;
	}
	
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	@Override
	public void drawCommandText(Graphics g) {	
	}
	


}
