package shadowBuild;

import org.newdawn.slick.Image;

import java.awt.RenderingHints;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public abstract class Resource {
	
	private double x;
	private double y;
	
	private Image image;
	
	public Resource(double x, double y) throws SlickException{
		this.x = x;
		this.y = y;	
	}
	
	/*public abstract String useMine();
	
	public abstract String useUnobtainium();*/
	
	public static Resource createResource(String name, double x, double y) throws SlickException {
		switch(name) {
		case("metal_mine"):
			return new Metal(x, y);
		case("unobtainium_mine"):
			return new Unobtainium(x, y);
		}
		return null;
	}
	
	
	
	//public abstract boolean isEnough(int amount);
	
	//public abstract void useResource(int amount);
	
	public void render(World world, Graphics g) {
		Camera camera = world.getCamera();
		getImage().drawCentered((int)camera.globalXToScreenX(getX()), (int)camera.globalYToScreenY(getY()));
	} 
	
	//draw amount text at (32, 32)
	
	
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
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
