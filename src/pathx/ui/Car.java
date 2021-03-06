/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import graph.VertexNotFoundException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.PathXConstants;
import static pathx.PathXConstants.GAME_VIEWPORT_X;
import static pathx.PathXConstants.GAME_VIEWPORT_Y;
import pathx.data.PathXDataModel;
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
    private double maxSpeed;
    
    
    //We use these constant globals for a reference point when rendering in comparison to
    //a Viewport. Do NOT use getX or getY for rendering as those values will change
    //constantly as the Viewport changes.
    private int constantXPos, constantYPos;
    
    //Intersection the sprite can be at at any time
    private PathXNode intersection;

    //The current state of the car. This is mostly used for special status.
    //private String currentState;
    protected boolean movingToTarget;
    
    //Path to take;
    protected ArrayList<PathXNode> path;
    private int pathIndex;
    
    //Coordinates of the target this sprite will be moving to.
    protected float targetX;
    protected float targetY;
    
    private GregorianCalendar specialTimer;
    private String specialState;
    private boolean stolen;
    
    public Car(SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXLevel level, PathXNode startSpot){
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        constantXPos = (int) initX;
        constantYPos = (int) initY;
        intersection = startSpot;
        movingToTarget = false;
        this.level = level;
        maxSpeed = 2;
        speed = maxSpeed;
        specialState = "";
        stolen = false;
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
    public void startMovingToTarget(double maxVelocity, Viewport gameVP)
    {
        
        // LET ITS POSITIONG GET UPDATED
        setMovingToTarget(true);
        
        // CALCULATE THE ANGLE OF THE TRAJECTORY TO THE TARGET
        float diffX = getTargetX() - constantXPos;
        float diffY = getTargetY() - constantYPos;
        float tanResult = diffY/diffX;
        float angleInRadians = (float) Math.atan(tanResult);
        
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
     * This method calculates the distance from this tile's current location
     * to the target coordinates on a direct line.
     * 
     * @return The total distance on a direct line from where the tile is
     * currently, to where its target is.
     */
    public float calculateDistanceToTarget()
    {
        // GET THE X-AXIS DISTANCE TO GO
        float diffX = getTargetX() - constantXPos;
        
        // AND THE Y-AXIS DISTANCE TO GO
        float diffY = getTargetY() - constantYPos;
        
        // AND EMPLOY THE PYTHAGOREAN THEOREM TO CALCULATE THE DISTANCE
        float distance = (float)Math.sqrt((diffX * diffX) + (diffY * diffY));
        
        // AND RETURN THE DISTANCE
        return distance;
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
    
    public void decreaseMaxSpeed(){
        setMaxSpeed(getMaxSpeed() * 0.90);
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

    public int getConstantXPos() {
        return constantXPos;
    }

    public int getConstantYPos() {
        return constantYPos;
    }
    
    @Override
    public void update(MiniGame game){
        PathXDataModel dataModel = (PathXDataModel) game.getDataModel();
        Viewport gameVP = dataModel.getGameViewport();
        //If the game hasn't started than do nothing
        if (!dataModel.inProgress()){
            return;
        }
        
        //Check if this car's tires were flattened or if its gas was emptied.
        //If enough time has past since the tires were flattened or the gas
        //emptied, then the specialState and timer get reset and the car is allowed
        //to update.
        //MindlessTerror will affect how each enemy Car's generatePath() method 
        //will function. We keep track of the time for MindlessTerror here though.
        if (!specialState.equals("")){
            if (getSpecialState().equals(PathXConstants.FLAT_TIRE_SPECIAL_TYPE)){
                GregorianCalendar newTime = new GregorianCalendar();
                long timePast = newTime.getTimeInMillis() - getSpecialTimer().getTimeInMillis();
                if((timePast/1000) <= 10)
                    return;
                else{
                    setSpecialState("");
                    setSpecialTimer(null);
                }
            }else if (getSpecialState().equals(PathXConstants.EMPTY_GAS_SPECIAL_TYPE)){
                GregorianCalendar newTime = new GregorianCalendar();
                long timePast = newTime.getTimeInMillis() - getSpecialTimer().getTimeInMillis();
                if((timePast/1000) <= 20)
                    return;
                else{
                    setSpecialState("");
                    setSpecialTimer(null);
                }
            } else if (getSpecialState().equals(PathXSpriteState.INCAPACITATED.toString())) {
                GregorianCalendar newTime = new GregorianCalendar();
                long timePast = newTime.getTimeInMillis() - getSpecialTimer().getTimeInMillis();
                if ((timePast / 1000) <= 20) 
                    return;
                else {
                    setSpecialState("");
                    setSpecialTimer(null);
                }
            }
        }
        
        //Begin moving the car through its path if there are Nodes to go to.
        if (path != null && !path.isEmpty()){

            speed = maxSpeed;
            
            //IF THIS TILE IS ALMOST AT ITS TARGET DESTINATION,
            // JUST GO TO THE TARGET AND THEN STOP MOVING
            if (calculateDistanceToTarget() <= speed){
                vX = 0;
                vY = 0;
                constantXPos = (int) targetX;
                constantYPos = (int) targetY;
                
                intersection = path.get(0);
                

                //If we just reached a red light then stop the car
                if(intersection.getState().indexOf("RED") >= 0 && 
                        !specialState.equals(PathXConstants.MINDLESS_TERROR_SPECIAL_TYPE)){
                    speed = 0;
                    movingToTarget = false;
                    return;
                }
                    
                    
                //Remove the Node that we just reached as it is no longer needed.
                //This behavior just replicates a Queue.
                PathXNode last = path.remove(0);
                speed = maxSpeed;
                
                //Un-highlight this node if it was highlighted from the player's
                //path
                if (this instanceof PlayerCar && last.getState().indexOf("_HIGHLIGHTED") >= 0)
                    last.setState(last.getState().substring(0, last.getState().indexOf("_HIGHLIGHTED")));
                
                //If we have emptied the path list, indicating that we've reached,
                //the target, then we can stop moving.
                if (path.isEmpty()){
                    //speed = 0;
                    vX = 0;
                    vY = 0;
                    targetX = 0;
                    targetY = 0;
                    movingToTarget = false;
                    return;
                }

                
                //targetX = GAME_VIEWPORT_X + path.get(0).getConstantXPos() - gameVP.getViewportX();
                targetX = path.get(0).getConstantXPos();
                //targetY = GAME_VIEWPORT_Y + path.get(0).getConstantYPos() - gameVP.getViewportY();
                targetY = path.get(0).getConstantYPos();
                
                //Start moving to the target that we just defined. "2" is a 
                //place holder speed for now.
                startMovingToTarget(speed, gameVP);
                //speed = getMaxSpeed();
                
            }
            
            else{
//                targetX = path.get(0).getConstantXPos();
//                targetY = path.get(0).getConstantYPos();
                
                //if (vX == 0 && vX == 0)
                startMovingToTarget(speed, gameVP);
                //speed = getMaxSpeed();
                constantXPos += vX;
                constantYPos += vY;
            }
        }

    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public void setMovingToTarget(boolean movingToTarget) {
        this.movingToTarget = movingToTarget;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
    
    public String getSpecialState() {
        return specialState;
    }

    public void setSpecialState(String specialState) {
        this.specialState = specialState;
    }

    /**
     * Prevents the car from moving for 10 seconds.
     */
    public void flattenTires() {
        setSpecialTimer(new GregorianCalendar());
        setSpecialState(PathXConstants.FLAT_TIRE_SPECIAL_TYPE);
    }
    
    public void emptyGas(){
        if (specialState.equals("")){
            setSpecialTimer(new GregorianCalendar());
            setSpecialState(PathXConstants.EMPTY_GAS_SPECIAL_TYPE);
        }
    }

    void mindlessTerror() {
        if (specialState.equals("")){
            //specialTimer = new GregorianCalendar();
            setSpecialState(PathXConstants.MINDLESS_TERROR_SPECIAL_TYPE);
        }
    }


    public void incapacitate() {
        if (specialState.equals("")){
            setSpecialTimer(new GregorianCalendar());
            setSpecialState(PathXSpriteState.INCAPACITATED.toString());
        }
    }

    public void setSpecialTimer(GregorianCalendar specialTimer) {
        this.specialTimer = specialTimer;
    }

    public GregorianCalendar getSpecialTimer() {
        return specialTimer;
    }

    void setStolen(boolean b) {
        stolen = b;
    }

    public boolean isStolen() {
        return stolen;
    }
    
    
}
