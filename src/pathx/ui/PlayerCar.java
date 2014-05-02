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
     * An ArrayList representation of the shortest path to the destination PathXNode.
     * @throws VertexNotFoundException
     * If either the current intersection or the destination is not part of the 
     * current level's graph, then this exception is thrown.
     */
    public ArrayList<PathXNode> generatePath(PathXNode destination)throws VertexNotFoundException{
        Graph graph = getLevel().getGraph();
        
        //The shortest path from the current intersection/vertex to the destination
        //intersection/vertex as an ArrayList of vertices.
        ArrayList<Vertex> shortestPath = graph.findPath(getIntersection().getVertex(), destination.getVertex());
        
        ArrayList<PathXNode> path = new ArrayList();
        
        //Convert the shortestPath ArrayList of Vertices to PathXNodes.
        ArrayList<PathXNode> nodes = getLevel().getDataModel().getNodes();
        for (Vertex v : shortestPath){
            for (PathXNode node : nodes)
                if (node.getVertex() == v){
                    path.add(node);
                    break;
                }
        }
        path.remove(0);
        return path;  
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
}
