/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType;
import pathx.PathXConstants;
import static pathx.PathXConstants.*;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevel;
import pathx.data.PathXRecord;
import pathx.file.PathXFileManager;
import static pathx.ui.PathXSpriteState.*;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class PathXMiniGame extends MiniGame{
    
    //Manages game data including the player's record, different cars in-game,
    //private PathXDataModel dataModel;
    
    //Holds the stats for the current player including balance, unlocked levels,
    //and unlocked specials.
    private PathXRecord record;
    
    //Handles mostly UI events, namely special activations and screen switching.
    private PathXEventHandler eventHandler;
    
    //Handles errors that will probably occur when loading from property XML
    //files images and other things.
    private PathXErrorHandler errorHandler;
    
    private Viewport levelSelectViewport;
    private Viewport gameViewport;
    
    //Loads and saves player records.
    private PathXFileManager fileManager;
    
    //Indicates the current screen being displayed.
    private String screenState;
    
    public PathXErrorHandler getErrorHandler(){
        return errorHandler;
    }
    
    //This method is used to get images of a locked, incomplete, or complete level
    // node for the level select screen.
    public BufferedImage[] getLevelNodeImage(String levelStatus){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        BufferedImage[] images = new BufferedImage[2];
        switch (levelStatus) {
            case PathXConstants.LOCKED_LEVEL_TYPE:
                images[0] = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LOCKED_LEVEL));
                images[1] = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LOCKED_LEVEL_MOUSE_OVER));
                return images;
            case PathXConstants.INCOMPLETE_LEVEL_TYPE:
                images[0] = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INCOMPLETE_LEVEL));
                images[1] = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INCOMPLETE_LEVEL_MOUSE_OVER));
                return images;
            case PathXConstants.COMPLETE_LEVEL_TYPE:
                images[0] = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_COMPLETED_LEVEL));
                images[1] = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_COMPLETED_LEVEL_MOUSE_OVER));
                return images;
            default:
                return null;
        }
    }
    
    public boolean isCurrentScreenState(String screenState){
        return this.screenState.equals(screenState);
    }
    
    public void switchToLevelSelectScreen(){
        //PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(LEVEL_SELECT_SCREEN_STATE);
        data.setGameState(MiniGameState.NOT_STARTED);
        
        //ACTIVE NORTH PANEL CONTROLS
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(true);
        
        //ACTIVATE ARROW BUTTONS
        guiButtons.get(UP_ARROW_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(UP_ARROW_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setEnabled(true);
        
        //Activatate the Level buttons
        //The level sprites will be automatically renedered by the PathXPanel 
        //when the screen state is changed.
        
        
        if (screenState.equals(MENU_SCREEN_STATE)) {
            //DEACTIVATE MAIN MENU BUTTONS
            guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(RESET_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(SETTINGS_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(HELP_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);

            screenState = LEVEL_SELECT_SCREEN_STATE;
        } else {
            //DEACTIVATE GAME SCREEN BUTTONS
            guiButtons.get(GAME_BACK_BUTTON_TYPE).setState(INVISIBLE.toString());
            guiButtons.get(GAME_BACK_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(GAME_QUIT_BUTTON_TYPE).setState(INVISIBLE.toString());
            guiButtons.get(GAME_QUIT_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(START_BUTTON_TYPE).setState(INVISIBLE.toString());
            guiButtons.get(START_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(PAUSE_BUTTON_TYPE).setState(INVISIBLE.toString());
            guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(false);
            deactivateSpecialButtons();
            
            //((PathXDataModel) data).endGameAsLoss();
            ((PathXDataModel) data).resetGameViewport();
            ((PathXDataModel) data).resetLists();
            screenState = LEVEL_SELECT_SCREEN_STATE;
        }
        //SONGS
    }
    
    public void switchToGameScreen(PathXLevel level){
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(GAME_SCREEN_STATE);
        //ACTIVATE POPUP DIALOG AND CLOSE BUTTON
        guiDecor.get(GAME_POPUP_TYPE).setState(VISIBLE.toString());
        guiDecor.get(GAME_POPUP_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
        
        //Activate Game Buttons
        guiButtons.get(GAME_BACK_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(GAME_BACK_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(GAME_QUIT_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(GAME_QUIT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(START_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(START_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(PAUSE_BUTTON_TYPE).setState(UNPAUSED_STATE);
        guiButtons.get(PAUSE_BUTTON_TYPE).setEnabled(true);
        activateSpecialButtons();
        
        //ACTIVATE POPUP DIALOG AND CLOSE BUTTON
        guiDecor.get(GAME_POPUP_TYPE).setState(VISIBLE.toString());
        guiDecor.get(GAME_POPUP_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(CLOSE_BUTTON_TYPE).setEnabled(true);
            
        //DEACTIVATE LEVEL SELECT BUTTONS
        guiButtons.get(BACK_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(false);
        
        
//        ArrayList<PathXLevelSprite> levelSprites = ((PathXDataModel)data).getLevelSprites();
//        for (PathXLevelSprite ls : levelSprites)
//            guiButtons.remove(ls.getName());
        
        //Update the Game Viewport to use the dimensions of the level's
        //background image
        BufferedImage bgImage = loadImage(PATH_DATA + "levels/" + level.getBgImage());
        gameViewport.setGameWorldSize(bgImage.getWidth(), bgImage.getHeight());
        gameViewport.setMaxViewportX(bgImage.getWidth() - GAME_VIEWPORT_WIDTH);
        gameViewport.setMaxViewportY(bgImage.getHeight() - GAME_VIEWPORT_HEIGHT);

        //Construct the PathXNode Sprites.
        ((PathXDataModel)data).constructNodes(level);
        //Construct the Road Sprites.
        ((PathXDataModel)data).constructRoads(level);
        //Update neighbors for each PathXNode
        //((PathXDataModel)data).updateNodeNeighbors(level);
        
        //Construct the PlayerCar.
        ((PathXDataModel) data).constructPlayerCar(level);
        
        //Construct enemy cars.
        ((PathXDataModel) data).constructEnemyCars(level);
        
        screenState = GAME_SCREEN_STATE;
        
        //((PathXDataModel) data).beginGame();
    }
    
    public void switchToMainMenu(){
        //PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(MENU_SCREEN_STATE);
        
        //Activate Menu Buttons
        guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(RESET_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(HELP_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(QUIT_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(true);
        
        //If we are switching from the level select screen or settings screen.
        if (screenState.equals(LEVEL_SELECT_SCREEN_STATE)) {
            //Deactivate level select buttons
            guiButtons.get(BACK_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(UP_ARROW_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(UP_ARROW_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(DOWN_ARROW_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(RIGHT_ARROW_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(LEFT_ARROW_BUTTON_TYPE).setEnabled(false);
            
            //Deactivate the level buttons
            ArrayList<PathXLevelSprite> levelSprites = ((PathXDataModel) data).getLevelSprites();
            for (PathXLevelSprite ls : levelSprites) {
                guiButtons.remove(ls.getName());
            }
        } else if (screenState.equals(SETTINGS_SCREEN_STATE)){
            //Deactivate settings screen buttons
            guiButtons.get(SOUND_TOGGLE_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(SOUND_TOGGLE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(MUSIC_TOGGLE_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(MUSIC_TOGGLE_BUTTON_TYPE).setEnabled(false);
            guiButtons.get(BACK_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
            guiButtons.get(BACK_BUTTON_TYPE).setEnabled(false);
        }
        screenState = MENU_SCREEN_STATE;
    }
    
    public void switchToSettingsScreen(){
        //PropertiesManager props = PropertiesManager.getPropertiesManager();
        
        // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(SETTINGS_SCREEN_STATE);
        
        //DEACTIVE MAIN MENU BUTTONS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        
        //ACTIVATE SETTINGS BUTTONS
        guiButtons.get(SOUND_TOGGLE_BUTTON_TYPE).setState(PathXSpriteState.DISABLED.toString());
        guiButtons.get(SOUND_TOGGLE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MUSIC_TOGGLE_BUTTON_TYPE).setState(PathXSpriteState.DISABLED.toString());
        guiButtons.get(MUSIC_TOGGLE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        
        screenState = SETTINGS_SCREEN_STATE;
    }
    
    public void switchToHelpScreen() {
        
         // CHANGE THE BACKGROUND
        guiDecor.get(BACKGROUND_TYPE).setState(HELP_SCREEN_STATE);
        
        //ACTIVATE QUIT AND BACK BUTTONS
        guiButtons.get(QUIT_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(QUIT_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(BACK_BUTTON_TYPE).setState(PathXSpriteState.VISIBLE.toString());
        guiButtons.get(BACK_BUTTON_TYPE).setEnabled(true);
        
        //DEACTIVE MAIN MENU BUTTONS
        guiButtons.get(PLAY_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
        guiButtons.get(PLAY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(RESET_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
        guiButtons.get(RESET_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(SETTINGS_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
        guiButtons.get(SETTINGS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(HELP_BUTTON_TYPE).setState(PathXSpriteState.INVISIBLE.toString());
        guiButtons.get(HELP_BUTTON_TYPE).setEnabled(false);
        
        screenState = HELP_SCREEN_STATE;
    }
    
    @Override
    public void initData(){
        //Initialize error handlier
        errorHandler = new PathXErrorHandler(window);
        
        
        //Load a saved player record.
        //record = fileManager.loadRecord();
        
        //Initialize the data model.
        data = new PathXDataModel(this);
                
        //Initialize file manager.
        fileManager = new PathXFileManager(this);
        
        //Load the PathXLevels from the XML file.
//        fileManager.loadLevelDetails();
//        ((PathXDataModel)data).updateRecord();
    }

    @Override
    public void initAudioContent() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void initGUIControls() {
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        // FIRST PUT THE ICON IN THE WINDOW
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);        
        String windowIconFile = props.getProperty(PathXPropertyType.IMAGE_WINDOW_ICON);
        img = loadImage(imgPath + windowIconFile);
        window.setIconImage(img);
        
        // CONSTRUCT THE PANEL WHERE WE'LL DRAW EVERYTHING
        canvas = new PathXPanel(this, (PathXDataModel)data);
        
        // LOAD THE BACKGROUNDS, WHICH ARE GUI DECOR
        screenState = PathXConstants.MENU_SCREEN_STATE;
        
        sT = new SpriteType(BACKGROUND_TYPE);
        
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_MENU));
        sT.addState(MENU_SCREEN_STATE, img); 
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_LEVEL));
        sT.addState(LEVEL_SELECT_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_GAME));
        sT.addState(GAME_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_SETTINGS));
        sT.addState(SETTINGS_SCREEN_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BACKGROUND_HELP));
        sT.addState(HELP_SCREEN_STATE, img);
        
        s = new Sprite(sT, 0, 0, 0, 0, MENU_SCREEN_STATE);
        guiDecor.put(BACKGROUND_TYPE, s);
        
        initMenuButtons();
        initLevelSelectButtons();
        initGameButtons();
        initSettingsButtons();
         
    }
    
    private void initMenuButtons(){
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        
        //ADD MAIN MENU BUTTONS
        //PLAY BUTTON
        sT = new SpriteType(PLAY_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_PLAY_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        s = new Sprite(sT, PLAY_BUTTON_X, PLAY_BUTTON_Y, 0, 0, VISIBLE.toString());
        guiButtons.put(PLAY_BUTTON_TYPE, s);
        
        //RESET BUTTON
        sT = new SpriteType(RESET_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_RESET));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_RESET_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PLAY_BUTTON_X + MENU_BUTTON_WIDTH + MENU_BUTTON_GAP;
        y = PLAY_BUTTON_Y;
        s = new Sprite(sT, x, y, 0, 0, VISIBLE.toString());
        guiButtons.put(RESET_BUTTON_TYPE, s);
        
        //SETTINGS BUTTON
        sT = new SpriteType(SETTINGS_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_SETTINGS_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PLAY_BUTTON_X + (2 * MENU_BUTTON_WIDTH) + (2 * MENU_BUTTON_GAP);
        s = new Sprite(sT, x, y, 0, 0, VISIBLE.toString());
        guiButtons.put(SETTINGS_BUTTON_TYPE, s);
        
        //HELP BUTTON
        sT = new SpriteType(HELP_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_HELP));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_HELP_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PLAY_BUTTON_X + (3 * MENU_BUTTON_WIDTH) + (3 * MENU_BUTTON_GAP);
        s = new Sprite(sT, x, y, 0, 0, VISIBLE.toString());
        guiButtons.put(HELP_BUTTON_TYPE, s);
        
        //QUIT BUTTON
        sT = new SpriteType(QUIT_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_QUIT));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_QUIT_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = MAIN_QUIT_BUTTON_X;
        y = MAIN_QUIT_BUTTON_Y;
        s = new Sprite(sT, x, y, 0, 0, VISIBLE.toString());
        guiButtons.put(QUIT_BUTTON_TYPE, s);
    }
    
    private void initLevelSelectButtons(){
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        
        //LEVEL SELECT SCREEN BUTTONS
        //BACK BUTTON
        sT = new SpriteType(BACK_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_BACK));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_BACK_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PathXConstants.LEVEL_SELECT_BACK_BUTTON_X - 60;
        y = PathXConstants.MAIN_QUIT_BUTTON_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(BACK_BUTTON_TYPE, s);
        
        //ARROW BUTTONS
        //UP ARROW
        sT = new SpriteType(UP_ARROW_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_UP_ARROW));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        x = UP_ARROW_X;
        y = UP_ARROW_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(UP_ARROW_BUTTON_TYPE, s);
        
        //DOWN ARROW
        sT = new SpriteType(DOWN_ARROW_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_DOWN_ARROW));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        s = new Sprite(sT, DOWN_ARROW_X, DOWN_ARROW_Y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(DOWN_ARROW_BUTTON_TYPE, s);
        
        //LEFT ARROW
        sT = new SpriteType(LEFT_ARROW_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_LEFT_ARROW));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        s = new Sprite(sT, LEFT_ARROW_X, LEFT_ARROW_Y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(LEFT_ARROW_BUTTON_TYPE, s);
        
        //RIGHT ARROW
        sT = new SpriteType(RIGHT_ARROW_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_RIGHT_ARROW));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        s = new Sprite(sT, RIGHT_ARROW_X, RIGHT_ARROW_Y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(RIGHT_ARROW_BUTTON_TYPE, s);
        
        //THE VIEWPORT
        initLevelSelectViewport();
        
        // KEY LISTENER - LET'S US PROVIDE CUSTOM RESPONSES
        this.setKeyListener(new KeyAdapter(){
            public void keyPressed(KeyEvent ke)
            {   
                getEventHandler().respondToKeyPress(ke.getKeyCode());    
            }
        });
    }
    
    private void initGameButtons(){
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        
        //GAME SCREEN BUTTONS
        //BACK BUTTON
        sT = new SpriteType(GAME_BACK_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_BACK));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_BACK_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PathXConstants.GAME_BACK_BUTTON_X;
        y = PathXConstants.GAME_BACK_BUTTON_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(GAME_BACK_BUTTON_TYPE, s);
        
        //QUIT BUTTON
        sT = new SpriteType(GAME_QUIT_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_QUIT));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_QUIT_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PathXConstants.GAME_QUIT_BUTTON_X;
        y = PathXConstants.GAME_QUIT_BUTTON_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(GAME_QUIT_BUTTON_TYPE, s);
        
        //START BUTTON
        sT = new SpriteType(START_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_START));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_START_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PathXConstants.GAME_START_BUTTON_X;
        y = PathXConstants.GAME_START_BUTTON_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(START_BUTTON_TYPE, s);
        
        //PAUSE BUTTON
        sT = new SpriteType(PAUSE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_PAUSE));
        sT.addState(UNPAUSED_STATE, img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_UNPAUSE));
        sT.addState(PAUSED_STATE, img);
        x = PathXConstants.LEFT_ARROW_X + 30;
        y = PathXConstants.LEFT_ARROW_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(PAUSE_BUTTON_TYPE, s);
        
        //GAME SPECIALS GO HERE.
        //Make Light Green Button
        sT = new SpriteType(MAKE_GREEN_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MAKE_GREEN));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MAKE_GREEN_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X;
        y = PathXConstants.FIRST_SPECIAL_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(MAKE_GREEN_BUTTON_TYPE, s);
        
        //Make Light Red Button
        sT = new SpriteType(MAKE_RED_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MAKE_RED));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MAKE_RED_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + GAME_SPECIAL_WIDTH;
        y = PathXConstants.FIRST_SPECIAL_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(MAKE_RED_BUTTON_TYPE, s);
        
        //Freeze Time Button
//        sT = new SpriteType(FREEZE_BUTTON_TYPE);
//        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_FREEZE_TIME));
//        sT.addState(VISIBLE.toString(), img);
//        sT.addState(MOUSE_OVER.toString(), img);
//        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_FREEZE_TIME_ACTIVE));
//        sT.addState(ENABLED.toString(), img);
//        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 2);
//        y = PathXConstants.FIRST_SPECIAL_Y;
//        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
//        s.setEnabled(false);
//        guiButtons.put(FREEZE_BUTTON_TYPE, s);
        
        //INCREASE SPEED BUTTON
        sT = new SpriteType(INCREASE_SPEED_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INCREASE_SPEED));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INCREASE_SPEED_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 2);
        y = PathXConstants.FIRST_SPECIAL_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(INCREASE_SPEED_BUTTON_TYPE, s);
        
        //DECREASE SPEED BUTTON
        sT = new SpriteType(DECREASE_SPEED_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_DECREASE_SPEED));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_DECREASE_SPEED_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 3);
        y = PathXConstants.FIRST_SPECIAL_Y ;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(DECREASE_SPEED_BUTTON_TYPE, s);
        
        //INCREASE PLAYER SPEED BUTTON
        sT = new SpriteType(INCREASE_PLAYER_SPEED_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INCREASE_PLAYER_SPEED));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INCREASE_PLAYER_SPEED_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X;
        y = PathXConstants.FIRST_SPECIAL_Y + GAME_SPECIAL_HEIGHT;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(INCREASE_PLAYER_SPEED_BUTTON_TYPE, s);
        
        //FLAT TIRE BUTTON
        sT = new SpriteType(FLAT_TIRE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_FLAT_TIRE));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_FLAT_TIRE_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + GAME_SPECIAL_WIDTH;
        y = PathXConstants.FIRST_SPECIAL_Y + GAME_SPECIAL_HEIGHT;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(FLAT_TIRE_BUTTON_TYPE, s);
        
        //EMPTY GAS BUTTON
        sT = new SpriteType(EMPTY_GAS_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_EMPTY_GAS));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_EMPTY_GAS_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 2);
        y = PathXConstants.FIRST_SPECIAL_Y + GAME_SPECIAL_HEIGHT;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(EMPTY_GAS_BUTTON_TYPE, s);
        
        //CLOSE ROAD BUTTON
        sT = new SpriteType(CLOSE_ROAD_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_CLOSE_ROAD));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_CLOSE_ROAD_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 3);
        y = PathXConstants.FIRST_SPECIAL_Y + GAME_SPECIAL_HEIGHT;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(CLOSE_ROAD_BUTTON_TYPE, s);
        
        //OPEN INTERSECTION BUTTON
        sT = new SpriteType(OPEN_INTERSECTION_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_OPEN_NODE));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_OPEN_NODE_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X;
        y = PathXConstants.FIRST_SPECIAL_Y + (GAME_SPECIAL_WIDTH * 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(OPEN_INTERSECTION_BUTTON_TYPE, s);
        
        //CLOSE INTERSECTION BUTTON
        sT = new SpriteType(CLOSE_INTERSECTION_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_CLOSE_NODE));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_CLOSE_NODE_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + GAME_SPECIAL_WIDTH;
        y = PathXConstants.FIRST_SPECIAL_Y + (GAME_SPECIAL_WIDTH * 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(CLOSE_INTERSECTION_BUTTON_TYPE, s);
        
        //STEALING BUTTON
        sT = new SpriteType(STEAL_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_STEALING));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_STEALING_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 2);
        y = PathXConstants.FIRST_SPECIAL_Y + (GAME_SPECIAL_WIDTH * 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(STEAL_BUTTON_TYPE, s);
        
        //MIND CONTROL BUTTON
        sT = new SpriteType(MIND_CONTROL_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MIND_CONTROL));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MIND_CONTROL_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 3);
        y = PathXConstants.FIRST_SPECIAL_Y + (GAME_SPECIAL_HEIGHT * 2);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(MIND_CONTROL_BUTTON_TYPE, s);
        
        //INTANGIBILITY BUTTON
        sT = new SpriteType(INTANGIBILITY_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INTANGIBILITY));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_INTANGIBILITY_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X;
        y = PathXConstants.FIRST_SPECIAL_Y + (GAME_SPECIAL_HEIGHT * 3);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(INTANGIBILITY_BUTTON_TYPE, s);
        
        //MINDLESS TERROR BUTTON
        sT = new SpriteType(MINDLESS_TERROR_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MINDLESS_TERROR));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_MINDLESS_TERROR_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + GAME_SPECIAL_WIDTH;
        y = PathXConstants.FIRST_SPECIAL_Y + (GAME_SPECIAL_HEIGHT * 3);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(MINDLESS_TERROR_BUTTON_TYPE, s);
        
        //FLYING BUTTON
        sT = new SpriteType(FLYING_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_FLYING));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_FLYING_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 2);
        y = PathXConstants.FIRST_SPECIAL_Y + (GAME_SPECIAL_WIDTH * 3);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(FLYING_BUTTON_TYPE, s);
        
        //INVINCIBILITY BUTTON
        sT = new SpriteType(GOD_MODE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_GOD_MODE));
        sT.addState(VISIBLE.toString(), img);
        sT.addState(MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_GOD_MODE_ACTIVE));
        sT.addState(ENABLED.toString(), img);
        x = PathXConstants.FIRST_SPECIAL_X + (GAME_SPECIAL_WIDTH * 3);
        y = PathXConstants.FIRST_SPECIAL_Y + (GAME_SPECIAL_HEIGHT * 3);
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(GOD_MODE_BUTTON_TYPE, s);
        
        //LEVEL SELECT ARROWS WILL BE USED FOR THE GAME SCREEN AS WELL.
        
        //POPUP OVERLAY DECOR
        sT  = new SpriteType(GAME_POPUP_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_POPUP_BACKGROUND));
        sT.addState(VISIBLE.toString(), img);
        x = PathXConstants.GAME_OVERLAY_X;
        y = PathXConstants.GAME_OVERLAY_Y;
        s = new Sprite(sT, x, y, 0, 0,INVISIBLE.toString());
        s.setEnabled(false);
        guiDecor.put(GAME_POPUP_TYPE, s);
        
        //POPUP CLOSE BUTTON
        sT = new SpriteType(CLOSE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_CLOSE_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PathXConstants.OVERLAY_BUTTON_CLOSE_X;
        y = PathXConstants.OVERLAY_BUTTON_CLOSE_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(CLOSE_BUTTON_TYPE, s);
        
        //POPUP TRY AGAIN BUTTON
        sT = new SpriteType(TRY_AGAIN_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_TRY_AGAIN));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_TRY_AGAIN_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PathXConstants.OVERLAY_BUTTON_TRY_AGAIN_X;
        y = PathXConstants.OVERLAY_BUTTON_TRY_AGAIN_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(TRY_AGAIN_BUTTON_TYPE, s);
        
        //POPUP LEAVE TOWN BUTTON
        sT = new SpriteType(LEAVE_TOWN_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEAVE_TOWN));
        sT.addState(VISIBLE.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_LEAVE_TOWN_MOUSE_OVER));
        sT.addState(MOUSE_OVER.toString(), img);
        x = PathXConstants.OVERLAY_BUTTON_LEAVE_TOWN_X;
        y = PathXConstants.OVERLAY_BUTTON_LEAVE_TOWN_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(LEAVE_TOWN_BUTTON_TYPE, s);
        
        //GAME VIEWPORT
        initGameViewport();
        
    }

    private void initSettingsButtons(){
        // WE'LL USE AND REUSE THESE FOR LOADING STUFF
        BufferedImage img;
        float x, y;
        SpriteType sT;
        Sprite s;
        
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        
        //Settings Sound Toggle
        sT = new SpriteType(SOUND_TOGGLE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_TOGGLE_OFF));
        sT.addState(PathXSpriteState.DISABLED.toString(), img);
        sT.addState(PathXSpriteState.MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_TOGGLE_ON));
        sT.addState(PathXSpriteState.ENABLED.toString(), img);
        x = PathXConstants.SOUND_TOGGLE_X;
        y = PathXConstants.SOUND_TOGGLE_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(SOUND_TOGGLE_BUTTON_TYPE, s);
        
        //Settings Music Toggle
        sT = new SpriteType(MUSIC_TOGGLE_BUTTON_TYPE);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_TOGGLE_OFF));
        sT.addState(PathXSpriteState.DISABLED.toString(), img);
        sT.addState(PathXSpriteState.MOUSE_OVER.toString(), img);
        img = loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BUTTON_TOGGLE_ON));
        sT.addState(PathXSpriteState.ENABLED.toString(), img);
        x = PathXConstants.MUSIC_TOGGLE_X;
        y = PathXConstants.MUSIC_TOGGLE_Y;
        s = new Sprite(sT, x, y, 0, 0, INVISIBLE.toString());
        s.setEnabled(false);
        guiButtons.put(MUSIC_TOGGLE_BUTTON_TYPE, s);
    }
    @Override
    public void initGUIHandlers() {
         // WE'LL RELAY UI EVENTS TO THIS OBJECT FOR HANDLING
        eventHandler = new PathXEventHandler(this);
        
        // WE'LL HAVE A CUSTOM RESPONSE FOR WHEN THE USER CLOSES THE WINDOW
        window.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        window.addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent we) 
            {   getEventHandler().respondToExitRequest(); }
        });
        
        initMainMenuHandlers();
        initLevelSelectHandlers();
        initGameScreenHandlers();
        initSettingsHandlers();
        //fileManager.loadLevels();
        fileManager.loadRecord();
        ((PathXDataModel)data).updateRecord();
        
    }

    private void initMainMenuHandlers() {
        //Set play button resopnse
        Sprite playButton = guiButtons.get(PLAY_BUTTON_TYPE);
        playButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                { getEventHandler().switchToLevelSelectScreen();  }
        });
        
        //Set reset button response
        Sprite resetButton = guiButtons.get(RESET_BUTTON_TYPE);
        resetButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                { getEventHandler().resetRequest();  }
        });
        
        //Set settings button response
        Sprite settingsButton = guiButtons.get(SETTINGS_BUTTON_TYPE);
        settingsButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   getEventHandler().switchToSettingsMenu(); }
        });
        
        //Set help button response
        Sprite helpButton = guiButtons.get(HELP_BUTTON_TYPE);
        helpButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   
                    getEventHandler().switchToHelpView();    
                }
        });
        
        //Set quit button response
        Sprite quitButton = guiButtons.get(QUIT_BUTTON_TYPE);
        quitButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   getEventHandler().quitGameRequest(); }
        });
    }
    
    private void initLevelSelectHandlers() {
        //Level select back button
        Sprite backButton = guiButtons.get(BACK_BUTTON_TYPE);
        backButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   getEventHandler().switchToMainMenu();    }
        });
        
        //Scroll up arrow button
        Sprite upArrow = guiButtons.get(UP_ARROW_BUTTON_TYPE);
        upArrow.setActionListener(new ActionListener(){ 
            public void actionPerformed(ActionEvent ae)
                {   getEventHandler().scrollUpRequest(); }
        });
        
        //Scroll down arrow button
        Sprite downArrow = guiButtons.get(DOWN_ARROW_BUTTON_TYPE);
        downArrow.setActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent ae)
                {   getEventHandler().scrollDownRequest();   }
        });
        
        //Scroll left arrow button
        Sprite leftArrow = guiButtons.get(LEFT_ARROW_BUTTON_TYPE);
        leftArrow.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   
                    getEventHandler().scrollLeftRequest();   
                }
        });
        
        //Scroll right arrow button
        Sprite rightArrow = guiButtons.get(RIGHT_ARROW_BUTTON_TYPE);
        rightArrow.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   
                    getEventHandler().scrollRightRequest();  
                }
        });
    }
    
    private void initGameScreenHandlers() {
        //Back button
        Sprite backButton = guiButtons.get(GAME_BACK_BUTTON_TYPE);
        backButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   getEventHandler().switchToLevelSelectScreen();   }
        });
        
        //Quit Button
        Sprite quitButton = guiButtons.get(GAME_QUIT_BUTTON_TYPE);
        quitButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   getEventHandler().quitGameRequest();  }
        });
        
        //START BUTTON
        Sprite startButton = guiButtons.get(START_BUTTON_TYPE);
        startButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   getEventHandler().startLevelRequest();   }
        });
        
        //PAUSE BUTTON
        Sprite pauseButton = guiButtons.get(PAUSE_BUTTON_TYPE);
        pauseButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   getEventHandler().pauseGame();  }
        });
        
        //CLOSE DIALOG BUTTON
        Sprite closeButton = guiButtons.get(CLOSE_BUTTON_TYPE);
        closeButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   getEventHandler().closeLevelDialog();   }
        });
        
        //LEAVE TOWN BUTTON
        Sprite leaveButton = guiButtons.get(LEAVE_TOWN_BUTTON_TYPE);
        leaveButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                //Disable the Overlay and it's buttons
                getGUIDecor().get(GAME_POPUP_TYPE).setEnabled(false);
                getGUIDecor().get(GAME_POPUP_TYPE).setState(INVISIBLE.toString());
                getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
                getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE).setState(INVISIBLE.toString());
                getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
                getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE.toString());
                
                //Switch the state to not started.
                data.setGameState(MiniGameState.NOT_STARTED);
                //Switch to the level select screen.
                getEventHandler().switchToLevelSelectScreen();  
            }
        });
        
        //TRY AGAIN BUTTON
        Sprite tryAgainButton = guiButtons.get(TRY_AGAIN_BUTTON_TYPE);
        tryAgainButton.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {   
                //Reset everything relavent for the level and prep it for replay.
                getEventHandler().resetLevel();
                
                //Disable the Overlay and it's buttons
                getGUIDecor().get(GAME_POPUP_TYPE).setEnabled(false);
                getGUIDecor().get(GAME_POPUP_TYPE).setState(INVISIBLE.toString());
                getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE).setEnabled(false);
                getGUIButtons().get(LEAVE_TOWN_BUTTON_TYPE).setState(INVISIBLE.toString());
                getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setEnabled(false);
                getGUIButtons().get(TRY_AGAIN_BUTTON_TYPE).setState(INVISIBLE.toString());
                
            }
        });
        
        //SPECIALS EVENT HANDLERS GO HERE
        
        //MAKE GREEN HANDLING
        Sprite makeGreen = guiButtons.get(MAKE_GREEN_BUTTON_TYPE);
        makeGreen.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(MAKE_GREEN_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(MAKE_GREEN_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(MAKE_GREEN_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(MAKE_GREEN_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(MAKE_GREEN_SPECIAL_TYPE);
                    getGUIButtons().get(MAKE_GREEN_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //MAKE RED HANDLING
        Sprite makeRed = guiButtons.get(MAKE_RED_BUTTON_TYPE);
        makeRed.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(MAKE_RED_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(MAKE_RED_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(MAKE_RED_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(MAKE_RED_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(MAKE_RED_SPECIAL_TYPE);
                    getGUIButtons().get(MAKE_RED_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //FREEZE TIME HANDLING
//        Sprite freeze = guiButtons.get(FREEZE_BUTTON_TYPE);
//        freeze.setActionListener(new ActionListener(){
//            public void actionPerformed(ActionEvent ae)
//            {
//                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
//                String specialButton;
//                PathXDataModel dataModel = (PathXDataModel) getDataModel();
//                if (activeSpecial.equals(FREEZE_SPECIAL_TYPE)) {
//                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
//                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
//                    getGUIButtons().get(FREEZE_BUTTON_TYPE).setState(VISIBLE.toString());
//                } else if (!activeSpecial.equals("")) {
//                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
//                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
//                    dataModel.setActivatedSpecial(FREEZE_SPECIAL_TYPE);
//                    dataModel.setSpecialActive(true);
//                    getGUIButtons().get(FREEZE_BUTTON_TYPE).setState(ENABLED.toString());
//                } else {
//
//                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
//                    ((PathXDataModel) getDataModel()).setActivatedSpecial(FREEZE_SPECIAL_TYPE);
//                    getGUIButtons().get(FREEZE_BUTTON_TYPE).setState(ENABLED.toString());
//                }
//            }
//        });
        
        //DECREASE SPEED LIMIT
        Sprite decrSpeed = guiButtons.get(DECREASE_SPEED_BUTTON_TYPE);
        decrSpeed.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(DECREASE_SPEED_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(DECREASE_SPEED_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(DECREASE_SPEED_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(DECREASE_SPEED_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(DECREASE_SPEED_SPECIAL_TYPE);
                    getGUIButtons().get(DECREASE_SPEED_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //INCREASE SPEED LIMIT
        Sprite incrSpeed = guiButtons.get(INCREASE_SPEED_BUTTON_TYPE);
        incrSpeed.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(INCREASE_SPEED_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(INCREASE_SPEED_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(INCREASE_SPEED_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(INCREASE_SPEED_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(INCREASE_SPEED_SPECIAL_TYPE);
                    getGUIButtons().get(INCREASE_SPEED_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //INCREASE PLAYER SPEED
        Sprite incrPlayerSpeed = guiButtons.get(INCREASE_PLAYER_SPEED_BUTTON_TYPE);
        incrPlayerSpeed.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                //If this special was active and were trying to deactivate it
                if (activeSpecial.equals(INCREASE_PLAYER_SPEED_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(INCREASE_PLAYER_SPEED_BUTTON_TYPE).setState(VISIBLE.toString());
                } 
                //If there was another special active and we want to activate this one.
                else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(INCREASE_PLAYER_SPEED_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(INCREASE_PLAYER_SPEED_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(INCREASE_PLAYER_SPEED_SPECIAL_TYPE);
                    getGUIButtons().get(INCREASE_PLAYER_SPEED_BUTTON_TYPE).setState(ENABLED.toString());
                }
                
                getEventHandler().increasePlayerSpeed();
            }
        });
        
        //FLAT TIRE
        Sprite flatTire = guiButtons.get(FLAT_TIRE_BUTTON_TYPE);
        flatTire.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(FLAT_TIRE_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(FLAT_TIRE_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(FLAT_TIRE_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(FLAT_TIRE_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(FLAT_TIRE_SPECIAL_TYPE);
                    getGUIButtons().get(FLAT_TIRE_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //EMPTY GAS
        Sprite emptyGas = guiButtons.get(EMPTY_GAS_BUTTON_TYPE);
        emptyGas.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(EMPTY_GAS_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(EMPTY_GAS_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(EMPTY_GAS_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(EMPTY_GAS_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(EMPTY_GAS_SPECIAL_TYPE);
                    getGUIButtons().get(EMPTY_GAS_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //CLOSE ROAD
        Sprite closeRoad = guiButtons.get(CLOSE_ROAD_BUTTON_TYPE);
        closeRoad.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(CLOSE_ROAD_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(CLOSE_ROAD_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(CLOSE_ROAD_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(CLOSE_ROAD_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(CLOSE_ROAD_SPECIAL_TYPE);
                    getGUIButtons().get(CLOSE_ROAD_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //CLOSE INTERSECTION
        Sprite closeNode = guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE);
        closeNode.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(CLOSE_INTERSECTION_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(CLOSE_INTERSECTION_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(CLOSE_INTERSECTION_SPECIAL_TYPE);
                    getGUIButtons().get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //OPEN INTERSECTION
        Sprite openNode = guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE);
        openNode.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(OPEN_INTERSECTION_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(OPEN_INTERSECTION_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(OPEN_INTERSECTION_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(OPEN_INTERSECTION_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(OPEN_INTERSECTION_SPECIAL_TYPE);
                    getGUIButtons().get(OPEN_INTERSECTION_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //STEAL
        Sprite steal = guiButtons.get(STEAL_BUTTON_TYPE);
        steal.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(STEAL_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(STEAL_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(STEAL_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(STEAL_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(STEAL_SPECIAL_TYPE);
                    getGUIButtons().get(STEAL_BUTTON_TYPE).setState(ENABLED.toString());
                }
                
                getEventHandler().steal();
            }
        });
        
        //MIND CONTROL
        Sprite mindControl = guiButtons.get(MIND_CONTROL_BUTTON_TYPE);
        mindControl.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(MIND_CONTROL_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(MIND_CONTROL_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(MIND_CONTROL_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(MIND_CONTROL_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(MIND_CONTROL_SPECIAL_TYPE);
                    getGUIButtons().get(MIND_CONTROL_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //INTANGIBILITY
        Sprite intangibility = guiButtons.get(INTANGIBILITY_BUTTON_TYPE);
        intangibility.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(INTANGIBILITY_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(INTANGIBILITY_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(INTANGIBILITY_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(INTANGIBILITY_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(INTANGIBILITY_SPECIAL_TYPE);
                    getGUIButtons().get(INTANGIBILITY_BUTTON_TYPE).setState(ENABLED.toString());
                }
                
                getEventHandler().intangibility();
            }
        });
        
        //MINDLESS TERROR
        Sprite mindlessTerror = guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE);
        mindlessTerror.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(MINDLESS_TERROR_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(MINDLESS_TERROR_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(MINDLESS_TERROR_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(MINDLESS_TERROR_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(MINDLESS_TERROR_SPECIAL_TYPE);
                    getGUIButtons().get(MINDLESS_TERROR_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //FLYING
        Sprite flying = guiButtons.get(FLYING_BUTTON_TYPE);
        flying.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(FLYING_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(FLYING_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(FLYING_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(FLYING_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(FLYING_SPECIAL_TYPE);
                    getGUIButtons().get(FLYING_BUTTON_TYPE).setState(ENABLED.toString());
                }
            }
        });
        
        //GOD MODE
        Sprite godMode = guiButtons.get(GOD_MODE_BUTTON_TYPE);
        godMode.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
            {
                String activeSpecial = ((PathXDataModel) getDataModel()).getActivatedSpecial();
                String specialButton;
                PathXDataModel dataModel = (PathXDataModel) getDataModel();
                if (activeSpecial.equals(GOD_MODE_SPECIAL_TYPE)) {
                    ((PathXDataModel) getDataModel()).setActivatedSpecial("");
                    ((PathXDataModel) getDataModel()).setSpecialActive(false);
                    getGUIButtons().get(GOD_MODE_BUTTON_TYPE).setState(VISIBLE.toString());
                } else if (!activeSpecial.equals("")) {
                    specialButton = activeSpecial.substring(0, activeSpecial.indexOf("_SPECIAL_TYPE")) + "_BUTTON_TYPE";
                    getGUIButtons().get(specialButton).setState(VISIBLE.toString());
                    dataModel.setActivatedSpecial(GOD_MODE_SPECIAL_TYPE);
                    dataModel.setSpecialActive(true);
                    getGUIButtons().get(GOD_MODE_BUTTON_TYPE).setState(ENABLED.toString());
                } else {

                    ((PathXDataModel) getDataModel()).setSpecialActive(true);
                    ((PathXDataModel) getDataModel()).setActivatedSpecial(GOD_MODE_SPECIAL_TYPE);
                    getGUIButtons().get(GOD_MODE_BUTTON_TYPE).setState(ENABLED.toString());
                }
                
                getEventHandler().godMode();
            }
        });
    }
    
    private void initSettingsHandlers() {
        //Sound toggle
        Sprite soundToggle = guiButtons.get(SOUND_TOGGLE_BUTTON_TYPE);
        soundToggle.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   
                    getEventHandler().toggleSoundRequest();  
                }
        });
        
        //Music Toggle
        Sprite musicToggle = guiButtons.get(MUSIC_TOGGLE_BUTTON_TYPE);
        musicToggle.setActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ae)
                {   
                    getEventHandler().toggleMusicRequest();  
                }
        });
        
        //TODO Game Speed Slider
    }
    
    /**
     * Called when a game is started.
     *
     * @param game
     */
    @Override
    public void reset() {
        data.reset(this);
    }
    /**
     * When invoked, this method results in each button in the GUI testing to
     * see if the x, y coordinates are inside its bounds. If they are, the
     * button's actionPerformed method is invoked and the appropriate event
     * response is executed.
     *
     * @param x the x coordinate on the canvas of the mouse press
     *
     * @param y the y coordinate on the canvas of the mouse press
     *
     * @return true if the mouse press resulted in a button's event handler
     * being executed, false otherwise. This is important because if false is
     * returned, other game logic should proceed.
     */
    @Override
    public boolean processButtonPress(int x, int y)
    {
        PathXDataModel dataModel = (PathXDataModel) data;
        boolean buttonClickPerformed = false;

        // TEST EACH BUTTON
        for (Sprite s : guiButtons.values())
        {
            // THIS METHOD WILL INVOKE actionPeformed WHEN NEEDED
            buttonClickPerformed = s.testForClick(this, x, y);

            // ONLY EXECUTE THE FIRST ONE, SINCE BUTTONS
            // SHOULD NOT OVERLAP
            if (buttonClickPerformed)
            {
                return true;
            }
        }
        
        //TEST FOR LEVEL SPRITES ON THE LEVEL SELECT SCREEN
        if(screenState.equals(LEVEL_SELECT_SCREEN_STATE)){

            // TEST EACH BUTTON
            for (Sprite s : dataModel.getLevelSprites()) {
                // THIS METHOD WILL INVOKE actionPeformed WHEN NEEDED
                buttonClickPerformed = s.testForClick(this, x, y);

            // ONLY EXECUTE THE FIRST ONE, SINCE BUTTONS
                // SHOULD NOT OVERLAP
                if (buttonClickPerformed) {
                    return true;
                }
            }
        }
        
        //Test for road clicks on the game screen
        if(screenState.equals(GAME_SCREEN_STATE) 
                && dataModel.getActivatedSpecial().equals(CLOSE_ROAD_SPECIAL_TYPE)
                && dataModel.getRecord().balance >= 25){
            
            ArrayList<Road> roads = dataModel.getRoads();

            for (Road road : roads) {

                PathXNode[] nodes = road.getNodes();
                Line2D.Double tempLine = new Line2D.Double();
                tempLine.x1 = nodes[0].getX() + 15;
                tempLine.y1 = nodes[0].getY() + 15;
                tempLine.x2 = nodes[1].getX() + 15;
                tempLine.y2 = nodes[1].getY() + 15;
                float distance = (float) tempLine.ptSegDist(data.getLastMouseX(), data.getLastMouseY());
                
                if (distance <= 7){
                    road.close();
                    dataModel.getRecord().balance -= 25;
                }
            }
            
        }
        return false;
    }

    @Override
    public void updateGUI() {
        PathXDataModel dataModel = (PathXDataModel) data;
        // GO THROUGH THE VISIBLE BUTTONS TO TRIGGER MOUSE OVERS
        Iterator<Sprite> buttonsIt = guiButtons.values().iterator();
        
        while (buttonsIt.hasNext())
        {
            Sprite button = buttonsIt.next();
            
            // ARE WE ENTERING A BUTTON?
            if (button.getState().equals(PathXSpriteState.VISIBLE.toString()))
            {
                if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(PathXSpriteState.MOUSE_OVER.toString());
                }
            }
            // ARE WE EXITING A BUTTON?
            else if (button.getState().equals(PathXSpriteState.MOUSE_OVER.toString()))
            {
                 if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY()))
                {
                    button.setState(PathXSpriteState.VISIBLE.toString());
                }
            }

        }
        
        //Check for mouse overs of the PathXLevelSprites
        if (screenState.equals(LEVEL_SELECT_SCREEN_STATE)) {
            Iterator<PathXLevelSprite> levelButtonsIt = dataModel.getLevelSprites().iterator();

            while (levelButtonsIt.hasNext()) {
                Sprite button = levelButtonsIt.next();

                // ARE WE ENTERING A BUTTON?
                if (button.getState().indexOf("MOUSE_OVER") < 0) {
                    if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                        button.setState(button.getState() + "_MOUSE_OVER");
                    }
                } // ARE WE EXITING A BUTTON?
                else if (button.getState().indexOf("MOUSE_OVER") >= 0) {
                    if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                        button.setState(button.getState().substring(0, button.getState().indexOf("_MOUSE_OVER")));
                    }
                }
            }
        }
        
        //Check for mouse overs of Game Nodes
        if (screenState.equals(GAME_SCREEN_STATE) && data.inProgress()) {
            ArrayList<Sprite> gameSprites = new ArrayList();
            gameSprites.addAll(dataModel.getNodes());

            
            Iterator<Sprite> gameButtonsIt = gameSprites.iterator();

            while (gameButtonsIt.hasNext()) {
                Sprite button = gameButtonsIt.next();

                // ARE WE ENTERING A NODE?
                if (button.getState().indexOf("MOUSE_OVER") < 0 && button.getState().indexOf("HIGHLIGHTED") < 0) {
                    if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                        button.setState(button.getState() + "_MOUSE_OVER");
                    }
                } // ARE WE EXITING A NODE?
                else if (button.getState().indexOf("MOUSE_OVER") >= 0) {
                    if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                        button.setState(button.getState().substring(0, button.getState().indexOf("_MOUSE_OVER")));
                    }
                }
            }
        }
        
        //Check for mouse overs of Roads
        if (screenState.equals(GAME_SCREEN_STATE)){
            
            ArrayList<Road> roads = ((PathXDataModel) data).getRoads();

            for (Road road : roads) {

                PathXNode[] nodes = road.getNodes();
                Line2D.Double tempLine = new Line2D.Double();
                tempLine.x1 = nodes[0].getX() + 15;
                tempLine.y1 = nodes[0].getY() + 15;
                tempLine.x2 = nodes[1].getX() + 15;
                tempLine.y2 = nodes[1].getY() + 15;
                float distance = (float) tempLine.ptSegDist(data.getLastMouseX(), data.getLastMouseY());

                //If we haven't moused over this road, check if we are now moused over.
                if (road.getCurrentState().indexOf(MOUSE_OVER.toString()) < 0) {
                    if (distance <= 7) {
                        road.setState(road.getCurrentState() + "_MOUSE_OVER");
                    }
                } //If we have moused over this road check if we have left it or not.
                else {
                    if (distance > 8) {
                        road.setState(road.getCurrentState().substring(0, road.getCurrentState().indexOf("_MOUSE_OVER")));
                    }
                }
            }
        }
        
        //Check for mouse overs of cars
        if (screenState.equals(GAME_SCREEN_STATE)) {
            ArrayList<Sprite> gameSprites = new ArrayList();
            gameSprites.addAll(dataModel.getCops());
            gameSprites.addAll(dataModel.getBandits());
            gameSprites.addAll(dataModel.getZombies());
            
            Iterator<Sprite> gameButtonsIt = gameSprites.iterator();

            while (gameButtonsIt.hasNext()) {
                Sprite button = gameButtonsIt.next();

                // ARE WE ENTERING A BUTTON?
                if (button.getState().equals(VISIBLE.toString())) {
                    if (button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                        button.setState(MOUSE_OVER.toString());
                    }
                } // ARE WE EXITING A BUTTON?
                else if (button.getState().equals(MOUSE_OVER.toString())) {
                    if (!button.containsPoint(data.getLastMouseX(), data.getLastMouseY())) {
                        button.setState(VISIBLE.toString());
                    }
                }
            }
        }
    }  

    private void initLevelSelectViewport() {
        //Create the viewport
        levelSelectViewport = new Viewport();
        
        //Specify sizes and location for the viewport
        levelSelectViewport.setScreenSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        levelSelectViewport.setViewportSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        levelSelectViewport.setGameWorldSize(MAP_WIDTH, MAP_HEIGHT);
        levelSelectViewport.setNorthPanelHeight(LEVEL_SELECT_NORTH_PANEL_HEIGHT);
        //levelSelect.initViewportMargins();
        levelSelectViewport.setMaxViewportX(MAP_WIDTH - VIEWPORT_WIDTH);
        levelSelectViewport.setMaxViewportY(MAP_HEIGHT - VIEWPORT_HEIGHT);
        
        data.setViewport(levelSelectViewport);
    }

    public PathXEventHandler getEventHandler() {
        return eventHandler;
    }

    private void activateSpecialButtons() {
        guiButtons.get(MAKE_GREEN_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(MAKE_GREEN_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MAKE_RED_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(MAKE_RED_BUTTON_TYPE).setEnabled(true);
//        guiButtons.get(FREEZE_BUTTON_TYPE).setState(VISIBLE.toString());
//        guiButtons.get(FREEZE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INCREASE_SPEED_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(INCREASE_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(DECREASE_SPEED_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(DECREASE_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INCREASE_PLAYER_SPEED_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(INCREASE_PLAYER_SPEED_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(FLYING_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(true);
        guiButtons.get(GOD_MODE_BUTTON_TYPE).setState(VISIBLE.toString());
        guiButtons.get(GOD_MODE_BUTTON_TYPE).setEnabled(true);
    }

    private void deactivateSpecialButtons() {
        guiButtons.get(MAKE_GREEN_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(MAKE_GREEN_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MAKE_RED_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(MAKE_RED_BUTTON_TYPE).setEnabled(false);
//        guiButtons.get(FREEZE_BUTTON_TYPE).setState(INVISIBLE.toString());
//        guiButtons.get(FREEZE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCREASE_SPEED_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(INCREASE_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(DECREASE_SPEED_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(DECREASE_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INCREASE_PLAYER_SPEED_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(INCREASE_PLAYER_SPEED_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(FLAT_TIRE_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(EMPTY_GAS_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(CLOSE_ROAD_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(OPEN_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(CLOSE_INTERSECTION_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(STEAL_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(STEAL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(MIND_CONTROL_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(INTANGIBILITY_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(MINDLESS_TERROR_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(FLYING_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(FLYING_BUTTON_TYPE).setEnabled(false);
        guiButtons.get(GOD_MODE_BUTTON_TYPE).setState(INVISIBLE.toString());
        guiButtons.get(GOD_MODE_BUTTON_TYPE).setEnabled(false);
    }

    private void initGameViewport() {
        gameViewport = new Viewport();
        
        //Specify sizes and location for the viewport
        gameViewport.setScreenSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        gameViewport.setViewportSize(GAME_VIEWPORT_WIDTH, GAME_VIEWPORT_HEIGHT);
        //gameViewport.setGameWorldSize(MAP_WIDTH, MAP_HEIGHT);
        gameViewport.setNorthPanelHeight(GAME_VIEWPORT_Y);
        //levelSelect.initViewportMargins();
        //gameViewport.updateViewportBoundaries();
        //levelSelect.setViewportSize(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        
        ((PathXDataModel) data).setGameViewport(gameViewport);
    }

}
