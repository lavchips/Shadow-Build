package shadowBuild;

import org.newdawn.slick.Image;

import java.awt.RenderingHints;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import java.util.ArrayList;

public class ResourceHelper {
	
	private static final int METAL_ON_MAP = 500;
	private static final int UNOBTAINIUM_ON_MAP = 50;
	private static final int TEXTX = 32;
	private static final int TEXTY = 32;
	
	public int getTotalMetal() {
		return METAL_ON_MAP;
	}
	public int getTotalUnobtainium() {
		return UNOBTAINIUM_ON_MAP;
	}
	/*!!! change */
	private int ownedMetal = 300;
	private int ownedUnobtainium = 20;
	
	public int getOwnedMetal() {
		return ownedMetal;
	}
	public void setOwnedMetal(int amount) {
		this.ownedMetal = amount;
	}
	
	public int getOwnedUnobtainium() {
		return ownedUnobtainium;
	}
	public void setOwnedUnobtainium(int amount) {
		this.ownedUnobtainium = amount;
	}
	
	/* Use resources */
	public boolean hasEnoughMetal(int amount) {
		if((this.ownedMetal - amount) < 0) {
			return false;
		}
		return true;	
	}
	
	public void useMetal(int amount) {
		if(hasEnoughMetal(amount)) {
			this.ownedMetal = this.ownedMetal - amount;
		}
	}
	
	public boolean hasEnoughUnobtainium(int amount) {
		if((this.ownedUnobtainium - amount) < 0) {
			return false;
		}
		return true;	
	}
	
	public void useUnobtainium(int amount) {
		if(hasEnoughUnobtainium(amount)) {
			this.ownedUnobtainium = this.ownedUnobtainium - amount;
		}
	}
	
	public boolean mapHasEnoughMetal(int minedMetal) {
		if(minedMetal <= METAL_ON_MAP) {
			return true;
		}
		return false;
	}
	
	public boolean mapHasEnoughUnobtainium(int minedUnobtainium) {
		if(minedUnobtainium <= UNOBTAINIUM_ON_MAP) {
			return true;
		}
		return false;
	}
	
	
	public void update(ArrayList<Resource>resourceList) {
		if(ownedMetal >= METAL_ON_MAP) {
			for(int i = resourceList.size() - 1; i>= 0; i--) {
				if(resourceList.get(i) instanceof Metal) {
					
					resourceList.remove((Metal)resourceList.get(i));
				}
			}
		}
		
		if(ownedUnobtainium >= UNOBTAINIUM_ON_MAP) {
			for(int i = resourceList.size() - 1; i>= 0; i--) {
				if(resourceList.get(i) instanceof Unobtainium) {
					resourceList.remove((Unobtainium)resourceList.get(i));
				}
			}
		}
		
	}
	/* Mine resources*/
	

	
	public void drawText(Graphics g) {
		g.drawString("Metal: "+Integer.toString(ownedMetal)+"\nUnobtainium: "+Integer.toString(ownedUnobtainium), TEXTX, TEXTY);
	}

}
