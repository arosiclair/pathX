/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.TreeMap;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import static pathx.PathXConstants.COMPLETE_LEVEL_TYPE;
import static pathx.PathXConstants.INCOMPLETE_LEVEL_TYPE;
import static pathx.PathXConstants.VIEWPORT_X;
import static pathx.PathXConstants.VIEWPORT_Y;
import pathx.data.PathXLevel;
import static pathx.ui.PathXSpriteState.MOUSE_OVER;
import static pathx.ui.PathXSpriteState.VISIBLE;
/**
 *
 * @author Andrew
 */
public class PathXLevelSprite extends Sprite{
    
    private String name;
    private PathXLevel level;
    private PathXMiniGame game;
    private PathXEventHandler eventHandler;
    
    public PathXLevelSprite (SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXLevel level, PathXMiniGame game){
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        this.level = level;
        eventHandler = game.getEventHandler();
        this.game = game;
        name = level.getLevelName();
        
        setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.switchToGameScreen();
            }
        });
    }

    public String getName() {
        return name;
    }

    public PathXLevel getLevel() {
        return level;
    }
    
    public void update(){
        //TreeMap<String, Sprite> buttons = game.getGUIButtons();
        Viewport vp = game.getDataModel().getViewport();
        
        setX(VIEWPORT_X + getX() - vp.getViewportX());
        setY(VIEWPORT_Y + getY() - vp.getViewportY());

        //buttons.put(level.getLevelName(), s);
    }
}
