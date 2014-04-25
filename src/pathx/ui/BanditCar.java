/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.util.ArrayList;
import mini_game.Sprite;
import mini_game.SpriteType;

/**
 *
 * @author Andrew
 */
public class BanditCar extends Car{

    public BanditCar(SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXNode startSpot){
        super(initSpriteType, initX, initY, initVx, initVy, initState, startSpot);
    }
    
    @Override
    public ArrayList<PathXNode> generatePath(PathXNode destination){
        return null;
        //TODO Create algorithm for finding a BanditCar's path.
    }
}
