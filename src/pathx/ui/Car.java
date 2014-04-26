/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import graph.VertexNotFoundException;
import java.util.ArrayList;
import pathx.ui.PathXNode;
import mini_game.Sprite;
import mini_game.SpriteType;
import pathx.data.PathXLevel;

/**
 * This Car class lays out the template for any other cars to be used in the game.
 * This Car class is what the player will be using as his/her get away car. 
 * Enemy cars in game will be descendants of this class.
 * @author Andrew Rosiclair
 */
public abstract class Car extends Sprite{
    
    //The level this Car belongs to.
    private PathXLevel level;
    
    //Speed of the sprite
    private double speed;
    
    
    //Intersection the sprite can be at at any time
    private PathXNode intersection;

    //The current state of the car. This is mostly used for special status.
    //private String currentState;
    boolean movingToTarget;
    
    //Path to take;
    private ArrayList<PathXNode> path;
    private int pathIndex;
    
    //Coordinates of the target this sprite will be moving to.
    float targetX, targetY;
    
    public Car(SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXLevel level, PathXNode startSpot){
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        intersection = startSpot;
        movingToTarget = false;
        this.level = level;
    }
    
    //This method will be overidden and defined for other, AI-driven cars in game.
    public abstract ArrayList<PathXNode> generatePath() throws VertexNotFoundException;
    
    public void changeDestination(ArrayList<PathXNode> path){
        this.setPath(path);
        setPathIndex(0);
    }
    
    /**
     * Mutator method for setting bot the x-axis and y-axis target
     * coordinates for this tile.
     * 
     * @param initTargetX The x-axis target coordinate to move this
     * tile towards.
     * 
     * @param initTargetY The y-axis target coordinate to move this
     * tile towards.
     */
    public void setTarget(float initTargetX, float initTargetY) 
    {
        targetX = initTargetX; 
        targetY = initTargetY;
    }
    
    /**
     * Allows the tile to start moving by initializing its properly
     * scaled velocity vector pointed towards it target coordinates.
     * 
     * @param maxVelocity The maximum velocity of this tile, which
     * we'll then compute the x and y axis components for taking into
     * account the trajectory angle.
     */
    public void startMovingToTarget(int maxVelocity)
    {
        // LET ITS POSITIONG GET UPDATED
        movingToTarget = true;
        
        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = targetX - x;
        float diffY = targetY - y;
        float tanResult = diffY/diffX;
        float angleInRadians = (float)Math.atan(tanResult);
        
        // COMPUTE THE X VELOCITY COMPONENT
        vX = (float)(maxVelocity * Math.cos(angleInRadians));
        
        // CLAMP THE VELOCTY IN CASE OF NEGATIVE ANGLES
        if ((diffX < 0) && (vX > 0)) vX *= -1;
        if ((diffX > 0) && (vX < 0)) vX *= -1;
        
        // COMPUTE THE Y VELOCITY COMPONENT
        vY = (float)(maxVelocity * Math.sin(angleInRadians));        
        
        // CLAMP THE VELOCITY IN CASE OF NEGATIVE ANGLES
        if ((diffY < 0) && (vY > 0)) vY *= -1;
        if ((diffY > 0) && (vY < 0)) vY *= -1;
    }
    
    /**
     * Accessor method for getting whether this tile is currently moving toward
     * target coordinates or not.
     * 
     * @return true if this tile is currently moving toward target coordinates,
     * false otherwise.
     */
    public boolean isMovingToTarget() 
    { 
        return movingToTarget; 
    }
    
    public double getSpeed() {
        return speed;
    }

    public PathXNode getIntersection() {
        return intersection;
    }

    public ArrayList<PathXNode> getPath() {
        return path;
    }

    public int getPathIndex() {
        return pathIndex;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setIntersection(PathXNode intersection) {
        this.intersection = intersection;
    }

    public void setPath(ArrayList<PathXNode> path) {
        this.path = path;
    }

    public void setPathIndex(int pathIndex) {
        this.pathIndex = pathIndex;
    }

    public PathXLevel getLevel() {
        return level;
    }
}
