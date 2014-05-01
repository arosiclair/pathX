/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andrew
 */
public class PathXConstants {
    // WE NEED THESE CONSTANTS JUST TO GET STARTED
    // LOADING SETTINGS FROM OUR XML FILES
    //public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String PROPERTIES_FILE_NAME = "properties.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";    
    public static String PATH_DATA = "./data/";
    public static String PATH_LEVEL_SCHEMA = PATH_DATA + "levels/PathXLevelSchema.xsd";
    // THESE ARE THE TYPES OF CONTROLS, WE USE THESE CONSTANTS BECAUSE WE'LL
    // STORE THEM BY TYPE, SO THESE WILL PROVIDE A MEANS OF IDENTIFYING THEM
    
    // EACH SCREEN HAS ITS OWN BACKGROUND TYPE
    public static final String BACKGROUND_TYPE = "BACKGROUND_TYPE";
    
    //IN-GAME UI CONTROL TYPES
    //Main Menu
    public static final String PLAY_BUTTON_TYPE = "PLAY_BUTTON_TYPE";
    public static final String RESET_BUTTON_TYPE = "RESET_GAME_BUTTON_TYPE";
    public static final String SETTINGS_BUTTON_TYPE = "SETTINGS_GAME_BUTTON_TYPE";
    public static final String HELP_BUTTON_TYPE = "HELP_GAME_BUTTON_TYPE";
    
    //Level Select Screen
    public static final String BACK_BUTTON_TYPE = "BACK_BUTTON_TYPE";
    public static final String QUIT_BUTTON_TYPE = "QUIT_BUTTON_TYPE";
    public static final String UP_ARROW_BUTTON_TYPE = "UP_ARROW_BUTTON_TYPE";
    public static final String DOWN_ARROW_BUTTON_TYPE = "DOWN_ARROW_BUTTON_TYPE";
    public static final String RIGHT_ARROW_BUTTON_TYPE = "RIGHT_ARROW_BUTTON_TYPE";
    public static final String LEFT_ARROW_BUTTON_TYPE = "LEFT_ARROW_BUTTON_TYPE";
    public static final String MAP_TYPE = "MAP_TYPE";
    public static final String LEVEL_TYPE = "LEVEL_TYPE";
    public static final String LOCKED_LEVEL_TYPE = "LOCKED_LEVEL_TYPE";
    public static final String INCOMPLETE_LEVEL_TYPE = "INCOMPLETE_LEVEL_TYPE";
    public static final String COMPLETE_LEVEL_TYPE = "COMPLETE_LEVEL_TYPE";
    public static final String CLOSE_BUTTON_TYPE = "CLOSE_BUTTON_TYPE";
    public static final int VIEWPORT_X = 0;
    public static final int VIEWPORT_Y = 112;
    public static final int VIEWPORT_WIDTH = 640;
    public static final int VIEWPORT_HEIGHT = 368;
    public static final int LEVEL_SELECT_NORTH_PANEL_HEIGHT = 112;
    public static final int MAP_WIDTH = 1200;
    public static final int MAP_HEIGHT = 662;
    
    //USED FOR THE SETTINGS SCREEN.
    public static final String SOUND_TOGGLE_BUTTON_TYPE = "SOUND_TOGGLE_BUTTON_TYPE";
    public static final String MUSIC_TOGGLE_BUTTON_TYPE = "MUSIC_TOGGLE_BUTTON_TYPE";
    
    //Game Screen
    public static final String START_BUTTON_TYPE = "START_BUTTON_TYPE";
    public static final String GAME_QUIT_BUTTON_TYPE = "GAME_QUIT_BUTTON_TYPE";
    public static final String GAME_BACK_BUTTON_TYPE = "GAME_BACK_BUTTON_TYPE";
    public static final String GAME_POPUP_TYPE = "GAME_POPUP_TYPE";
    public static final String POPUP_CLOSE_BUTTON_TYPE = "POPUP_CLOSE_BUTTON_TYPE";
    
    //THE GAME'S VIEWPORT LOCATION
    public static final int GAME_VIEWPORT_X = 160;
    public static final int GAME_VIEWPORT_Y = 20;
    public static final int GAME_VIEWPORT_WIDTH = 480;
    public static final int GAME_VIEWPORT_HEIGHT = 440;
    
    //GAME SPECIALS
    public static final String MAKE_GREEN_BUTTON_TYPE = "MAKE_GREEN_BUTTON_TYPE";
    public static final String MAKE_RED_BUTTON_TYPE = "MAKE_RED_BUTTON_TYPE";
    public static final String FREEZE_BUTTON_TYPE = "FREEZE_BUTTON_TYPE";
    public static final String INCREASE_SPEED_BUTTON_TYPE = "INCREASE_SPEED_BUTTON_TYPE";
    public static final String DECREASE_SPEED_BUTTON_TYPE = "DECREASE_SPEED_BUTTON_TYPE";
    public static final String INCREASE_PLAYER_SPEED_BUTTON_TYPE = "INCREASE_PLAYER_SPEED_BUTTON_TYPE";
    public static final String EMPTY_GAS_BUTTON_TYPE = "EMPTY_GAS_BUTTON_TYPE";
    public static final String FLAT_TIRE_BUTTON_TYPE = "FLAT_TIRE_BUTTON_TYPE";
    public static final String CLOSE_ROAD_BUTTON_TYPE = "CLOSE_ROAD_BUTTON_TYPE";
    public static final String CLOSE_INTERSECTION_BUTTON_TYPE = "CLOSE_INTERSECTION_BUTTON_TYPE";
    public static final String OPEN_INTERSECTION_BUTTON_TYPE = "OPEN_INTERSECTION_BUTTON_TYPE";
    public static final String STEAL_BUTTON_TYPE = "STEAL_BUTTON_TYPE";
    public static final String MIND_CONTROL_BUTTON_TYPE = "MIND_CONTROL_BUTTON_TYPE";
    public static final String INTANGIBILITY_BUTTON_TYPE = "INTANGIBILITY_BUTTON_TYPE";
    public static final String MINDLESS_TERROR_BUTTON_TYPE = "MINDLESS_TERROR_BUTTON_TYPE";
    public static final String FLYING_BUTTON_TYPE = "FLYING_BUTTON_TYPE";
    public static final String GOD_MODE_BUTTON_TYPE = "GOD_MODE_BUTTON_TYPE";
    
    public static final String PLAYER_TYPE = "PLAYER_TYPE";
    public static final String NODE_TYPE = "NODE_TYPE";
    public static final String CAR_TYPE = "CAR_TYPE";
    public static final String COP_CAR_TYPE = "COP_CAR_TYPE";
    public static final String BANDIT_CAR_TYPE = "BANDIT_CAR_TYPE";
    public static final String ZOMBIE_CAR_TYPE = "ZOMBIE_CAR_TYPE";
    public static final String ROAD_TYPE = "ROAD_TYPE";
    
    public static final String LEVEL_FAIL_DIALOG_TYPE = "LEVEL_FAIL_DIALOG_TYPE";
    
    public static final String MENU_SCREEN_STATE = "MENU_SCREEN_STATE";
    public static final String LEVEL_SELECT_SCREEN_STATE = "LEVEL_SELECT_SCREEN_STATE";
    public static final String SETTINGS_SCREEN_STATE = "SETTINGS_SCREEN_STATE";
    public static final String GAME_SCREEN_STATE = "GAME_SCREEN_STATE";
    public static final String HELP_SCREEN_STATE = "HELP_SCREEN_STATE";
    // ANIMATION SPEED
    public static final int FPS = 50;
    // UI CONTROL SIZE AND POSITION SETTINGS
    public static final int WINDOW_WIDTH = 640;
    public static final int WINDOW_HEIGHT = 480;
    
    //MAP LEVEL COORDINATES
    public static final int[] LEVEL_X_POS = {70, 97, 140, 155, 180, 205, 245, 250, 250, 290, 255, 
        290, 360, 430, 395, 355, 400, 323, 340, 403};
    public static final int[] LEVEL_Y_POS = {280, 330, 380, 280, 225, 150, 175, 220, 290, 290, 360,
        430, 425, 420, 360, 310, 273, 184, 138, 137};
    
    public static final int MIN_CAR_VELOCITY = 5;
    public static final int MAX_CAR_VELOCITY = 20;
    
    // UI CONTROLS POSITIONS IN THE GAME SCREEN
    public static final int MENU_BUTTON_WIDTH = 100;
    public static final int MENU_BUTTON_GAP = 50;
    public static final int PLAY_BUTTON_X = 50;
    public static final int PLAY_BUTTON_Y = 350;
    public static final int MAIN_QUIT_BUTTON_X = 550;
    public static final int MAIN_QUIT_BUTTON_Y = 50;
    
    public static final int LEVEL_SELECT_QUIT_BUTTON_X = MAIN_QUIT_BUTTON_X;
    public static final int LEVEL_SELECT_QUIT_BUTTON_Y = MAIN_QUIT_BUTTON_Y;
    public static final int LEVEL_SELECT_BACK_BUTTON_X = LEVEL_SELECT_QUIT_BUTTON_X - 5;
    public static final int LEVEL_SELECT_BALANCE_X = 320;
    public static final int LEVEL_SELECT_BALANCE_Y = 58;
    public static final int LEVEL_SELECT_GOAL_X = 275;
    public static final int LEVEL_SELECT_GOAL_Y = 86;
    public static final int LEVEL_SELECT_CITY_X = 460;
    public static final int LEVEL_SELECT_CITY_Y = 430;
    public static final int LEFT_ARROW_X = 30;
    public static final int LEFT_ARROW_Y = 370;
    public static final int UP_ARROW_X = 60;
    public static final int UP_ARROW_Y = 340;
    public static final int RIGHT_ARROW_X = 90;
    public static final int RIGHT_ARROW_Y = LEFT_ARROW_Y;
    public static final int DOWN_ARROW_X = UP_ARROW_X;
    public static final int DOWN_ARROW_Y = 400;
    
    public static final int SOUND_TOGGLE_X = 220;
    public static final int SOUND_TOGGLE_Y = 170;
    public static final int MUSIC_TOGGLE_X = SOUND_TOGGLE_X;
    public static final int MUSIC_TOGGLE_Y = 230;
    
    public static final int GAME_BACK_BUTTON_X = 20;
    public static final int GAME_BACK_BUTTON_Y = 30;
    public static final int GAME_QUIT_BUTTON_X = 80;
    public static final int GAME_QUIT_BUTTON_Y = 30;
    public static final int GAME_START_BUTTON_X = 23;
    public static final int GAME_START_BUTTON_Y = 100;
    public static final int GAME_SPECIAL_WIDTH = 30;
    public static final int GAME_SPECIAL_HEIGHT = 30;
    public static final int FIRST_SPECIAL_X = 15;
    public static final int FIRST_SPECIAL_Y = 187;
    public static final int GAME_CITY_LABEL_X = 165;
    public static final int GAME_CITY_LABEL_Y = 55;
    public static final int GAME_REWARD_LABEL_X = 165;
    public static final int GAME_REWARD_LABEL_Y = 55;
    
    public static final int GAME_OVERLAY_X = 200;
    public static final int GAME_OVERLAY_Y = 90;
    public static final int OVERLAY_CITY_LABEL_X = GAME_OVERLAY_X + 20;
    public static final int OVERLAY_CITY_LABEL_Y = GAME_OVERLAY_Y + 20;
    public static final int OVERLAY_LEVEL_DIALOG_X = OVERLAY_CITY_LABEL_X;
    public static final int OVERLAY_LEVEL_DIALOG_Y = OVERLAY_CITY_LABEL_Y + 50;
    public static final int OVERLAY_BUTTON_CLOSE_X = GAME_OVERLAY_X + 150;
    public static final int OVERLAY_BUTTON_CLOSE_Y = GAME_OVERLAY_Y + 200;
    
    
    
    // FONTS USED DURING FOR TEXTUAL GAME DISPLAYS
    public static final Font FONT_TEXT_DISPLAY = new Font(Font.SANS_SERIF, Font.BOLD, 24);
    public static final Font FONT_LEVEL_DISPLAY = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
    public static final Font FONT_DEBUG_TEXT = new Font(Font.MONOSPACED, Font.BOLD, 14);
    public static final Font FONT_STATS = new Font(Font.MONOSPACED, Font.BOLD, 20);
    
}
