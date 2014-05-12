/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import graph.VertexNotFoundException;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mini_game.MiniGameDataModel;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.Viewport;
import pathx.PathXConstants;
import static pathx.PathXConstants.GAME_SCREEN_STATE;
import static pathx.PathXConstants.LEVEL_SELECT_SCREEN_STATE;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevel;
import static pathx.ui.PathXSpriteState.INVISIBLE;

/**
 * This class will manage all of the event handling for the game. Namely the 
 * game's various specials.
 * @author Andrew
 */
public class PathXEventHandler {
    
    PathXMiniGame game;
    PathXDataModel dataModel;
    Viewport vp;
    Viewport gameVp;
    
    public PathXEventHandler(PathXMiniGame initGame){
        game = initGame;
        dataModel = (PathXDataModel) initGame.getDataModel();
        vp = dataModel.getViewport();
        gameVp = dataModel.getGameViewport();
    }
    
    /**
     * Called when the user clicks the close window button.
     */    
    public void respondToExitRequest()
    {
        // IF THE GAME IS STILL GOING ON, END IT AS A LOSS
        if (game.getDataModel().inProgress())
        {
            game.getDataModel().endGameAsLoss();
        }
        // AND CLOSE THE ALL
        System.exit(0);        
    }
    
    public void switchToMainMenu(){
        if (game.isCurrentScreenState(PathXConstants.LEVEL_SELECT_SCREEN_STATE) ||
                game.isCurrentScreenState(PathXConstants.SETTINGS_SCREEN_STATE) ||
                game.isCurrentScreenState(PathXConstants.HELP_SCREEN_STATE)){
            game.switchToMainMenu();
        }
    }
    
    public void switchToLevelSelectScreen(){
        if (game.isCurrentScreenState(PathXConstants.MENU_SCREEN_STATE) || 
                game.isCurrentScreenState(PathXConstants.GAME_SCREEN_STATE)){
            game.switchToLevelSelectScreen();
        }
    }
    
    public void switchToGameScreen(PathXLevel level){
        //PathXLevel level = dataModel.getLevels().get(levelName);
        dataModel.setCurrentLevel(level);
        game.switchToGameScreen(level);
    }
    
    //Resets the player's record.
    public void resetRequest(){
        
    }
    
    public void switchToSettingsMenu(){
        if (game.isCurrentScreenState(PathXConstants.MENU_SCREEN_STATE)){
            game.switchToSettingsScreen();
        }
    }
    
    public void switchToHelpView(){
        if (game.isCurrentScreenState(PathXConstants.MENU_SCREEN_STATE))
            game.switchToHelpScreen();
    }
    
    //Should save the player record before quitting.
    public void quitGameRequest(){
        respondToExitRequest();
    }
    
    public void displayLevelInfoRequest(PathXLevel level){
        
    }
    
    public void closeLevelDialog(){
        game.getGUIDecor().get(PathXConstants.GAME_POPUP_TYPE).setState(INVISIBLE.toString());
        game.getGUIDecor().get(PathXConstants.GAME_POPUP_TYPE).setEnabled(false);
        game.getGUIButtons().get(PathXConstants.CLOSE_BUTTON_TYPE).setState(INVISIBLE.toString());
        game.getGUIButtons().get(PathXConstants.CLOSE_BUTTON_TYPE).setEnabled(false);
    }
    
    public void startLevelRequest(){
        dataModel.beginGame();
    }
    
    //Triggered when the "try again" option is chosen after a level completion
    //or failure.
    public void resetLevel(){
        //Delete/clear all of the cars, nodes, and roads
        dataModel.resetLists();
        dataModel.resetGameViewport();
        dataModel.setGameState(MiniGameState.NOT_STARTED);
        
        //Reconstruct everything needed for the level. This will rearrange the 
        //starting position of the cops.
        dataModel.constructNodes(dataModel.getCurrentLevel());
        dataModel.constructRoads(dataModel.getCurrentLevel());
        dataModel.constructEnemyCars(dataModel.getCurrentLevel());
        dataModel.constructPlayerCar(dataModel.getCurrentLevel());
        
    }
    //Will either scroll the level select or game level view.
    public void scrollUpRequest(){
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
            if (vp.getMinViewportY() < vp.getViewportY() - 4) {
                dataModel.getViewport().scroll(0, -4);
            }
        }else if (game.isCurrentScreenState(GAME_SCREEN_STATE)){
            if (gameVp.getMinViewportY() < gameVp.getViewportY() - 4)
                gameVp.scroll(0, -4);
        }
    }
    
    //Will either scroll the level select or game level view.
    public void scrollDownRequest(){
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
            if (vp.getMaxViewportY() > vp.getViewportY() + 4) {
                vp.scroll(0, 4);
            }
        }else if (game.isCurrentScreenState(GAME_SCREEN_STATE)){
            if (gameVp.getMaxViewportY() > gameVp.getViewportY() + 4)
                gameVp.scroll(0, 4);
        }
    }
    
    //Will either scroll the level select or game level view.
    public void scrollLeftRequest(){
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
            if (vp.getMinViewportX() < vp.getViewportX() - 4) {
                vp.scroll(-4, 0);
            }
        }else if (game.isCurrentScreenState(GAME_SCREEN_STATE)){
            if (gameVp.getMinViewportX() < gameVp.getViewportX() - 4)
                gameVp.scroll(-4, 0);
        }
    }
    
    //Will either scroll the level select or game level view.
    public void scrollRightRequest(){
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
            if (vp.getMaxViewportX() > vp.getViewportX() + 4) {
                vp.scroll(4, 0);
            }
        }else if (game.isCurrentScreenState(GAME_SCREEN_STATE)){
            if (gameVp.getMaxViewportX() > gameVp.getViewportX() + 4)
                gameVp.scroll(4, 0);
        }
    }
    
    public void toggleSoundRequest(){
        //TODO Toggle the game's sound
        Sprite soundToggle = game.getGUIButtons().get(PathXConstants.SOUND_TOGGLE_BUTTON_TYPE);
        if (soundToggle.getState().equals(PathXSpriteState.ENABLED.toString()))
            soundToggle.setState(PathXSpriteState.DISABLED.toString());
        else
            soundToggle.setState(PathXSpriteState.ENABLED.toString());
    }
    
    public void toggleMusicRequest(){
        //TODO Toggle the game's music
        Sprite musicToggle = game.getGUIButtons().get(PathXConstants.MUSIC_TOGGLE_BUTTON_TYPE);
        if (musicToggle.getState().equals(PathXSpriteState.ENABLED.toString()))
            musicToggle.setState(PathXSpriteState.DISABLED.toString());
        else
            musicToggle.setState(PathXSpriteState.ENABLED.toString());
    }
    
    public void changeGameSpeed(double multiplier){
        
    }
    
    public void respondToNodeSelection(float xPos, float yPos){
        PlayerCar player = dataModel.getPlayer();
        PathXNode node = null;
        
        ArrayList<PathXNode> nodes = dataModel.getNodes();
        for (PathXNode n : nodes) {
            if (n.containsPoint(xPos, yPos)) {
                node = n;
                break;
            }
        }
        ArrayList<PathXNode> path = null;
        
        //Check if the player is trying to use a special
        if (dataModel.isSpecialActive()){
            String activatedSpecial = dataModel.getActivatedSpecial();
            
            switch (activatedSpecial){
                case PathXConstants.MAKE_GREEN_SPECIAL_TYPE:
                    makeLightGreen(node);
                    break;
                case PathXConstants.MAKE_RED_SPECIAL_TYPE:
                    makeLightRed(node);
                    break;
                case PathXConstants.CLOSE_INTERSECTION_SPECIAL_TYPE:
                    closeIntersection(node);
                    break;
                case PathXConstants.OPEN_INTERSECTION_SPECIAL_TYPE:
                    openIntersection(node);
                    break;
            }
        }
        //If the player is not using a Special then navigate the Player to that 
        //PathXNode
        else{
            try {
                path = player.generatePath(node);
            } catch (VertexNotFoundException ex) {
                Logger.getLogger(PathXEventHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (path != null)
                player.changeDestination(path);
            //player.startMovingToTarget(2, gameVp);
            
        }
    }
    
    /**
     * This method does nothing if there is no special active. If there is, then
     * it searches for the car using the mouse's last known coordinates and the
     * coordinates of every car currently in the game.
     * 
     * @param xPos
     * @param yPos 
     */
    public void respondToCarSelection(float xPos, float yPos){
        
        if (dataModel.isSpecialActive()) {
            Car car = null;

            ArrayList<Car> enemies = new ArrayList();
            enemies.addAll(dataModel.getCops());
            enemies.addAll(dataModel.getBandits());
            enemies.addAll(dataModel.getZombies());

            //Find the car that we just clicked.
            for (Car c : enemies) {
                if (c.containsPoint(xPos, yPos)) {
                    car = c;
                    break;
                }
            }

            String activatedSpecial = dataModel.getActivatedSpecial();

            switch (activatedSpecial) {
                case PathXConstants.FLAT_TIRE_SPECIAL_TYPE:
                    flattenTires(car);
                    break;
                case PathXConstants.EMPTY_GAS_SPECIAL_TYPE:
                    emptyGasTank(car);
                    break;
                case PathXConstants.MIND_CONTROL_SPECIAL_TYPE:
                    mindControl(car);
                    break;
                case PathXConstants.MINDLESS_TERROR_SPECIAL_TYPE:
                    mindlessTerror(car);
                    break;
            }

        }
    }
    
    //GAME SPECIALS
    public void makeLightGreen(PathXNode node){
        if(node.getState().indexOf("GREEN") < 0)
            node.makeGreen();
    }
    
    public void makeLightRed(PathXNode node){
        if(node.getState().indexOf("RED") < 0)
            node.makeRed();
    }
    
    public void freezeTime(){
        
    }
    
    public void decreaseRoadSpeed(Road road){
        
    }
    
    public void increaseRoadSpeed(Road road){
        
    }
    
    public void increasePlayerSpeed(){
        double newSpeed = dataModel.getPlayer().getMaxSpeed() * 1.20;
        dataModel.getPlayer().setMaxSpeed(newSpeed);
        game.getGUIButtons().get(PathXConstants.INCREASE_PLAYER_SPEED_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        dataModel.setSpecialActive(false);
        dataModel.setActivatedSpecial("");
    }
    
    public void flattenTires(Car car){
        car.flattenTires();
        game.getGUIButtons().get(PathXConstants.FLAT_TIRE_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        dataModel.setSpecialActive(false);
        dataModel.setActivatedSpecial("");
    }
    
    public void emptyGasTank(Car car){
        car.emptyGas();
        game.getGUIButtons().get(PathXConstants.EMPTY_GAS_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        dataModel.setSpecialActive(false);
        dataModel.setActivatedSpecial("");
    }
    
    public void closeRoad(Road road){
        
    }
    
    public void closeIntersection(PathXNode node){
        if(node.getState().indexOf("CLOSE") < 0)
            node.close();
    }
    
    public void openIntersection(PathXNode node){
        if(node.getState().indexOf("CLOSE") >= 0)
            node.open();
    }
    
    public void steal(){
        
    }
    
    //Will use changeDestination within the car class.
    public void mindControl(Car car){
        
    }
    
    public void intangibility(){
        
    }
    
    public void mindlessTerror(Car car){
        
    }
    
    public void fly(){
        
    }
    
    public void godMode(){
        
    }

    public void respondToKeyPress(int keyCode) {
        //Right key press on level select screen
        if (keyCode == KeyEvent.VK_RIGHT){
            scrollRightRequest();
        }else if (keyCode == KeyEvent.VK_LEFT){
            scrollLeftRequest();
        }else if (keyCode == KeyEvent.VK_UP){
            scrollUpRequest();
        }else if (keyCode == KeyEvent.VK_DOWN){
            scrollDownRequest();
        }
        
        //Pressing U unlocks and completes all of the levels on the level select screen.
        else if (keyCode == KeyEvent.VK_U){
            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)){
                Collection<PathXLevel> levels = dataModel.getLevels().values();
                for(PathXLevel level : levels)
                    level.setCompleted(true);
                for(PathXLevelSprite ls : dataModel.getLevelSprites())
                    ls.setState(PathXSpriteState.COMPLETED.toString());
            }
        }
    }

    public void pauseGame() {
        TreeMap<String, Sprite> buttons = game.getGUIButtons();
        if(dataModel.inProgress()){
            dataModel.setGameState(MiniGameState.NOT_STARTED);
            buttons.get(PathXConstants.PAUSE_BUTTON_TYPE).setState(PathXConstants.PAUSED_STATE);
        }else{
            dataModel.setGameState(MiniGameState.IN_PROGRESS);
            buttons.get(PathXConstants.PAUSE_BUTTON_TYPE).setState(PathXConstants.UNPAUSED_STATE);
        }
    }
}
