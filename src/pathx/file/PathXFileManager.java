/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.file;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import pathx.data.PathXDataModel;
import pathx.data.PathXRecord;
import pathx.ui.PathXMiniGame;
import properties_manager.PropertiesManager;
import pathx.PathX.PathXPropertyType;
import static pathx.PathXConstants.PATH_DATA;
import pathx.data.PathXLevel;
import pathx.ui.PathXLevelSprite;
import xml_utilities.XMLUtilities;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import static pathx.PathXConstants.PATH_LEVEL_SCHEMA;
import xml_utilities.InvalidXMLFileFormatException;
import graph.Graph;
import graph.Vertex;
import java.awt.image.BufferedImage;
import mini_game.SpriteType;
import pathx.PathXConstants;
import static pathx.PathXConstants.LEVEL_X_POS;
import static pathx.PathXConstants.LEVEL_Y_POS;
import static pathx.PathXConstants.NODE_TYPE;
import pathx.ui.PathXNode;
import pathx.ui.PathXSpriteState;
import static pathx.ui.PathXSpriteState.INVISIBLE;
import static pathx.ui.PathXSpriteState.MOUSE_OVER;
import static pathx.ui.PathXSpriteState.VISIBLE;

/**
 *
 * @author Andrew
 */
public class PathXFileManager {
    
    private PathXMiniGame game;
    private PathXDataModel data;
    
    public static final String LEVEL_NODE_TAG = "level";
    public static final String INTERSECTIONS_LIST_TAG = "intersections";
    public final String NAME_ATT = "name";
    public final String IMAGE_ATT = "image";
    public final String INTERSECTION_TAG = "intersection";
    public final String ID_ATT = "id";
    public final String OPEN_ATT = "open";
    public static final String X_ATT = "x";
    public static final String Y_ATT = "y";
    public static final String INT_ID1_ATT = "int_id1";
    public static final String INT_ID2_ATT = "int_id2";
    public static final String SPEED_LIMIT_ATT = "speed_limit";
    public static final String ONE_WAY_ATT = "one_way";
    public static final String AMOUNT_ATT = "amount";
    public static final String NUM_ATT = "num";
    public String MONEY_TAG = "money";
    public static final String POLICE_TAG = "police";
    public static final String BANDITS_TAG = "bandits";
    public static final String ZOMBIES_TAG = "zombies";
    
    private int levelsLoaded = 0;
    public static final String START_INTERSECTION_TAG = "start_intersection";
    public static final String DESTINATION_INTERSECTION_TAG = "destination_intersection";
    public static final String ROAD_LIST_TAG = "roads";
    public static final String ROAD_TAG = "road";
    
    
    public PathXFileManager(PathXMiniGame miniGame){
        game = miniGame;
        data = (PathXDataModel)game.getDataModel();
    }
    
    public void saveRecord(PathXRecord record){
        //@TODO Implement game stats saving.
    }
    
    public void loadRecord(){
        //@TODO Implement game record loading.
    }
    
    public void loadLevels(){
        //The Level details will be saved in a dedicated XML file
        //Details will include it's location on the level select map, as well
        //as its name and reward.
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        XMLUtilities xmlUtil = new XMLUtilities();
        
        //Create HashMaps to store the newly created PathXLevels and their sprites
        HashMap<String, PathXLevel> levels = new HashMap();
        ArrayList<PathXLevelSprite> levelSprites = new ArrayList();
        
        //Iterate through the XML file to find details on each level available.
        //These details are loaded into a new PathXLevel and then put into the 
        //levels HashMap.
        //Another ArrayList of PathXLevelSprites will also be filled. These sprites
        //are used for level select map rendering.
        ArrayList<String> levelDetails = props.getPropertyOptionsList(PathXPropertyType.LEVEL_OPTIONS);
        StringTokenizer st;
        for (String s : levelDetails){
            try {
                Document levelDoc = xmlUtil.loadXMLDocument(PATH_DATA + s, PATH_LEVEL_SCHEMA);
                PathXLevel newLevel = createLevel(levelDoc, xmlUtil);
                levels.put(newLevel.getLevelName(), newLevel);
                PathXLevelSprite newSprite = createLevelSprite(newLevel);
                levelSprites.add(newSprite);
                
            } catch (InvalidXMLFileFormatException ex) {
                Logger.getLogger(PathXFileManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        //Give the HashMap of levels to the DataModel.
        data.setLevels(levels);
        //Give the ArrayList of the associated sprites to the DataModel
        data.setLevelSprites(levelSprites);
    }
    
    private PathXLevel createLevel(Document levelDoc, XMLUtilities xmlUtil){

        //Get the level name and background Image.
        Node levelNode = levelDoc.getElementsByTagName(LEVEL_NODE_TAG).item(0);
        NamedNodeMap attrs = levelNode.getAttributes();
        String levelName = attrs.getNamedItem(NAME_ATT).getNodeValue();
        String bgImageName = attrs.getNamedItem(IMAGE_ATT).getNodeValue();
        
        // LOAD THE START INTERSECTION
        Node startIntNode = levelDoc.getElementsByTagName(START_INTERSECTION_TAG).item(0);
        attrs = startIntNode.getAttributes();
        String startImage = attrs.getNamedItem(IMAGE_ATT).getNodeValue();
        
        //LOAD THE END INTERSECTION
        Node endIntNode = levelDoc.getElementsByTagName(DESTINATION_INTERSECTION_TAG).item(0);
        attrs = endIntNode.getAttributes();
        String destinationImage = attrs.getNamedItem(IMAGE_ATT).getNodeValue();
        
        //Reward amount
        Node moneyNode = levelDoc.getElementsByTagName(MONEY_TAG).item(0);
        attrs = moneyNode.getAttributes();
        int reward = Integer.parseInt(attrs.getNamedItem(AMOUNT_ATT).getNodeValue());
        
        //Number of police
        Node policeNode = levelDoc.getElementsByTagName(POLICE_TAG).item(0);
        attrs = policeNode.getAttributes();
        int numPolice = Integer.parseInt(attrs.getNamedItem(NUM_ATT).getNodeValue());
        
        //Number of bandits
        Node banditsNode = levelDoc.getElementsByTagName(BANDITS_TAG).item(0);
        attrs = banditsNode.getAttributes();
        int numBandits = Integer.parseInt(attrs.getNamedItem(NUM_ATT).getNodeValue());
        
        //Number of zombies
        Node zombiesNode = levelDoc.getElementsByTagName(ZOMBIES_TAG).item(0);
        attrs = zombiesNode.getAttributes();
        int numZombies = Integer.parseInt(attrs.getNamedItem(NUM_ATT).getNodeValue());

        
        //Construct the graph
        Graph newGraph = new Graph();
        
        //Load the intersections/vertices.
        Node intersectionsList = levelDoc.getElementsByTagName(INTERSECTIONS_LIST_TAG).item(0);
        ArrayList<Node> intersections = xmlUtil.getChildNodesWithName(intersectionsList, INTERSECTION_TAG); 
        for (Node intersection : intersections){
//            NamedNodeMap attrs = intersection.getAttributes();
//            int idNum = Integer.parseInt(attrs.getNamedItem(ID_ATT).getNodeValue());
//            boolean open = Boolean.parseBoolean(attrs.getNamedItem(OPEN_ATT).getNodeValue());
//            int xPos = Integer.parseInt(attrs.getNamedItem(X_ATT).getNodeValue());
//            int yPos = Integer.parseInt(attrs.getNamedItem(Y_ATT).getNodeValue());
//            
//            //Creat a PathXNode for the level.
//            SpriteType st = createIntersectionSpriteType();
//            PathXNode newNode = new PathXNode();
            
            //Create the new Vertex and add it to the graph.
            Vertex newVertex = new Vertex();
            newGraph.addVertex(newVertex);
        }
        
        //Load the edges of the graph
        Node roadsListNode = levelDoc.getElementsByTagName(ROAD_LIST_TAG).item(0); 
        ArrayList<Node> roadsList = xmlUtil.getChildNodesWithName(roadsListNode, ROAD_TAG);
        for (Node road : roadsList){
            
            NamedNodeMap attributes = road.getAttributes();
            int nodeIndex1 = Integer.parseInt(attributes.getNamedItem(INT_ID1_ATT).getNodeValue());
            int nodeIndex2 = Integer.parseInt(attributes.getNamedItem(INT_ID2_ATT).getNodeValue());
            boolean oneWay = Boolean.parseBoolean(attributes.getNamedItem(ONE_WAY_ATT).getNodeValue());
            Vertex v1 = newGraph.getVertex(nodeIndex1);
            Vertex v2 = newGraph.getVertex(nodeIndex2);
            
            //Make the appropriate connections
            if (oneWay){
                v1.addNeighbor(v2);
            }else{
                v1.addNeighbor(v2);
                v2.addNeighbor(v1);
            }
            
            
        }

        //Create the PathXLevel and add all relevant information.
        PathXLevel newLevel = new PathXLevel(levelName, bgImageName, newGraph, reward, LEVEL_X_POS[levelsLoaded], LEVEL_Y_POS[levelsLoaded], data);
        levelsLoaded++;
        newLevel.setNumCops(numPolice);
        newLevel.setNumBandits(numBandits);
        newLevel.setNumZombies(numZombies);
        newLevel.setStartNodeImage(startImage);
        newLevel.setDestinationNodeImage(destinationImage);
        
        return newLevel;
    }

    private PathXLevelSprite createLevelSprite(PathXLevel newLevel) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        
        SpriteType sT = new SpriteType(PathXConstants.INCOMPLETE_LEVEL_TYPE);
        BufferedImage img = game.getLevelNodeImage(PathXConstants.INCOMPLETE_LEVEL_TYPE);
        sT.addState(VISIBLE.toString(), img);
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INCOMPLETE_LEVEL_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        
        PathXLevelSprite newSprite = new PathXLevelSprite(sT, newLevel.getxPos(), newLevel.getyPos(), 0, 0, INVISIBLE.toString(), newLevel, game);
        
        return newSprite;
    }

}