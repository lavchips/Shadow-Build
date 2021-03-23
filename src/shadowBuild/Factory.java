package shadowBuild;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class Factory extends Building{
	
	private static final String FACTORY_PATH = "assets/buildings/factory.png";
	private static final String TRAIN_TRUCK = "truck";
	private static final int TRUCK_COST = 150;
	private static final int TIME_NEEDED = 5000;
	
	private static final int DEFAULT_KEYDOWNTIME = -1;
	
	
	private static final int TEXTX = 32;
	private static final int TEXTY = 100;
	private Image image = new Image(FACTORY_PATH);

	public Factory(double x, double y) throws SlickException {
		super(x, y);
	
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}

	private int time = 0;
	private int keyDownTime = DEFAULT_KEYDOWNTIME;
	@Override
	public void trainUnit(Input input, int delta, ArrayList<Resource> resourceList, ArrayList<Building> buildingList,
			ArrayList<Unit> unitList, ResourceHelper resourceInfo) throws SlickException {
		
		time += delta;
	    if(input.isKeyPressed(Input.KEY_1)) {	
	    	if(resourceInfo.hasEnoughMetal(TRUCK_COST)) {
	    		resourceInfo.useMetal(TRUCK_COST);
	    		keyDownTime = time;	
	    	}
	    }
	    
	    if((time - keyDownTime >= TIME_NEEDED) && (keyDownTime != DEFAULT_KEYDOWNTIME)) {
	    	
	    	unitList.add(Unit.createUnit(TRAIN_TRUCK, getX(), getY()));
	    	keyDownTime = DEFAULT_KEYDOWNTIME;
	    }
	  
		
	}

	@Override
	public void drawCommandText(Graphics g) {
		g.drawString("1- Create Truck\n", TEXTX, TEXTY);
		
	}
	
	
}
