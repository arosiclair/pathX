/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.KeyEvent;
import mini_game.MiniGameDataModel;
import mini_game.Sprite;
import mini_game.Viewport;
import pathx.PathXConstants;
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
    
    public PathXEventHandler(PathXMiniGame initGame){
        game = initGame;
        dataModel = (PathXDataModel) initGame.getDataModel();
        vp = dataModel.getViewport();
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
        
    }
    
    //Triggered when the "try again" option is chosen after a level completion
    //or failure.
    public void resetLevel(){
        
    }
    //Will either scroll the level select or game level view.
    public void scrollUpRequest(){
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
            if (vp.getMinViewportY() < vp.getViewportY() - 4) {
                dataModel.getViewport().scroll(0, -4);
            }
        }
    }
    
    //Will either scroll the level select or game level view.
    public void scrollDownRequest(){
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
            if (vp.getMaxViewportY() > vp.getViewportY() + 4) {
                dataModel.getViewport().scroll(0, 4);
            }
        }
    }
    
    //Will either scroll the level select or game level view.
    public void scrollLeftRequest(){
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
            if (vp.getMinViewportX() < vp.getViewportX() - 4) {
                dataModel.getViewport().scroll(-4, 0);
            }
        }
    }
    
    //Will either scroll the level select or game level view.
    public void scrollRightRequest(){
        if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
            if (vp.getMaxViewportX() > vp.getViewportX() + 4) {
                dataModel.getViewport().scroll(4, 0);
            }
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
    
    //GAME SPECIALS
    public void makeLightGreen(PathXNode node){
        
    }
    
    public void makeLightRed(PathXNode node){
        
    }
    
    public void freezeTime(){
        
    }
    
    public void decreaseRoadSpeed(Road road){
        
    }
    
    public void increaseRoadSpeed(Road road){
        
    }
    
    public void increasePlayerSpeed(){
        
    }
    
    public void flattenTires(Car car){
        
    }
    
    public void emptyGasTank(Car car){
        
    }
    
    public void closeRoad(Road road){
        
    }
    
    public void closeIntersection(PathXNode node){
        
    }
    
    public void openIntersection(PathXNode node){
        
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

    void respondToKeyPress(int keyCode) {
        //Right key press on level select screen
        if (keyCode == KeyEvent.VK_RIGHT){
            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)){
                scrollRightRequest();
            }
        }else if (keyCode == KeyEvent.VK_LEFT){
            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)){
                scrollLeftRequest();
            }
        }else if (keyCode == KeyEvent.VK_UP){
            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)){
                scrollUpRequest();
            }
        }else if (keyCode == KeyEvent.VK_DOWN){
            if (game.isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)){
                scrollDownRequest();
            }
        }
    }
}
