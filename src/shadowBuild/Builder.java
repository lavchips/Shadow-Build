package shadowBuild;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import java.util.ArrayList;
import org.newdawn.slick.SlickException;

/** This class contains all the builder objects and their interations with other classes
 */

public class Builder extends Unit{

	private static final double SPEED = 0.1;
	private static final String BUILDER_PATH = "assets/units/builder.png";
	private static final String BUILDING_NAME = "factory";
	private static final int FACTORY_COST = 100;
	private static final int TIME_NEEDED = 10000;
	
	private static final int DEFAULT_KEYDOWNTIME = -1;
	
	private static final int TEXTX = 32;
	private static final int TEXTY = 100;
	
	private Image image = new Image(BUILDER_PATH);
	
	public Builder(double x, double y) throws SlickException {
		super(x, y);
		
	}
	
	/** get the speed of the builder
     * 
     * @return a double of the builder's speed
     */
	public double getSpeed() {
		return SPEED;
	}
	
	/** Get the icon of the builder
     * 
     * @return image of the builder's icon
     */
	public Image getImage() {
		return image;
	}
	
	/** Set the icon of the builder
     * @param image another image
     */
	public void setImage(Image image) {
		this.image = image;
	}



	private int time = 0;
	private int keyDownTime = DEFAULT_KEYDOWNTIME;
	
	/** The creation of factory 
     * @param input Input from the system
     * @param delta Time passed since last frame (milliseconds).
     * @param resourceList A list of all the resources on map
     * @param buildingList A list of all the buildings on map.
     * @param unitList A list of all the units on map
     * @param resourceInfo The info of the amount of all the resources
     * @param world Game world
     */
	@Override
	public void doWork(Input input, int delta, ArrayList<Resource> resourceList, ArrayList<Building> buildingList, ArrayList<Unit> unitList, ResourceHelper resourceInfo, World world) throws SlickException{
		
		time += delta;
		
		if(input.isKeyPressed(Input.KEY_1)) {	
		    	if(resourceInfo.hasEnoughMetal(FACTORY_COST) && (!(world.isPositionOccupied(getX(), getY())))) {
		    		
		    			resourceInfo.useMetal(FACTORY_COST);
			    		keyDownTime = time;
			    		setMoveState("lock");
			    		
			    		// when 1 is pressed, create building immediately and cancel planned path
			    		resetTarget();
		    	}
		    }
			
		
	    if((time - keyDownTime >= TIME_NEEDED) && (keyDownTime != DEFAULT_KEYDOWNTIME)) {
	    	 
	    		buildingList.add(Building.createBuilding(BUILDING_NAME, getX(), getY()));
	    		setMoveState("unlock");
		    	keyDownTime = DEFAULT_KEYDOWNTIME;

	    }
	    
	}

    /** Render command instructions
     * @param g The Slick graphics object, used for drawing.
     */

	@Override
	public void drawCommandText(Graphics g) {
		g.drawString("1- Create Factory", TEXTX, TEXTY);
		
	}

	

}
