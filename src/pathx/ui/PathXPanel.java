/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import javax.swing.JPanel;
import mini_game.MiniGame;
import mini_game.MiniGameState;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import pathx.PathX.PathXPropertyType;
import pathx.PathXConstants;
import static pathx.PathXConstants.BACKGROUND_TYPE;
import static pathx.PathXConstants.FONT_TEXT_DISPLAY;
import static pathx.PathXConstants.GAME_SCREEN_STATE;
import static pathx.PathXConstants.GAME_VIEWPORT_HEIGHT;
import static pathx.PathXConstants.GAME_VIEWPORT_WIDTH;
import static pathx.PathXConstants.GAME_VIEWPORT_X;
import static pathx.PathXConstants.GAME_VIEWPORT_Y;
import static pathx.PathXConstants.LEVEL_SELECT_SCREEN_STATE;
import static pathx.PathXConstants.PATH_DATA;
import static pathx.PathXConstants.VIEWPORT_HEIGHT;
import static pathx.PathXConstants.VIEWPORT_WIDTH;
import static pathx.PathXConstants.VIEWPORT_X;
import static pathx.PathXConstants.VIEWPORT_Y;
import pathx.data.PathXDataModel;
import pathx.data.PathXLevel;
import static pathx.ui.PathXSpriteState.VISIBLE;
import properties_manager.PropertiesManager;

/**
 *
 * @author Andrew
 */
public class PathXPanel extends JPanel{
    
    private MiniGame game;
    
    private PathXDataModel dataModel;
    private PathXEventHandler eventHandler;
    
    private NumberFormat numberFormatter;
    private PropertiesManager props = PropertiesManager.getPropertiesManager();
    
    private BufferedImage map;
    
    /**
     * This constructor stores the game and data references,
     * which we'll need for rendering.
     * 
     * @param initGame The pathX game that is using
     * this panel for rendering.
     * 
     * @param initData The pathX game data.
     */
    public PathXPanel(MiniGame initGame, PathXDataModel initData){
        
        game = initGame;
        dataModel = initData;
        numberFormatter = NumberFormat.getNumberInstance();
        numberFormatter.setMinimumFractionDigits(3);
        numberFormatter.setMaximumFractionDigits(3);
        map = game.loadImage(props.getProperty(PathXPropertyType.PATH_IMG)
                + props.getProperty(PathXPropertyType.IMAGE_MAP));
        eventHandler = ((PathXMiniGame)game).getEventHandler();
    }
    
    /**
     * This is where rendering starts. This method is called each frame, and the
     * entire game application is rendered here with the help of a number of
     * helper methods.
     * 
     * @param g The Graphics context for this panel.
     */
    @Override
    public void paintComponent(Graphics g){
        
        try{
            // MAKE SURE WE HAVE EXCLUSIVE ACCESS TO THE GAME DATA
            game.beginUsingData();
            
            // CLEAR THE PANEL
            super.paintComponent(g);
            
            
            
            //Render these things if the player is at the Game Screen.
            if (((PathXMiniGame)game).isCurrentScreenState(GAME_SCREEN_STATE)){
                
                renderLevelBackground(g);
                
                //RENDER THE GRAPH, INCLUDING THE ROADS AND NODES.
                renderGraph(g);
                
                renderStartAndEnd(g);
                
                //RENDER THE CARS IF THE LEVEL HASN'T BEEN WON YET
                if (!dataModel.won())
                    renderCars(g);
                
                //RENDER DIALOGS IF THERE ARE ANY.
                //renderDialogs(g);
                
                renderLevelName(g);
            
            
            }
            if (((PathXMiniGame) game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE)) {
                renderMap(g);
                
                renderLevelSprites(g);
            }
            
            // RENDER THE BACKGROUND, WHICHEVER SCREEN WE'RE ON
            renderBackground(g);
            
            if (((PathXMiniGame) game).isCurrentScreenState(LEVEL_SELECT_SCREEN_STATE))
                renderLevelSelectStats(g);
            
            //RENDER BUTTONS AND DECOR
            renderGUIControls(g);
            
            if(dataModel.getGameState().equals(MiniGameState.WIN) || dataModel.getGameState().equals(MiniGameState.LOSS)){
                
                renderDialog(dataModel.getGameState(), g);
            }
            
//          renderStats(g);
            
            
        }
        finally {
            game.endUsingData();
        }
    }
    
    public void renderBackground(Graphics g){
        
        Sprite bg = game.getGUIDecor().get(BACKGROUND_TYPE);
        renderSprite(g, bg);
    }

    private void renderSprite(Graphics g, Sprite s) {
     
     // ONLY RENDER THE VISIBLE ONES
     if (!s.getState().equals(PathXSpriteState.INVISIBLE.toString())){
         SpriteType bgST = s.getSpriteType();
         Image img = bgST.getStateImage(s.getState());
         g.drawImage(img, (int)s.getX(), (int)s.getY(), bgST.getWidth(), bgST.getHeight(), null);
     }
        
    }

    private void renderGUIControls(Graphics g) {
        
        Collection<Sprite> decorSprites = game.getGUIDecor().values();
        for (Sprite s : decorSprites) 
        {
            if (!s.getSpriteType().getSpriteTypeID().equals(BACKGROUND_TYPE))
                renderSprite (g, s);
        
        }
        
        Collection<Sprite> buttonSprites = game.getGUIButtons().values();
        for (Sprite s : buttonSprites)
            if (s.isEnabled())
                renderSprite(g, s);
        
    }

    private void renderMap(Graphics g) {
        Viewport vp = dataModel.getViewport();
        int vpx = vp.getViewportX();
        int vpy = vp.getViewportY();
        g.drawImage(map, VIEWPORT_X, VIEWPORT_Y, VIEWPORT_X + VIEWPORT_WIDTH, 
                VIEWPORT_Y + VIEWPORT_HEIGHT, vpx, vpy, vpx + VIEWPORT_WIDTH , 
                vpy + VIEWPORT_HEIGHT, this);
    }

    private void renderLevelSelectStats(Graphics g) {
        g.setFont(FONT_TEXT_DISPLAY);
        String balance = "$";
        balance += dataModel.getRecord().balance;
        g.drawString(balance, PathXConstants.LEVEL_SELECT_BALANCE_X, PathXConstants.LEVEL_SELECT_BALANCE_Y);
        String goal = "$50000";
        g.drawString(goal, PathXConstants.LEVEL_SELECT_GOAL_X, PathXConstants.LEVEL_SELECT_GOAL_Y);
    }
    
    public void renderLevelInfo(Graphics g, PathXLevel level){
        String name = level.getLevelName();
        int reward = level.getReward();
        String info = name + " - $" + reward;
        
        //g.
        g.setFont(FONT_TEXT_DISPLAY);
        g.setColor(Color.BLACK);
        g.drawString(info, PathXConstants.LEVEL_SELECT_CITY_X, PathXConstants.LEVEL_SELECT_CITY_Y);
        
    }

    private void renderLevelSprites(Graphics g) {
        for (PathXLevelSprite ls : dataModel.getLevelSprites()){
            ls.setEnabled(true);
            ls.update();
            renderSprite(g, ls);
            
            //Render the level's info on the level select screen if it's 
            //moused-over
            if (ls.getState().indexOf("MOUSE_OVER") >= 0)
                renderLevelInfo(g, ls.getLevel());
        }          
    }

    private void renderGraph(Graphics g) {
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        Viewport vp = data.getGameViewport();
        Graphics2D g2 = (Graphics2D) g;
        //Render the Roads first
        ArrayList<Road> roads = data.getRoads();

        //Render each of the roads using our PathXPanel's Graphics component.
        //Taking account for the Viewport
        int startX, startY, endX, endY;
        for(Road road: roads){
            PathXNode[] nodes = road.getNodes();
            startX = VIEWPORT_X + (int) nodes[0].getConstantXPos() - vp.getViewportX() + 15;
            startY = VIEWPORT_Y + (int) nodes[0].getConstantYPos() - vp.getViewportY() + 15;
            endX = VIEWPORT_X + (int) nodes[1].getConstantXPos() - vp.getViewportX() + 15;
            endY = VIEWPORT_Y + (int) nodes[1].getConstantYPos() - vp.getViewportY() + 15;
            
            //Draw this road in either green or black depedning on its state.
            String state = road.getCurrentState();
            if (state.indexOf(PathXSpriteState.MOUSE_OVER.toString()) >= 0)
                g2.setColor(new Color(34, 177, 76));
            else
                g2.setColor(Color.BLACK);
            
            g2.setStroke(new BasicStroke(10));
            g2.draw(new Line2D.Float(startX, startY, endX, endY));
        }
        
        //Render the nodes. Taking account for the Viewport
        ArrayList<PathXNode> nodes = data.getNodes();
        for (PathXNode node : nodes){
            node.setX(VIEWPORT_X + node.getConstantXPos() - vp.getViewportX());
            node.setY(VIEWPORT_Y + node.getConstantYPos() - vp.getViewportY());
            renderSprite(g, node);
        }
    }

    private void renderCars(Graphics g) {
          PathXDataModel data = (PathXDataModel) game.getDataModel();
        Viewport vp = data.getGameViewport();
        //Get our collection of Car Sprites
        ArrayList<CopCar> cops = data.getCops();
        ArrayList<BanditCar> bandits = data.getBandits();
        ArrayList<ZombieCar> zombies = data.getZombies();
        PlayerCar playerCar = data.getPlayer();
        
        //Render the cops
        for (CopCar car : cops){
            //Adjust for the viewport
            car.setX(VIEWPORT_X + car.getConstantXPos() - vp.getViewportX());
            car.setY(VIEWPORT_Y + car.getConstantYPos() - vp.getViewportY());
            renderSprite(g, car);
        }
        
        //Render the bandits
        for (BanditCar car : bandits){
            //Adjust for the viewport
            car.setX(VIEWPORT_X + car.getConstantXPos() - vp.getViewportX());
            car.setY(VIEWPORT_Y + car.getConstantYPos() - vp.getViewportY());
            renderSprite(g, car);
        }
        
        //Render the zombies
        for (ZombieCar car : zombies){
            //Adjust for the viewport
            car.setX(VIEWPORT_X + car.getConstantXPos() - vp.getViewportX());
            car.setY(VIEWPORT_Y + car.getConstantYPos() - vp.getViewportY());
            renderSprite(g, car);
        }
        
        //Render the player's car
        playerCar.setX(VIEWPORT_X + playerCar.getConstantXPos() - vp.getViewportX());
        playerCar.setY(VIEWPORT_Y + playerCar.getConstantYPos() - vp.getViewportY());
        renderSprite(g, playerCar);
        
    }

    private void renderLevelName(Graphics g) {
        String levelName = dataModel.getCurrentLevel().getLevelName();
        g.setColor(Color.BLACK);
        g.setFont(FONT_TEXT_DISPLAY);
        g.drawString(levelName, PathXConstants.GAME_CITY_LABEL_X, PathXConstants.GAME_CITY_LABEL_Y);
    }

    private void renderLevelBackground(Graphics g) {
        PathXLevel level = dataModel.getCurrentLevel();
        String levelBG = level.getBgImage();
        Viewport vp = dataModel.getGameViewport();
        int vpx = vp.getViewportX();
        int vpy = vp.getViewportY();
        BufferedImage bgImage = game.loadImage(PATH_DATA + "levels/" + levelBG);
        g.drawImage(bgImage, GAME_VIEWPORT_X, GAME_VIEWPORT_Y, GAME_VIEWPORT_X + GAME_VIEWPORT_WIDTH, 
                GAME_VIEWPORT_Y + GAME_VIEWPORT_HEIGHT, vpx, vpy, vpx + GAME_VIEWPORT_WIDTH , 
                vpy + GAME_VIEWPORT_HEIGHT, this);
        
    }

    private void renderStartAndEnd(Graphics g) {
        PathXDataModel data = (PathXDataModel) game.getDataModel();
        Viewport vp = data.getGameViewport();
        PathXNode start = data.getNodes().get(0);
        PathXNode finish = data.getNodes().get(1);
        String imgPath = props.getProperty(PathXPropertyType.PATH_IMG);
        
        BufferedImage img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_BANK));
        g.drawImage(img, (int) start.getX() - 15, (int) start.getY() - 15, this);
        
        img = game.loadImage(imgPath + props.getProperty(PathXPropertyType.IMAGE_HOUSE));
        g.drawImage(img, (int) finish.getX() -15, (int) finish.getY() - 15, this);
    }

    private void renderDialog(MiniGameState gameState, Graphics g) {
        g.setFont(FONT_TEXT_DISPLAY);
        g.setColor(Color.BLACK);
        if (gameState.equals(MiniGameState.WIN)){
            String dialog = "YOU WIN! \n Reward: $" + dataModel.getCurrentLevel().getReward();
            g.drawString(dialog, PathXConstants.OVERLAY_LEVEL_DIALOG_X, PathXConstants.OVERLAY_LEVEL_DIALOG_Y);
        }else if (gameState.equals(MiniGameState.LOSS)){
            String dialog = "YOU LOSE! \n Your balance is now $" + dataModel.getRecord().balance;
            g.drawString(dialog, PathXConstants.OVERLAY_LEVEL_DIALOG_X, PathXConstants.OVERLAY_LEVEL_DIALOG_Y);
        }
    }
}
