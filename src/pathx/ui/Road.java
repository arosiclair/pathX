/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import mini_game.Sprite;

/**
 *
 * @author Andrew
 */
public class Road {
    
    //Speed limit that cars will be restricted to.
    private double speedLimit;
    
    //State of the Road which can either be OPEN or CLOSED.
    private String currentState;
    
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
        currentState = PathXSpriteState.OPEN.toString();
    }
    
    //Close the road for use.
    public void close(){
        currentState = PathXSpriteState.CLOSED.toString();
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
}

