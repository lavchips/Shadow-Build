package shadowBuild;

import java.util.ArrayList;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

import org.newdawn.slick.SlickException;

public class Pylon extends Building{
	private static final String PYLON_PATH = "assets/buildings/pylon.png";
	private static final String PYLON_ACTIVE_PATH = "assets/buildings/pylon_active.png";
	private static final int NEAR_DISTANCE  = 32;
	
	private Image image = new Image(PYLON_PATH);
	private Image activatedImage = new Image(PYLON_ACTIVE_PATH);

	public Pylon(double x, double y) throws SlickException {
		super(x, y);
		
	}
	
	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
	
	public void swapActiveImage() {
		
		this.image = activatedImage;
	}
	
	private boolean activated = false;
	private boolean counted = false;

	public void setActivationState(boolean state) {
		this.activated = state;
	}
	public boolean getActivated() {
		return activated;
	}
	public void setCounted(boolean counted) {
		this.counted = counted;
	}
	public boolean getCounted() {
		return counted;
	}
	
	@Override
	public void trainUnit(Input input, int delta, ArrayList<Resource> resourceList, ArrayList<Building> buildingList,
			ArrayList<Unit> unitList, ResourceHelper resourceInfo) throws SlickException {
	}

	@Override
	public void drawCommandText(Graphics g) {	
	}

}
