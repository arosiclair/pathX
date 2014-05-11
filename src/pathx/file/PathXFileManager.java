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
import static pathx.PathXConstants.VIEWPORT_Y;
import pathx.ui.PathXSpriteState;
import static pathx.ui.PathXSpriteState.COMPLETED;
import static pathx.ui.PathXSpriteState.COMPLETED_MOUSE_OVER;
import static pathx.ui.PathXSpriteState.INCOMPLETE;
import static pathx.ui.PathXSpriteState.INCOMPLETE_MOUSE_OVER;
import static pathx.ui.PathXSpriteState.LOCKED;
import static pathx.ui.PathXSpriteState.LOCKED_MOUSE_OVER;

/**
 *
 * @author Andrew
 */
public class PathXFileManager {
    
    private PathXMiniGame game;
    private PathXDataModel data;
    
    public static final String LEVEL_NODE_TAG = "level";
    public static final String INTERSECTIONS_LIST_TAG = "intersections";
    public static final String NAME_ATT = "name";
    public static final String IMAGE_ATT = "image";
    public static final String INTERSECTION_TAG = "intersection";
    public static final String ID_ATT = "id";
    public static final String OPEN_ATT = "open";
    public static final String X_ATT = "x";
    public static final String Y_ATT = "y";
    public static final String INT_ID1_ATT = "int_id1";
    public static final String INT_ID2_ATT = "int_id2";
    public static final String SPEED_LIMIT_ATT = "speed_limit";
    public static final String ONE_WAY_ATT = "one_way";
    public static final String AMOUNT_ATT = "amount";
    public static final String NUM_ATT = "num";
    public static final String MONEY_TAG = "money";
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
        
        //For now, create a new record each session with no money, unlocked levels, or specials
        PathXRecord newRecord = new PathXRecord();
        newRecord.setBalance(0);
        newRecord.setLevels(data.getLevels());
        HashMap<String, Boolean> newSpecials = data.createSpecialsMap();
        newRecord.setSpecials(newSpecials);
        data.setRecord(newRecord);
        loadLevels();
        
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
        int i = 0;
        String[] levelNames = new String[20];
        PathXLevel newLevel = null;
        PathXLevel previous = null;
        for (String s : levelDetails){
            try {
                previous = newLevel;
                Document levelDoc = xmlUtil.loadXMLDocument(PATH_DATA + s, PATH_LEVEL_SCHEMA);
                String path = PATH_DATA + s;
                newLevel = createLevel(path, levelDoc, xmlUtil, previous);
                levels.put(newLevel.getLevelName(), newLevel);
                levelNames[i] = newLevel.getLevelName();
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
    
    private PathXLevel createLevel(String path, Document levelDoc, XMLUtilities xmlUtil, PathXLevel previousLevel){

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
        PathXLevel newLevel = new PathXLevel(levelName, path, bgImageName, 
                newGraph, reward, LEVEL_X_POS[levelsLoaded], LEVEL_Y_POS[levelsLoaded],
                data, previousLevel);
        
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
        
        //Add the necessary images for displaying whether or not the level is 
        //locked incomplete or completed.
        SpriteType sT = new SpriteType(PathXConstants.LEVEL_TYPE);
        BufferedImage[] images = game.getLevelNodeImage(PathXConstants.LOCKED_LEVEL_TYPE);
        sT.addState(LOCKED.toString(), images[0]);
        sT.addState(LOCKED_MOUSE_OVER.toString(), images[1]);
        images = game.getLevelNodeImage(PathXConstants.INCOMPLETE_LEVEL_TYPE);
        sT.addState(INCOMPLETE.toString(), images[0]);
        sT.addState(INCOMPLETE_MOUSE_OVER.toString(), images[1]);
        images = game.getLevelNodeImage(PathXConstants.COMPLETE_LEVEL_TYPE);
        sT.addState(COMPLETED.toString(), images[0]);
        sT.addState(COMPLETED_MOUSE_OVER.toString(), images[1]);
        String state;
        
        //Assign a state based on the completion of the previous level.
        if (newLevel.getPrevious() != null && !newLevel.getPrevious().isCompleted())
            state = PathXSpriteState.LOCKED.toString();
        else if (!newLevel.isCompleted())
            state = PathXSpriteState.INCOMPLETE.toString();
        else
            state = PathXSpriteState.COMPLETED.toString();
        PathXLevelSprite newSprite = new PathXLevelSprite(sT, newLevel.getxPos(), newLevel.getyPos() - VIEWPORT_Y, 0, 0, state, newLevel, game);
        
        //Set the event handler
//        newSprite.setActionCommand(newSprite.getName());
//        newSprite.setActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent ae)
//            {   game.getEventHandler().switchToGameScreen(this.);    }
//        });
        
        return newSprite;
    }


}
