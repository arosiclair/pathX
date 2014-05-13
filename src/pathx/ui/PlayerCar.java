/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import graph.Graph;
import graph.Vertex;
import graph.VertexNotFoundException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import mini_game.MiniGame;
import mini_game.SpriteType;
import pathx.PathXConstants;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevel;

/**
 *
 * @author Andrew
 */
public class PlayerCar extends Car{
    
    public PlayerCar(SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXLevel level, PathXNode startSpot){
        super(initSpriteType, initX, initY, initVx, initVy, initState, level, startSpot);
    }
    
    /**
     * generatePath for the PlayerCar will be typically called when the player
     * chooses a desired destination node without drawing a path. This method is 
     * used to find the shortest path to the desired node using the findPath
     * implementation of the Graph library.
     * 
     * @param destination
     * 
     * @return
     * An ArrayList representation of the shortest path to the destination PathXNode.
     * @throws VertexNotFoundException
     * If either the current intersection or the destination is not part of the 
     * current level's graph, then this exception is thrown.
     */
    public ArrayList<PathXNode> generatePath(PathXNode destination)throws VertexNotFoundException{
        
        String destState = destination.getState();
        if (destState.indexOf("MOUSE_OVER") >= 0)
            destState = destState.substring(0, destState.indexOf("_MOUSE_OVER"));
        
        if (path != null && !path.isEmpty())
            return null;
        
        Graph graph = getLevel().getGraph();
        
        //The shortest path from the current intersection/vertex to the destination
        //intersection/vertex as an ArrayList of vertices.
        ArrayList<Vertex> shortestPath = graph.findPath(getIntersection().getVertex(), destination.getVertex());
        
        ArrayList<PathXNode> newPath = new ArrayList();
        
        //Convert the shortestPath ArrayList of Vertices to PathXNodes.
        ArrayList<PathXNode> nodes = getLevel().getDataModel().getNodes();
        for (Vertex v : shortestPath){
            for (PathXNode node : nodes)
                if (node.getVertex() == v){
                    newPath.add(node);
                    break;
                }
        }
        
        
        newPath.remove(0);
        
        //Highlight the nodes.
        for (PathXNode node : newPath)
            node.setState(node.getState() + "_HIGHLIGHTED");
        
        destination.setState(destState + "_HIGHLIGHTED");
        if (newPath != null && !newPath.isEmpty()) {
            targetX = newPath.get(0).getConstantXPos();
            targetY = newPath.get(0).getConstantYPos();
        }
        return newPath;  
    }

    /**
     * Do NOT call this method for a PlayerCar. User input is required to move 
     * the PlayerCar.
     * @return
     * @throws VertexNotFoundException 
     */
    @Override
    public ArrayList<PathXNode> generatePath() throws VertexNotFoundException {
        return null;
    }
    
    //We perform a check to see if the current intersection is the SafeHouse 
    //PathXNode before updating the Player's Car as usual.
    @Override
    public void update(MiniGame game){
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        
        //Check if 10 seconds has past since activating steal
        if (getSpecialState().equals(PathXSpriteState.STEALING.toString())){
            GregorianCalendar newTime = new GregorianCalendar();
            long timePast = newTime.getTimeInMillis() - getSpecialTimer().getTimeInMillis();
            if ((timePast / 1000) >= 10){
                game.getGUIButtons().get(PathXConstants.STEAL_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
                setSpecialState("");
                setSpecialTimer(null);
            }
                
        }
        
        //Check if 10 seconds has past since activating Intangibility
        if (getSpecialState().equals(PathXSpriteState.INTANGIBLE.toString())){
            GregorianCalendar newTime = new GregorianCalendar();
            long timePast = newTime.getTimeInMillis() - getSpecialTimer().getTimeInMillis();
            if ((timePast / 1000) >= 10){
                game.getGUIButtons().get(PathXConstants.INTANGIBILITY_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
                setSpecialState("");
                setSpecialTimer(null);
            }
                
        }
        
        //Check if 10 seconds has past since activating God Mode
        if (getSpecialState().equals(PathXSpriteState.GOD_MODE.toString())){
            GregorianCalendar newTime = new GregorianCalendar();
            long timePast = newTime.getTimeInMillis() - getSpecialTimer().getTimeInMillis();
            if ((timePast / 1000) >= 10){
                game.getGUIButtons().get(PathXConstants.GOD_MODE_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
                setSpecialState("");
                setSpecialTimer(null);
            }
                
        }

        if (getSpecialState().equals(PathXSpriteState.STEALING.toString())) {
            ArrayList<Car> otherCars = new ArrayList();
            otherCars.addAll(data.getCops());
            otherCars.addAll(data.getBandits());
            otherCars.addAll(data.getZombies());

            for (Car car : otherCars) {
                if (aabbsOverlap(car)) {
                    if (!car.isStolen()) {
                        data.getRecord().balance += 20;
                        car.setStolen(true);
                    }
                }
            }
        }
        
        super.update(game);
        
        PathXDataModel dataModel = (PathXDataModel) game.getDataModel();
        PathXNode safeHouse = dataModel.getNodes().get(1);
        if (getIntersection() == safeHouse)
            dataModel.endGameAsWin();
    }

    public void steal() {
        if (getSpecialState().equals("")){
            setSpecialState(PathXSpriteState.STEALING.toString());
            setSpecialTimer(new GregorianCalendar());
        }
    }

    public void intangibility() {
        if (getSpecialState().equals("")){
            setSpecialState(PathXSpriteState.INTANGIBLE.toString());
            setSpecialTimer(new GregorianCalendar());
        }
    }
    
    public void godMode(){
        if (getSpecialState().equals("")){
            setSpecialState(PathXSpriteState.GOD_MODE.toString());
            setSpecialTimer(new GregorianCalendar());
        }
    }
}
