/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import graph.Graph;
import graph.Vertex;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import pathx.PathX;
import pathx.PathX.PathXPropertyType;
import static pathx.PathX.PathXPropertyType.PATH_IMG;
import pathx.PathXConstants;
import static pathx.PathXConstants.GAME_VIEWPORT_X;
import static pathx.PathXConstants.PATH_LEVEL_SCHEMA;
import static pathx.PathXConstants.WINDOW_HEIGHT;
import static pathx.PathXConstants.WINDOW_WIDTH;
import pathx.file.PathXFileManager;
import static pathx.file.PathXFileManager.ID_ATT;
import static pathx.file.PathXFileManager.INTERSECTIONS_LIST_TAG;
import static pathx.file.PathXFileManager.INTERSECTION_TAG;
import static pathx.file.PathXFileManager.INT_ID1_ATT;
import static pathx.file.PathXFileManager.ONE_WAY_ATT;
import static pathx.file.PathXFileManager.OPEN_ATT;
import static pathx.file.PathXFileManager.ROAD_LIST_TAG;
import static pathx.file.PathXFileManager.ROAD_TAG;
import static pathx.file.PathXFileManager.SPEED_LIMIT_ATT;
import static pathx.file.PathXFileManager.X_ATT;
import static pathx.file.PathXFileManager.Y_ATT;
import pathx.ui.BanditCar;
import pathx.ui.Car;
import pathx.ui.CopCar;
import pathx.ui.PathXLevelSprite;
import pathx.ui.PathXMiniGame;
import pathx.ui.PathXNode;
import pathx.ui.PathXSpriteState;
import static pathx.ui.PathXSpriteState.MOUSE_OVER;
import static pathx.ui.PathXSpriteState.VISIBLE;
import pathx.ui.PlayerCar;
import pathx.ui.Road;
import pathx.ui.ZombieCar;
import properties_manager.PropertiesManager;
import xml_utilities.InvalidXMLFileFormatException;
import xml_utilities.XMLUtilities;

/**
 *
 * @author Andrew
 */
public class PathXDataModel extends MiniGameDataModel{
    
    private PathXMiniGame miniGame;
    private PathXLevel currentLevel;
    private PathXRecord record;
    
    //The Player's Car Sprite
    private PlayerCar playerCar;
    
    //References to all the opposing cars in the game.
    private ArrayList<CopCar> cops;
    private ArrayList<BanditCar> bandits;
    private ArrayList<ZombieCar> zombies;
    
    //References to all of the PathXNodes and Roads in the current level.
    private ArrayList<PathXNode> nodes;
    private ArrayList<Road> roads;
    
    //Used to check unlocked specials and unlocked/completed levels.
    private HashMap<String, Boolean> specials;
    private HashMap<String, PathXLevel> levels;
    
    //References to the Sprites used to render Levels on the level select screen.
    private ArrayList<PathXLevelSprite> levelSprites;
    
    private Viewport gameViewport;
    
     public PathXDataModel(PathXMiniGame initMiniGame){
        miniGame = initMiniGame;
        record = new PathXRecord();
        
        cops = new ArrayList();
        bandits = new ArrayList();
        zombies = new ArrayList();
        
        currentLevel = null;
        
        //Initialize the gameViewport
        //gameViewport = new Viewport();
        //initGameViewport();
        
        initSpecials();
        
    }
    
    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on Nodes, Cars, Specials, and etc.
     *
     * @param game The pathX game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y){
        
    }
    
    @Override
    public void endGameAsWin(){
        
    }
    
    @Override
    public void endGameAsLoss(){
        
    }
    
    //Helper method that adds all of the game specials to the specials HashMap 
    //for management in game.
    private void initSpecials(){
        
    }

    /**
     * Called when a level is started.
     *
     * @param game
     */
    @Override
    public void reset(MiniGame mg) {
//        Sprite s = player.getSprite();
//        player = new Car(s, level.getStartNode(), PathXSpriteState.STOPPED.toString());
        
    }

    @Override
    public void updateAll(MiniGame mg) {
        
    }

    @Override
    public void updateDebugText(MiniGame mg) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public PathXRecord getRecord() {
        return record;
    }

    public HashMap<String, Boolean> getSpecials() {
        return specials;
    }

    public HashMap<String, PathXLevel> getLevels() {
        return levels;
    }

    public void setSpecials(HashMap<String, Boolean> specials) {
        this.specials = specials;
    }

    public void setLevels(HashMap<String, PathXLevel> levels) {
        this.levels = levels;
    }
    
    public void updateRecord(){
        record.setLevels(levels);
        record.setSpecials(specials);
    }
    
    public void setLevelSprites(ArrayList<PathXLevelSprite> levelSprites) {
        this.levelSprites = levelSprites;
    }

    public ArrayList<PathXLevelSprite> getLevelSprites() {
        return levelSprites;
    }

    public ArrayList<PathXNode> getNodes() {
        return nodes;
    }

    public ArrayList<Road> getRoads() {
        return roads;
    }

    //Constructs all of the PathXNodes needed for a specific level. These Nodes 
    //are put into the nodes ArrayList. The connections between nodes are not 
    //created by this method yet.
    public void constructNodes(PathXLevel level) {
        //Needed for loading and analyzing the PathXLevel's XML file.s
        XMLUtilities xmlUtil = new XMLUtilities();
        Graph graph = level.getGraph();
        ArrayList<PathXNode> intersections = new ArrayList();
        
        Document levelXML = null;
        try {
            //Load the XML File.
            levelXML = xmlUtil.loadXMLDocument(level.getXmlFile(), PATH_LEVEL_SCHEMA);
        } catch (InvalidXMLFileFormatException ex) {
            Logger.getLogger(PathXDataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //The Parent XML Node of a sequence of child Nodes holding information on
        //each of this level's intersections.
        Node intersectionsList = levelXML.getElementsByTagName(INTERSECTIONS_LIST_TAG).item(0);
        ArrayList<Node> XMLNodes = xmlUtil.getChildNodesWithName(intersectionsList, INTERSECTION_TAG);
        
        //Iterate through the XML Nodes for each of the intersections.
        for (Node node : XMLNodes) {
            
            //Get info on the speciic intersection
            NamedNodeMap attrs = node.getAttributes();
            //This ID will be used to reference the corresponding Vertex in the Graph.
            int idNum = Integer.parseInt(attrs.getNamedItem(ID_ATT).getNodeValue());
            boolean open = Boolean.parseBoolean(attrs.getNamedItem(OPEN_ATT).getNodeValue());
            String initState;
            //Intersections indicated as open will initially start off green.
            if (open) 
                initState = PathXSpriteState.GREEN.toString();
            else 
                initState = PathXSpriteState.CLOSED.toString();
            
            int xPos = Integer.parseInt(attrs.getNamedItem(X_ATT).getNodeValue());
            int yPos = Integer.parseInt(attrs.getNamedItem(Y_ATT).getNodeValue());
            
            //Construct a SpriteType. The SpriteType will hold images for all possible
            //states of a PathXNode.
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
            
            SpriteType sT = new SpriteType(PathXConstants.NODE_TYPE);
            BufferedImage img = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_GREEN_INTERSECTION));
            sT.addState(PathXSpriteState.GREEN.toString(), img);
            img = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_GREEN_INTERSECTION_MOUSE_OVER));
            sT.addState(PathXSpriteState.GREEN_MOUSE_OVER.toString(), img);
            img = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_RED_INTERSECTION));
            sT.addState(PathXSpriteState.RED.toString(), img);
            img = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_RED_INTERSECTION_MOUSE_OVER));
            sT.addState(PathXSpriteState.RED_MOUSE_OVER.toString(), img);
            img = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_CLOSED_INTERSECTION));
            sT.addState(PathXSpriteState.CLOSED.toString(), img);
            img = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_CLOSED_INTERSECTION_MOUSE_OVER));
            sT.addState(PathXSpriteState.CLOSED_MOUSE_OVER.toString(), img);
            
            //Finish constructing the new PathXNode and add it to our temporary 
            //holding ArrayList.
            PathXNode newNode = new PathXNode(sT, xPos + GAME_VIEWPORT_X, yPos, 0, 0, initState, graph.getVertex(idNum));
            intersections.add(newNode);
        }
        
        //Update our global ArrayList of PathXNodes
        nodes = intersections;
    }

    //This method creates all the needed Road Sprites for the given level. These
    //roads are then added to the 
    public void constructRoads(PathXLevel level) {
        //Needed for loading and analyzing the PathXLevel's XML file.s
        XMLUtilities xmlUtil = new XMLUtilities();
        Graph graph = level.getGraph();
        ArrayList<Road> connections = new ArrayList();
        
        Document levelXML = null;
        try {
            //Load the XML File.
            levelXML = xmlUtil.loadXMLDocument(level.getXmlFile(), PATH_LEVEL_SCHEMA);
        } catch (InvalidXMLFileFormatException ex) {
            Logger.getLogger(PathXDataModel.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //The Parent XML Node of a sequence of child Nodes holding information on
        //each of this level's intersections.
        Node roadsList = levelXML.getElementsByTagName(ROAD_LIST_TAG).item(0);
        ArrayList<Node> XMLNodes = xmlUtil.getChildNodesWithName(roadsList, ROAD_TAG);
        
        //Iterate through the XML Nodes for each of the connections.
        for (Node node : XMLNodes) {
            
             //Get info on the speciic intersection
            NamedNodeMap attrs = node.getAttributes();
            
            //Get the indices of the PathXNodes/Vertices this road connects
            int nodeID1 = Integer.parseInt(attrs.getNamedItem(INT_ID1_ATT).getNodeValue());
            int nodeID2 = Integer.parseInt(attrs.getNamedItem(PathXFileManager.INT_ID2_ATT).getNodeValue());
            boolean oneWay = Boolean.parseBoolean(attrs.getNamedItem(ONE_WAY_ATT).getNodeValue());
            int speedLimit = Integer.parseInt(attrs.getNamedItem(SPEED_LIMIT_ATT).getNodeValue());
            
            //Create the road with this information.
            Road newRoad = new Road(nodes.get(nodeID1), nodes.get(nodeID2), oneWay, speedLimit, PathXSpriteState.OPEN.toString());
            connections.add(newRoad);   
        }
        
        roads = connections;
    }

    public ArrayList<CopCar> getCops() {
        return cops;
    }

    public ArrayList<BanditCar> getBandits() {
        return bandits;
    }

    public ArrayList<ZombieCar> getZombies() {
        return zombies;
    }

    public PlayerCar getPlayer() {
        return playerCar;
    }

    public void setPlayer(PlayerCar playerCar) {
        this.playerCar = playerCar;
    }

    public PathXLevel getCurrentLevel() {
        return currentLevel;
    }

    public void setCurrentLevel(PathXLevel currentLevel) {
        this.currentLevel = currentLevel;
    }

    
    public void constructPlayerCar(PathXLevel level) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        int x = level.getStartNode().getConstantXPos();
        int y = level.getStartNode().getConstantYPos();
        
        SpriteType sT = new SpriteType(PathXConstants.PLAYER_TYPE);
        BufferedImage img = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_PLAYER_CAR));
        sT.addState(VISIBLE.toString(), img);
        img = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_PLAYER_CAR_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        
        playerCar = new PlayerCar(sT, x, y, 0, 0, VISIBLE.toString(), level, null);
    }

//    private void initGameViewport() {
//        //Insets frameInsets = window.getInsets();
//        int screenWidth = WINDOW_WIDTH;
//        int screenHeight = WINDOW_HEIGHT;
//        gameViewport.setScreenSize(screenWidth, screenHeight);
//        
//        gameViewport.setGameWorldSize(screenWidth, screenHeight);
//        
//    }

    public Viewport getGameViewport() {
        return gameViewport;
    }

    public void setGameViewport(Viewport gameViewport) {
        this.gameViewport = gameViewport;
    }

    public void resetGameViewport() {
        int scrollX = gameViewport.getViewportX() * -1;
        int scrollY = gameViewport.getViewportY() * -1;
        gameViewport.scroll(scrollX, scrollY);
    }

    public void constructEnemyCars(PathXLevel level) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        
        //Images for each of the enemy car's and their states.
        BufferedImage copImage = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_COP_CAR));
        BufferedImage copMouseOverImage = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_COP_CAR_MOUSE_OVER));
        BufferedImage banditImage = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BANDIT_CAR));
        BufferedImage banditMouseOverImage = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BANDIT_CAR_MOUSE_OVER));
        BufferedImage zombieImage = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_ZOMBIE_CAR));
        BufferedImage zombieMouseOverImage = miniGame.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_ZOMBIE_CAR_MOUSE_OVER));
        SpriteType sT;
        
        ArrayList<PathXNode> nodes = getNodes();
        
        int numCops = level.getNumCops();
        int numBandits = level.getNumBandits();
        int numZombies = level.getNumZombies();
        
        //Used to get random numbers to randomly assign a starting node for each
        //enemy car.
        Random generator = new Random();
        
        //Keep track of all the nodes that are occupied by another car at the 
        //start of a level.
        boolean[] occupiedNode = new boolean[level.getGraph().size()];
        for (int i = 0; i < occupiedNode.length; i++){
            occupiedNode[i] = false;
        }
        
        //Nodes at index 0 and 1 are the start and ending nodes for the level 
        //so we cannot spawn cops on top of them.
        occupiedNode[0] = true;
        occupiedNode[1] = true;
        
        //We initialize this as 0 for now.
        int startNodeIndex = 0;
        
        
        
        //Construct all of the CopCars
        for (int i = 0; i < numCops; i++){
            sT = new SpriteType(PathXConstants.COP_CAR_TYPE.toString());
            sT.addState(VISIBLE.toString(), copImage);
            sT.addState(MOUSE_OVER.toString(), copMouseOverImage);
            
            //Update the starting node index.
            while (occupiedNode[startNodeIndex] == true)
                startNodeIndex = generator.nextInt(nodes.size());
            occupiedNode[startNodeIndex] = true;
            
            PathXNode startNode = nodes.get(startNodeIndex);
            int x = startNode.getConstantXPos();
            int y = startNode.getConstantYPos();
            
            CopCar newCop = new CopCar(sT, x, y, 0, 0, VISIBLE.toString(), level, startNode);
            newCop.setIntersection(startNode);

            cops.add(newCop);
        }
        
        //Construct all of the BanditCars
        for (int i = 0; i < numBandits; i++){
            sT = new SpriteType(PathXConstants.COP_CAR_TYPE.toString());
            sT.addState(VISIBLE.toString(), banditImage);
            sT.addState(MOUSE_OVER.toString(), banditMouseOverImage);
            
            //Update the starting node index.
            while (occupiedNode[startNodeIndex] == true)
                startNodeIndex = generator.nextInt(nodes.size());
            occupiedNode[startNodeIndex] = true;
            
            PathXNode startNode = nodes.get(startNodeIndex);
            int x = startNode.getConstantXPos();
            int y = startNode.getConstantYPos();
            
            BanditCar newBandit = new BanditCar(sT, x, y, 0, 0, VISIBLE.toString(), level, startNode);
            newBandit.setIntersection(startNode);

            bandits.add(newBandit);
        }
        
        //Construct all of the ZombieCars
        for (int i = 0; i < numZombies; i++){
            sT = new SpriteType(PathXConstants.COP_CAR_TYPE.toString());
            sT.addState(VISIBLE.toString(), zombieImage);
            sT.addState(MOUSE_OVER.toString(), zombieMouseOverImage);
            
            //Update the starting node index.
            while (occupiedNode[startNodeIndex] == true)
                startNodeIndex = generator.nextInt(nodes.size());
            occupiedNode[startNodeIndex] = true;
            
            PathXNode startNode = nodes.get(startNodeIndex);
            int x = startNode.getConstantXPos();
            int y = startNode.getConstantYPos();
            
            ZombieCar newZombie = new ZombieCar(sT, x, y, 0, 0, VISIBLE.toString(), level, startNode);
            newZombie.setIntersection(startNode);

            zombies.add(newZombie);
        }
    }
}
