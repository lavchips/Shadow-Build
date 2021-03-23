package shadowBuild;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class CommandCentre extends Building{
	
	private static final String COMMANDCENTRE_PATH = "assets/buildings/command_centre.png";
	
	private static final int TIME_NEEDED = 5000;
	
	private static final String TRAIN_SCOUT = "scout";
	private static final String TRAIN_BUILDER = "builder";
	private static final String TRAIN_ENGINEER = "engineer";
	
	private static final int SCOUT_COST = 5;
	private static final int BUILDER_COST = 10;
	private static final int ENGINEER_COST = 20;
	
	private static final int DEFAULT_KEYDOWNTIME = -1;
	
	private static final int TEXTX = 32;
	private static final int TEXTY = 100;
	private Image image = new Image(COMMANDCENTRE_PATH);
	
	public CommandCentre(double x, double y)throws SlickException{
		super(x, y);
	}
	
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public void useResourceAndTrain(ResourceHelper resourceInfo, int cost, int time){
    		resourceInfo.useMetal(cost);
    		setKeyDownTime(time);
	}
	
	
	private int time = 0;
	private int keyDownTime = DEFAULT_KEYDOWNTIME;
	private boolean isTraining = false;
	private String unitOnTraining;
	
	private void setTrainingState(boolean state) {
		this.isTraining = state;
	}
	
	private void setUnitOnTraining(String unitOnTraining) {
		this.unitOnTraining = unitOnTraining;
	}
	
	private String getUnitOnTraining() {
		return unitOnTraining;
	}
	
	public void setKeyDownTime(int keyDownTime) {
		this.keyDownTime = keyDownTime;
	}
	
	@Override
	public void trainUnit(Input input, int delta, ArrayList<Resource> resourceList, ArrayList<Building> buildingList,
			ArrayList<Unit> unitList, ResourceHelper resourceInfo) throws SlickException {
		
		time += delta;
		
		// can't train at the same time 
	    if(input.isKeyPressed(Input.KEY_1) && (!isTraining)) {	
	    	if(resourceInfo.hasEnoughMetal(SCOUT_COST)) {
	    		useResourceAndTrain(resourceInfo, SCOUT_COST, time);
	    		setTrainingState(true);
	    		setUnitOnTraining(TRAIN_SCOUT);
	    	}
	    }
	    
	    if(input.isKeyPressed(Input.KEY_2) && (!isTraining)) {	
	    	if(resourceInfo.hasEnoughMetal(BUILDER_COST)) {
	    		useResourceAndTrain(resourceInfo, BUILDER_COST, time);
	    		setTrainingState(true);
	    		setUnitOnTraining(TRAIN_BUILDER);
	    	}
	    }
	    
	    if(input.isKeyPressed(Input.KEY_3) && (!isTraining)) {	
	    	if(resourceInfo.hasEnoughMetal(ENGINEER_COST)) {
	    		useResourceAndTrain(resourceInfo, ENGINEER_COST, time);
	    		setTrainingState(true);
	    		setUnitOnTraining(TRAIN_ENGINEER);
	    	}
	    }
	    
	    if((time - keyDownTime >= TIME_NEEDED) && (keyDownTime != DEFAULT_KEYDOWNTIME)) {
	    	
	    	unitList.add(Unit.createUnit(getUnitOnTraining(), getX(), getY()));
	    	setTrainingState(false);
	    	keyDownTime = DEFAULT_KEYDOWNTIME;
	    }
	}
	
	public void drawCommandText(Graphics g) {
		g.drawString("1- Create Scout\n2- Create Builder\n3- Create Engineer\n", TEXTX, TEXTY);
	}

}
