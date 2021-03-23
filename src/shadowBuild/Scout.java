package shadowBuild;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;


public class Scout extends Unit{
	
	private static final double SPEED = 0.3;
	private static final String SCOUT_PATH = "assets/units/scout.png";
	private Image image = new Image(SCOUT_PATH);
	
	public Scout(double x, double y)throws SlickException{
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

	@Override
	public void doWork(Input input, int delta, ArrayList<Resource> resourceList, ArrayList<Building> buildingList, ArrayList<Unit> unitList, ResourceHelper resourceInfo, World world)
			 throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void drawCommandText(Graphics g) {
		// TODO Auto-generated method stub
		
	}


	

}
