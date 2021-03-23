package shadowBuild;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Metal extends Resource{
	
	private static final int INITIAL_AMOUNT = 500;
	private static final String METAL_PATH = "assets/resources/metal_mine.png";
	private Image image = new Image(METAL_PATH);
	
	private int amount = INITIAL_AMOUNT;

	public Metal(double x, double y)throws SlickException{
		super(x,y);
	}
	
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}

}
