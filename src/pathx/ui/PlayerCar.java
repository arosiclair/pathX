/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import graph.Graph;
import graph.Vertex;
import java.util.ArrayList;
import mini_game.SpriteType;
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
     * @throws VertexNotFoundException 
     */
    public ArrayList<PathXNode> generatePath(PathXNode destination)throws VertexNotFoundException{
        Graph graph = getLevel().getGraph();
        
        //The shortest path from the current intersection/vertex to the destination
        //intersection/vertex as
        ArrayList<Vertex> shortestPath = graph.findPath(getIntersection().getVertex(), destination.getVertex());
        
        
         
    }
}
