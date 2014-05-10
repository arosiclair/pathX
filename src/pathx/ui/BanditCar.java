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
import java.util.Random;
import mini_game.SpriteType;
import pathx.data.PathXLevel;

/**
 *
 * @author Andrew
 */
public class BanditCar extends Car{

    public BanditCar(SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXLevel level, PathXNode startSpot){
        super(initSpriteType, initX, initY, initVx, initVy, initState, level, startSpot);
    }
    
    /**
     * Bandit Cars choose random Nodes at the "far ends of the graph" to go to. 
     * This is only repeated after they have reached their destination. 
     * @return
     * A path to a far PathXNode in the form of an ArrayList of PathXNodes.
     * @throws VertexNotFoundException 
     */
    @Override
    public ArrayList<PathXNode> generatePath() throws VertexNotFoundException{  
        //Check if we are already on a path. If we are do nothing and return null.
        if (getPath() != null && !getPath().isEmpty())
            return null;
        
        ArrayList<PathXNode> path = new ArrayList();
        
        //Get neighbors and choose a random one.
        ArrayList<Vertex> neighbors = getIntersection().getVertex().getNeighbors();
        Random r = new Random();
        int next = r.nextInt(neighbors.size());
        Vertex destination = neighbors.get(next);
        
        //Find the associated PathXNode
        ArrayList<PathXNode> nodes = getLevel().getDataModel().getNodes();
        for (PathXNode node : nodes){
            if (node.getVertex() == destination){ 
                path.add(node);
                break;
            }
        }
        
//        //Get a path to the farthest Vertex from this Bandit's current Vertex.
//        Graph g = getLevel().getGraph();       
//        Vertex start = getIntersection().getVertex();
//        ArrayList<Vertex> pathToFarthest = g.findLongestPath(start);
//        
//        //Convert the shortestPath ArrayList of Vertices to PathXNodes.
//        ArrayList<PathXNode> path = new ArrayList();
//        ArrayList<PathXNode> nodes = getLevel().getDataModel().getNodes();
//        for (Vertex v : pathToFarthest){
//            for (PathXNode node : nodes)
//                if (node.getVertex() == v) path.add(node);
//        }
//        
//        path.remove(0);
        
        targetX = path.get(0).getConstantXPos();
        targetY = path.get(0).getConstantYPos();
        
        return path;
    }
}
