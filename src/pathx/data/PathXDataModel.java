/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.data;

import java.util.ArrayList;
import java.util.HashMap;
import mini_game.MiniGame;
import mini_game.MiniGameDataModel;
import pathx.ui.BanditCar;
import pathx.ui.CopCar;
import pathx.ui.PathXMiniGame;
import pathx.ui.ZombieCar;

/**
 *
 * @author Andrew
 */
public class PathXDataModel extends MiniGameDataModel{
    
    private PathXMiniGame miniGame;
    private String currentLevel;
    
    //References to all the opposing cars in the game.
    private ArrayList<CopCar> cops;
    private ArrayList<BanditCar> bandits;
    private ArrayList<ZombieCar> zombies;
    
    //Used to check unlocked specials and unlocked/completed levels.
    private HashMap<String, Boolean> specials;
    private HashMap<String, PathXLevel> levels;
    
     public PathXDataModel(PathXMiniGame initMiniGame){
        miniGame = initMiniGame;
        
        cops = new ArrayList();
        bandits = new ArrayList();
        zombies = new ArrayList();
        
        currentLevel = null;
        
        initSpecials();
        
    }
    
    /**
     * This method provides a custom game response for handling mouse clicks on
     * the game screen. We'll use this to close game dialogs as well as to
     * listen for mouse clicks on Nodes, Cars, Specials, and etc.
     *
     * @param game The pathX game.
     *
     * @param x The x-axis pixel location of the mouse click.
     *
     * @param y The y-axis pixel location of the mouse click.
     */
    @Override
    public void checkMousePressOnSprites(MiniGame game, int x, int y){
        
    }
    
    @Override
    public void endGameAsWin(){
        
    }
    
    @Override
    public void endGameAsLoss(){
        
    }
    //Helper method that adds all of the game specials to the specials HashMap 
    //for management in game.
    private void initSpecials(){
        
    }

    @Override
    public void reset(MiniGame mg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateAll(MiniGame mg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateDebugText(MiniGame mg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}