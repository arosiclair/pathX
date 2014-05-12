/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import graph.Vertex;
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
public class CopCar extends Car{

    public CopCar(SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXLevel level, PathXNode startSpot){
        super(initSpriteType, initX, initY, initVx, initVy, initState, level, startSpot);
    }
    
    /**
     * CopCars choose a random Node to visit among the neighbors of the intersection
     * it is at and then moves to it. This simple process is repeated at each 
     * intersection. generatePath will choose the Node to visit.
     * @return
     * An ArrayList of PathXNode's. CopCars will always generate a 1 node path
     * because of the behavior of their AI.
     */
    @Override
    public ArrayList<PathXNode> generatePath(){
        
        //Check if we are already on a path. If we are do nothing and return null.
        if (getPath() != null && !getPath().isEmpty())
            return null;
        
        ArrayList<PathXNode> path = new ArrayList();
        
        //Get neighbors and choose a random one.
        ArrayList<Vertex> neighbors = getIntersection().getVertex().getNeighbors();
        Random r = new Random();
        //Do this if the car gets stuck on a closed node.
        if (neighbors.size() <= 0)
            return null;
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
        
        targetX = path.get(0).getConstantXPos();
        targetY = path.get(0).getConstantYPos();
        return path;
    }
    
    @Override
    public void update(MiniGame game){
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        if(aabbsOverlap(data.getPlayer()))
            data.endGameAsLoss();
        
        super.update(game);
    }
}
