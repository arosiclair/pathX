/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import graph.Vertex;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import pathX.*;
import mini_game.*;
import java.util.ArrayList;

/**
 * This node class represents intersections in-game and will manage nodes in a
 * corresponding levels graph.
 * @author Andrew
 */
public class PathXNode extends Sprite {
    
    //The associated vertex in the level's Graph data structure.
    private Vertex vertex;
    
    //We use these constant globals for a reference point when rendering in comparison to
    //a Viewport. Do NOT use getX or getY for rendering as those values will change
    //constantly as the Viewport changes.
    private final int constantXPos, constantYPos;
    
    //A list of roads this Node is connected to.
    private ArrayList<Road> roads;
    
    private ArrayList<PathXNode> neighbors;
    
    //The current state of this node be it GREEN, RED or CLOSED.
    private String currentState;
    
    public PathXNode(SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, Vertex initVertex){
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        constantXPos = (int) initX;
        constantYPos = (int) initY;
        currentState = initState;
        vertex = initVertex;
        
        neighbors = new ArrayList();
    }
    
//    public PathXNode(Sprite s, String state, ArrayList<Road> roads){
//        this(s, state);
//        this.roads = roads;
//    }
    
    public void makeGreen(){
        setCurrentState(PathXSpriteState.GREEN.toString());
    }
    
    public void makeRed(){
        setCurrentState(PathXSpriteState.RED.toString());
    }
    
    public void close(){
        setCurrentState(PathXSpriteState.CLOSED.toString());       
    }
    
    public boolean hasRoad(Road r){
        for (Road road : roads){
            if (road == r) return true;
        }
        return false;
    }
    
    public void addNeighbor(PathXNode neighbor){
        neighbors.add(neighbor);
    }

    public String getCurrentState() {
        return currentState;
    }

    public void setRoads(ArrayList<Road> roads) {
        this.roads = roads;
    }

    public void setCurrentState(String currentState) {
        this.currentState = currentState;
    }

    public Vertex getVertex() {
        return vertex;
    }

    public int getConstantXPos() {
        return constantXPos;
    }

    public int getConstantYPos() {
        return constantYPos;
    }
    
}
