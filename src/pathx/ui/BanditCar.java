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
import mini_game.MiniGame;
import mini_game.SpriteType;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevel;

/**
 *
 * @author Andrew
 */
public class BanditCar extends Car{
    
    private boolean hasRobbed = false;

    public BanditCar(SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXLevel level, PathXNode startSpot){
        super(initSpriteType, initX, initY, initVx, initVy, initState, level, startSpot);
    }
    
    /**
     * Bandit Cars choose the node that Dijkstra's Algorithm finds has the 
     * largest distance to go to. 
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
        
        Graph graph = getLevel().getGraph();
        Vertex start = getIntersection().getVertex();
        
        //Find the shortest path to the farthest vertex in the graph.
        ArrayList<Vertex> longestPath = graph.findLongestPath(start);
        ArrayList<PathXNode> newPath = new ArrayList();
        
        //Convert the shortestPath ArrayList of Vertices to PathXNodes.
        ArrayList<PathXNode> nodes = getLevel().getDataModel().getNodes();
        for (Vertex v : longestPath){
            for (PathXNode node : nodes)
                if (node.getVertex() == v){
                    newPath.add(node);
                    break;
                }
        }
        
        newPath.remove(0);
        targetX = newPath.get(0).getConstantXPos();
        targetY = newPath.get(0).getConstantYPos();
        
        return newPath;
    }
    
    @Override
    public void update(MiniGame game){
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        if(aabbsOverlap(data.getPlayer()) && !hasRobbed){
            int reward = getLevel().getReward();
            int newReward = (int) (reward - (reward * 0.10));
            getLevel().setReward(newReward);
            hasRobbed = true;
        }
        
        super.update(game);
            
    }
}
