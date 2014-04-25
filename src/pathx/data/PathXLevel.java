/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import graph.Graph;
import mini_game.Viewport;
import static pathx.PathXConstants.VIEWPORT_X;
import static pathx.PathXConstants.VIEWPORT_Y;
import pathx.ui.PathXNode;

/**
 * This class will be used to hold information for every level in the pathX 
 * Mini-Game. Notably, the graph data structure will be stored here.
 * @author Andrew
 */
public class PathXLevel {
    
    private String levelName;
    
    //The directory to the background image to be loaded.
    private String bgImage, startNodeImage, destinationNodeImage;
    
    private int reward;
    //Coordinates of the level on the level select map.
    private int xPos, yPos;
    
    //The graph data structure.
    private Graph graph;
    
    private PathXNode startNode;
    private PathXDataModel dataModel;
    
    private int numCops, numBandits, numZombies;
    
    private boolean completed;
    
    
    public PathXLevel(String levelName, String bgImage, Graph graph, int reward, int xPos, int yPos, PathXDataModel data){
        this.dataModel = data;
        this.levelName = levelName;
        this.reward = reward;
        this.xPos = xPos;
        this.yPos = yPos;
        
        this.graph = graph;
        this.bgImage = bgImage;
        
        this.completed = false;
    }
//    public ArrayList<Node> findPath(Node from, Node to){{
//        
//    }
    
//    public boolean hasNode(Node node){
//        
//    }
    
    public PathXNode getStartNode(){
        return startNode;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getReward() {
        return reward;
    }

    public int getxPos() {
        return VIEWPORT_X + xPos - dataModel.getViewport().getViewportX();
    }

    public int getyPos() {
        return VIEWPORT_Y + yPos - dataModel.getViewport().getViewportY();
    }

    public boolean isCompleted() {
        return completed;
    }

    public String getBgImage() {
        return bgImage;
    }

    public void setBgImage(String bgImage) {
        this.bgImage = bgImage;
    }

    public int getNumCops() {
        return numCops;
    }

    public int getNumBandits() {
        return numBandits;
    }

    public int getNumZombies() {
        return numZombies;
    }

    public void setNumCops(int numCops) {
        this.numCops = numCops;
    }

    public void setNumBandits(int numBandits) {
        this.numBandits = numBandits;
    }

    public void setNumZombies(int numZombies) {
        this.numZombies = numZombies;
    }

    public String getStartNodeImage() {
        return startNodeImage;
    }

    public String getDestinationNodeImage() {
        return destinationNodeImage;
    }

    public void setStartNodeImage(String startNodeImage) {
        this.startNodeImage = startNodeImage;
    }

    public void setDestinationNodeImage(String destinationNodeImage) {
        this.destinationNodeImage = destinationNodeImage;
    }

    public Graph getGraph() {
        return graph;
    }
}
