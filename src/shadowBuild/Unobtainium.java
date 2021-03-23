package shadowBuild;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Unobtainium extends Resource{
	
	private static final int INITIAL_AMOUNT = 50;
	private static final String UNOBTAINIUM_PATH = "assets/resources/unobtainium_mine.png";
	
	Image image = new Image(UNOBTAINIUM_PATH);
	
	private int amount = INITIAL_AMOUNT;

	public Unobtainium(double x, double y)throws SlickException{
		super(x,y);
	}
	
	
	public Image getImage() {
		return image;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}


	

}
