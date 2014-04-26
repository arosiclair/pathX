/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import graph.Graph;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import pathx.ui.BanditCar;
import pathx.ui.Car;
import pathx.ui.CopCar;
import pathx.ui.PathXLevelSprite;
import pathx.ui.PathXMiniGame;
import pathx.ui.PathXNode;
import pathx.ui.PathXSpriteState;
import pathx.ui.Road;
import pathx.ui.ZombieCar;
import xml_utilities.XMLUtilities;

/**
 *
 * @author Andrew
 */
public class PathXDataModel extends MiniGameDataModel{
    
    private PathXMiniGame miniGame;
    private String currentLevel;
    private PathXLevel level;
    private PathXRecord record;
    
    private Car player;
    
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
    
    private ArrayList<PathXLevelSprite> levelSprites;
    
     public PathXDataModel(PathXMiniGame initMiniGame){
        miniGame = initMiniGame;
        record = new PathXRecord();
        
        cops = new ArrayList();
        bandits = new ArrayList();
        zombies = new ArrayList();
        
        currentLevel = null;
        
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
    //are put into the nodes ArrayList.
    public void constructNodes(PathXLevel level) {
        //Needed for loading and analyzing the PathXLevel's XML file.s
        XMLUtilities xmlUtil = new XMLUtilities();
        Graph graph = level.getGraph();
        ArrayList<PathXNode> intersections = new ArrayList();
        
        //Load the XML File.
        
    }
}
