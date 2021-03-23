package shadowBuild;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.Graphics;
import java.util.ArrayList;
import org.newdawn.slick.SlickException;

public abstract class Building {
	
	private double x;
	private double y;
	
	
	public Building(double x, double y)throws SlickException{
		this.x = x;
		this.y = y;
	}
	
	public static Building createBuilding(String name, double x, double y) throws SlickException {
		switch(name) {
		case("command_centre"):
			return new CommandCentre(x, y);
		case("factory"):
			return new Factory(x, y);
		case("pylon"):
			return new Pylon(x, y);
		}
		
		return null;
	}
	
	public abstract void trainUnit(Input input, int delta, ArrayList<Resource>resourceList, ArrayList<Building> buildingList, ArrayList<Unit>unitList, ResourceHelper resourceInfo)throws SlickException;
	
	public void update(World world, ArrayList<Resource>resourceList, ArrayList<Building> buildingList, 
			ArrayList<Unit>unitList, ResourceHelper resourceInfo) throws SlickException{
		
		Input input = world.getInput();
		trainUnit(input, world.getDelta(), resourceList, buildingList, unitList, resourceInfo);
	}
	
	public abstract void drawCommandText(Graphics g);
	
	
	public void render(World world) {
		Camera camera = world.getCamera();
		getImage().drawCentered((int)camera.globalXToScreenX(getX()), (int)camera.globalYToScreenY(getY()));
	} 
	
	
	public abstract Image getImage();
	public abstract void setImage(Image image);
	
	
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
