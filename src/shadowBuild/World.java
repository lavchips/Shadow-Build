package shadowBuild;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.BufferedReader;
import org.newdawn.slick.Image;

/**
 * This class should be used to contain all the different objects in your game world, and schedule their interactions.
 * 
 * You are free to make ANY modifications you see fit.
 * These classes are provided simply as a starting point. You are not strictly required to use them.
 */
public class World {
	private static final String MAP_PATH = "assets/main.tmx";
	private static final String CSV_PATH = "assets/objects.csv";
	private static final String SOLID_PROPERTY = "solid";
	private static final String OCCUPIED_PROPERTY = "occupied";

	
	
	private TiledMap map;
	private Camera camera;
	private ResourceHelper resourceInfo = new ResourceHelper();
	
	
	
	private Input lastInput;
	private int lastDelta;
	
	public Input getInput() {
		return lastInput;
	}
	public int getDelta() {
		return lastDelta;
	}
	public Camera getCamera() {
		return camera;
	}
	
	
	//check solid
	public boolean isPositionSolid(double x, double y) {
		int tileId = map.getTileId(worldXToTileX(x), worldYToTileY(y), 0);
		return Boolean.parseBoolean(map.getTileProperty(tileId, SOLID_PROPERTY, "false"));
	}
	// check occupied
	public boolean isPositionOccupied(double x, double y) {
		int tileId = map.getTileId(worldXToTileX(x), worldYToTileY(y), 0);
		return Boolean.parseBoolean(map.getTileProperty(tileId, OCCUPIED_PROPERTY, "false"));
	}
	
	
	public double getMapWidth() {
		return map.getWidth() * map.getTileWidth();
	}
	
	public double getMapHeight() {
		return map.getHeight() * map.getTileHeight();
	}
	
	
	private ArrayList<Resource> resourceOnMap = new ArrayList<Resource>();
	private ArrayList<Unit> unitOnMap = new ArrayList<Unit>();
	private ArrayList<Building> buildingOnMap = new ArrayList<Building>();
	
	// world constructor
	
	public World() throws SlickException {
		
		map = new TiledMap(MAP_PATH);
		camera = new Camera();
		addInitialObjects();
		
		
	}
	
	
	
	private Unit selectedUnit;
	private Building selectedBuilding;
	private void setSelectedUnit(Unit selectedUnit) {
		this.selectedUnit = selectedUnit;
	}
	
	private int activatedPylonCount = 0;
	public int getActivatedPylonCount() {
		return activatedPylonCount;
	}
	public void update(Input input, int delta) throws SlickException {
		
		lastInput = input;
		lastDelta = delta;
		
		
		
		// !!!jumping instead of moving smoothly
		// Select & deselect object 
		
		if(input.isMousePressed(Input.MOUSE_LEFT_BUTTON)){
			
			double leftClickX = camera.screenXToGlobalX(input.getMouseX());
			double leftClickY = camera.screenYToGlobalY(input.getMouseY());
			
			// deselect
			if(camera.deselectable(leftClickX, leftClickY)) {
				
				if(selectedBuilding != null) {
					selectedBuilding = null;
					
				}else {
					selectedUnit = null;
					
				}
	
					
					
				
				camera.deselect();
					
			}
			
			//Unit should be selected first
			if((selectedUnit==null) && (selectedBuilding==null)) {
				for(Unit unit:unitOnMap) {
					if(World.distance(unit.getX(), unit.getY(), leftClickX, leftClickY) <= 32) {
						camera.followObject(unit);
						selectedUnit = unit;
						camera.setUnlocked(false);
					}
				}	
			}
			
			//swap select
			if((selectedBuilding != null) && (selectedUnit == null)) {
				for(Unit unit:unitOnMap) {
					if(World.distance(unit.getX(), unit.getY(), leftClickX, leftClickY) <= 32) {
						camera.deselect();
						selectedBuilding = null;
						camera.followObject(unit);
						selectedUnit = unit;
						camera.setUnlocked(false);
					}
				}
				
				// if click on the same building object
				for(Building building:buildingOnMap) {
					if(World.distance(building.getX(), building.getY(), leftClickX, leftClickY) <= 32) {
						camera.setUnlocked(false);
					}
				}
			}
			
			// select building
			if((selectedUnit==null) && (selectedBuilding==null)) {
				
				for(Building building:buildingOnMap) {
					if(World.distance(building.getX(), building.getY(), leftClickX, leftClickY) <= 32) {
						camera.followObject(building);
						selectedBuilding = building;
						camera.setUnlocked(false);
					}
				}
			}
			
			// camera unlock click on the same unit object
			if(selectedUnit!=null) {
				for(Unit unit:unitOnMap) {
					if(World.distance(unit.getX(), unit.getY(), leftClickX, leftClickY) <= 32) {
						camera.setUnlocked(false);
					}
				}
			}
			
				
		}
		
		camera.update(this);
		
		resourceInfo.update(resourceOnMap);
		for(Building building:buildingOnMap) {
			if(building instanceof Pylon) {
				if(((Pylon)building).getActivated() && !((Pylon)building).getCounted()) {
					activatedPylonCount += 1;
					((Pylon)building).setCounted(true);
				}
			}
		}
		
		//!!! check lecture recording to see if put engineer at a resource then deselect will it mine or not
		
		if(selectedUnit!=null) {
			
			selectedUnit.update(this, resourceOnMap, buildingOnMap, unitOnMap, resourceInfo);
			selectedUnit.move(this);
			
			if(selectedUnit instanceof Engineer) {
				
				((Engineer)selectedUnit).engineerMove(this, resourceOnMap, buildingOnMap, unitOnMap, resourceInfo);
			}
			// destory Truck after training command centre
			if(!(unitOnMap.contains(selectedUnit))) {
				selectedUnit = null;
			}
		}
		
		// if any unit object is still moving then update the movement
		for(Unit unit:unitOnMap) {
			if((unit instanceof Engineer) && (selectedUnit != unit)) {
				
					((Engineer)unit).mining(resourceOnMap, buildingOnMap, this.getDelta());
					((Engineer)unit).engineerMove(this, resourceOnMap, buildingOnMap, unitOnMap, resourceInfo);
				
			}
			if(unit.getIsMoving() && !(unit instanceof Engineer) && selectedUnit != unit) {
				unit.move(this);
			}
		}
		
		
		
		if(selectedBuilding != null){
			selectedBuilding.update(this, resourceOnMap, buildingOnMap, unitOnMap, resourceInfo);
		}
		
		
		
		
		
	}
	
	
	Image unitHighlight = new Image("assets/highlight.png");
	Image buildingHighlight = new Image("assets/highlight_large.png");
	
	public void render(Graphics g) {
		
		//draw world map 
		map.render((int)camera.globalXToScreenX(0),
				   (int)camera.globalYToScreenY(0));
		
		if(selectedBuilding != null) {
			buildingHighlight.drawCentered((int)camera.globalXToScreenX(selectedBuilding.getX()), (int)camera.globalYToScreenY(selectedBuilding.getY()));
			selectedBuilding.drawCommandText(g);
		}
		
		if((selectedUnit != null)) {
			unitHighlight.drawCentered((int)camera.globalXToScreenX(selectedUnit.getX()), (int)camera.globalYToScreenY(selectedUnit.getY()));
			selectedUnit.drawCommandText(g);
		}
		
		// draw the updated position 
		// draw resources
		for(Resource resource:resourceOnMap) {
			resource.render(this, g);
		}
		resourceInfo.drawText(g);
		
		// initial buildings
		for(Building building:buildingOnMap) {
			building.render(this);
		}
		
		// initial units 
		for(Unit unit:unitOnMap) {
			unit.render(this);
		}
		
		
		
		
		
	}
	
	
	
	
	
	
	// read in the objects in the csv file 
	public void addInitialObjects() {
		try(BufferedReader br = new BufferedReader(new FileReader(CSV_PATH))){
			
			String text;
			
			while((text = br.readLine()) != null) {
				String line[] = text.split(",");
				
				// Resources
				if((line[0].equals("metal_mine")) || (line[0].equals("unobtainium_mine"))) {
					resourceOnMap.add(Resource.createResource(line[0], Double.parseDouble(line[1]), Double.parseDouble(line[2])));
				//Buildings
				}else if((line[0].equals("command_centre"))||(line[0].equals("factory"))||(line[0].equals("pylon"))) {
					buildingOnMap.add(Building.createBuilding(line[0], Double.parseDouble(line[1]), Double.parseDouble(line[2])));
				//Units
				}else {
					unitOnMap.add(Unit.createUnit(line[0], Double.parseDouble(line[1]), Double.parseDouble(line[2])));
				}
				
			}	
		
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	// This should probably be in a separate static utilities class, but it's a bit excessive for one method.
	public static double distance(double x1, double y1, double x2, double y2) {
		return (double)Math.sqrt((x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1));
	}
	
	private int worldXToTileX(double x) {
		return (int)(x / map.getTileWidth());
	}
	private int worldYToTileY(double y) {
		return (int)(y / map.getTileHeight());
	}
	
	

	
	
}
