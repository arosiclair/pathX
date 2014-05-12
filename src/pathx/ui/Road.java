/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import graph.Vertex;
import graph.VertexNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mini_game.Sprite;

/**
 *
 * @author Andrew
 */
public class Road {
    
    //Speed limit that cars will be restricted to.
    private double speedLimit;
    
    //State of the Road which can either be OPEN, CLOSED, or a highlighted 
    //version of those two.
    protected String currentState;
    
    //Associated sprite.
    //private Sprite s;
    
//    private float xPos = s.getX();
//    private float yPos = s.getY();
    
    private boolean oneWay;
    
    //Connecting nodes. If this road is one way, the direction is indicated by
    // and restricted to: PathXNode n1 to PathXNode n2.
    private PathXNode n1, n2;
    
    public Road(PathXNode node1, PathXNode node2, boolean oneWay, double speedLimit, String state){
        //this.s = s;
        n1 = node1;
        n2 = node2;
        this.oneWay = oneWay;
        this.speedLimit = speedLimit;
        currentState = state;
    }
    
    //Open the road for use.
    public void open(){
        setState(PathXSpriteState.OPEN.toString());
        
        Vertex v1 = n1.getVertex();
        Vertex v2 = n2.getVertex();
        v1.addNeighbor(v2);
        v2.addNeighbor(v1);
    }
    
    //Close the road for use.
    public void close(){
        setState(PathXSpriteState.CLOSED.toString());
        
        
        Vertex v1 = n1.getVertex();
        Vertex v2 = n2.getVertex();
        try {
            v1.removeNeighbor(v2);
            v2.removeNeighbor(v1);
        } catch (VertexNotFoundException ex) {
            Logger.getLogger(Road.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    //Activated by a special. Increases the speed limit by 50%
    public void increaseSpeedLimit(){
        speedLimit += (getSpeedLimit() * .50);
    }
    
    //Activated by a special. Decreases the speed limit by 50%
    public void decreaseSpeedLimit(){
        speedLimit -= (getSpeedLimit() * .50);
    }

    public double getSpeedLimit() {
        return speedLimit;
    }

    public String getCurrentState() {
        return currentState;
    }
    
//    public PathXNode[] getPathXNodes(){
//        PathXNode[] nodes = {getN1(), getN2()};
//        return nodes;
//    }

    public boolean isOneWay() {
        return oneWay;
    }

    public PathXNode[] getNodes() {
        return new PathXNode[]{n1, n2};
    }

    public void setState(String currentState) {
        this.currentState = currentState;
    }
}

