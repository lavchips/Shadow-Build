package shadowBuild;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Truck extends Unit{
	
	private static final double SPEED = 0.25;
	private static final String TRUCK_PATH = "assets/units/truck.png";
	private static final String BUILDING_NAME = "command_centre";
	private static final int TIME_NEEDED = 15000;
	private static final int DEFAULT_KEYDOWNTIME = -1;
	
	private static final int TEXTX = 32;
	private static final int TEXTY = 100;
	
	private Image image = new Image(TRUCK_PATH);

	public Truck(double x, double y) throws SlickException {
		super(x, y);
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





	private int time = 0;
	private int keyDownTime =  DEFAULT_KEYDOWNTIME;
	@Override
	public void doWork(Input input, int delta, ArrayList<Resource> resourceList, ArrayList<Building> buildingList, ArrayList<Unit> unitList, ResourceHelper resourceInfo, World world) throws SlickException{
		
		time += delta;
	    if(input.isKeyPressed(Input.KEY_1)) {	
	    	if(!world.isPositionOccupied(getX(), getY())) {
	    		keyDownTime = time;
			    setMoveState("lock");
			    resetTarget();
	    		
	    	}
	    	
	    }
	    if((time - keyDownTime >= TIME_NEEDED) && (keyDownTime !=  DEFAULT_KEYDOWNTIME)) {
	    	
	    		buildingList.add(Building.createBuilding(BUILDING_NAME, getX(), getY()));
	    		unitList.remove(this);
		    	setMoveState("unlock");
		    	keyDownTime = DEFAULT_KEYDOWNTIME;
	    	
	    	
	    }
	    
	}




	@Override
	public void drawCommandText(Graphics g) {
		g.drawString("1- Create Command Centre", TEXTX, TEXTY);
		
	}




}
