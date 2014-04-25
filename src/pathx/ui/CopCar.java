/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.util.ArrayList;
import mini_game.Sprite;
import mini_game.SpriteType;
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
    
    @Override
    public ArrayList<PathXNode> generatePath(PathXNode destination){
        return null;
        //TODO Create an algorithm for finding a CopCar's path.
    }
}
