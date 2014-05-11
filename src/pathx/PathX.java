/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx;

import mini_game.MiniGame;
import properties_manager.PropertiesManager;
import static pathx.PathXConstants.*;
import pathx.ui.PathXErrorHandler;
import pathx.ui.PathXMiniGame;
import xml_utilities.InvalidXMLFileFormatException;
/**
 *
 * @author Andrew
 */
public class PathX {

    // THIS HAS THE FULL USER INTERFACE AND ONCE IN EVENT
    // HANDLING MODE, BASICALLY IT BECOMES THE FOCAL
    // POINT, RUNNING THE UI AND EVERYTHING ELSE
    static PathXMiniGame miniGame = new PathXMiniGame();
    
    
    public static void main(String[] args) {
        try{
            //Load the app settings and properties.
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PathXConstants.PATH_DATA);
            props.loadProperties(PROPERTIES_FILE_NAME, PROPERTIES_SCHEMA_FILE_NAME);
            
            // THEN WE'LL LOAD THE GAME FLAVOR AS SPECIFIED BY THE PROPERTIES FILE
            String gameFlavorFile = props.getProperty(PathXPropertyType.FILE_GAME_PROPERTIES);
            props.loadProperties(gameFlavorFile, PROPERTIES_SCHEMA_FILE_NAME);
            
            // NOW WE CAN LOAD THE UI, WHICH WILL USE ALL THE FLAVORED CONTENT
            String appTitle = props.getProperty(PathXPropertyType.TEXT_TITLE_BAR);
            miniGame.initMiniGame(appTitle, FPS, WINDOW_WIDTH + 4, WINDOW_HEIGHT + 26);
            
            miniGame.startGame();
        }
        catch (InvalidXMLFileFormatException ixmlffe){
            // LET THE ERROR HANDLER PROVIDE THE RESPONSE
            PathXErrorHandler errorHandler = miniGame.getErrorHandler();
            errorHandler.processError(PathXPropertyType.TEXT_ERROR_LOADING_XML_FILE);
        }
    }
    public enum PathXPropertyType {
        // LOADED FROM properties.xml

        /* SETUP FILE NAMES */
        FILE_GAME_PROPERTIES,
        FILE_PLAYER_RECORD,
        /* DIRECTORY PATHS FOR FILE LOADING */
        PATH_AUDIO,
        PATH_IMG,
        // LOADED FROM THE GAME FLAVOR PROPERTIES XML FILE
        
        // sorting_hat_properties.xml

        /* IMAGE FILE NAMES */
        IMAGE_BUTTON_QUIT,
        IMAGE_BUTTON_QUIT_MOUSE_OVER,
        IMAGE_WINDOW_ICON,
        
        //Splash images
        IMAGE_BACKGROUND_MENU,
        IMAGE_BUTTON_PLAY,
        IMAGE_BUTTON_PLAY_MOUSE_OVER,
        IMAGE_BUTTON_RESET,
        IMAGE_BUTTON_RESET_MOUSE_OVER,
        IMAGE_BUTTON_SETTINGS,
        IMAGE_BUTTON_SETTINGS_MOUSE_OVER,
        IMAGE_BUTTON_HELP,
        IMAGE_BUTTON_HELP_MOUSE_OVER,
        
        //Level select images
        IMAGE_BACKGROUND_LEVEL,
        IMAGE_BUTTON_BACK,
        IMAGE_BUTTON_BACK_MOUSE_OVER,
        IMAGE_UP_ARROW,
        IMAGE_UP_ARROW_MOUSE_OVER,
        IMAGE_DOWN_ARROW,
        IMAGE_DOWN_ARROW_MOUSE_OVER,
        IMAGE_RIGHT_ARROW,
        IMAGE_RIGHT_ARROW_MOUSE_OVER,
        IMAGE_LEFT_ARROW,
        IMAGE_LEFT_ARROW_MOUSE_OVER,
        IMAGE_MAP,
        IMAGE_LOCKED_LEVEL,
        IMAGE_LOCKED_LEVEL_MOUSE_OVER,
        IMAGE_INCOMPLETE_LEVEL,
        IMAGE_INCOMPLETE_LEVEL_MOUSE_OVER,
        IMAGE_COMPLETED_LEVEL,
        IMAGE_COMPLETED_LEVEL_MOUSE_OVER,
        
        
        //In-game images
        IMAGE_BACKGROUND_GAME,
        IMAGE_POPUP_BACKGROUND,
        IMAGE_BUTTON_CLOSE,
        IMAGE_BUTTON_CLOSE_MOUSE_OVER,
        IMAGE_BUTTON_TRY_AGAIN,
        IMAGE_BUTTON_TRY_AGAIN_MOUSE_OVER,
        IMAGE_BUTTON_LEAVE_TOWN,
        IMAGE_BUTTON_LEAVE_TOWN_MOUSE_OVER,
        IMAGE_BUTTON_START,
        IMAGE_BUTTON_START_MOUSE_OVER,
        IMAGE_BUTTON_PAUSE,
        IMAGE_BUTTON_UNPAUSE,
        IMAGE_BANK_ICON,
        IMAGE_MAKE_GREEN,
        IMAGE_MAKE_RED,
        IMAGE_FREEZE_TIME,
        IMAGE_DECREASE_SPEED,
        IMAGE_INCREASE_SPEED,
        IMAGE_INCREASE_PLAYER_SPEED,
        IMAGE_FLAT_TIRE,
        IMAGE_EMPTY_GAS,
        IMAGE_CLOSE_ROAD,
        IMAGE_CLOSE_NODE,
        IMAGE_OPEN_NODE,
        IMAGE_STEALING,
        IMAGE_MIND_CONTROL,
        IMAGE_INTANGIBILITY,
        IMAGE_MINDLESS_TERROR,
        IMAGE_FLYING,
        IMAGE_GOD_MODE,
        IMAGE_MAKE_GREEN_ACTIVE,
        IMAGE_MAKE_RED_ACTIVE,
        IMAGE_FREEZE_TIME_ACTIVE,
        IMAGE_DECREASE_SPEED_ACTIVE,
        IMAGE_INCREASE_SPEED_ACTIVE,
        IMAGE_INCREASE_PLAYER_SPEED_ACTIVE,
        IMAGE_FLAT_TIRE_ACTIVE,
        IMAGE_EMPTY_GAS_ACTIVE,
        IMAGE_CLOSE_ROAD_ACTIVE,
        IMAGE_CLOSE_NODE_ACTIVE,
        IMAGE_OPEN_NODE_ACTIVE,
        IMAGE_STEALING_ACTIVE,
        IMAGE_MIND_CONTROL_ACTIVE,
        IMAGE_INTANGIBILITY_ACTIVE,
        IMAGE_MINDLESS_TERROR_ACTIVE,
        IMAGE_FLYING_ACTIVE,
        IMAGE_GOD_MODE_ACTIVE,
        
        IMAGE_GREEN_INTERSECTION,
        IMAGE_GREEN_INTERSECTION_MOUSE_OVER,
        IMAGE_RED_INTERSECTION,
        IMAGE_RED_INTERSECTION_MOUSE_OVER,
        IMAGE_CLOSED_INTERSECTION,
        IMAGE_CLOSED_INTERSECTION_MOUSE_OVER,
        IMAGE_HOUSE,
        IMAGE_BANK,
        
        IMAGE_PLAYER_CAR,
        IMAGE_PLAYER_CAR_MOUSE_OVER,
        IMAGE_COP_CAR,
        IMAGE_COP_CAR_MOUSE_OVER,
        IMAGE_BANDIT_CAR,
        IMAGE_BANDIT_CAR_MOUSE_OVER,
        IMAGE_ZOMBIE_CAR,
        IMAGE_ZOMBIE_CAR_MOUSE_OVER,
        
        //Settings view images
        IMAGE_BACKGROUND_SETTINGS,
        IMAGE_BUTTON_TOGGLE_OFF,
        IMAGE_BUTTON_TOGGLE_ON,
        
        IMAGE_BACKGROUND_HELP,
        
        //Used to load details for every level
        LEVEL_OPTIONS,
        
        //GAME TEXT
        TEXT_ERROR_LOADING_AUDIO,
        TEXT_ERROR_LOADING_LEVEL,
        TEXT_ERROR_LOADING_RECORD,
        TEXT_ERROR_LOADING_XML_FILE,
        TEXT_ERROR_SAVING_RECORD,
        TEXT_TITLE_BAR,
        TEXT_CLOSE_LEVEL_PROMPT,
        TEXT_RETRY_LEVEL,
        TEXT_EXIT_LEVEL,
        TEXT_LEVEL_FAIL,
        
        //AUDIO CUES
        AUDIO_CUE_ACTIVATE_SPECIAL,
        AUDIO_CUE_LOW_CASH,
        AUDIO_CUE_WIN,
        AUDIO_CUE_FAIL,
        SONG_CUE_GAME_SCREEN,
        SONG_CUE_MENU_SCREEN, 
        
        

    }
}    

