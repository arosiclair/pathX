/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pathx.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mini_game.MiniGame;
import mini_game.Sprite;
import mini_game.SpriteType;
import mini_game.Viewport;
import static pathx.PathXConstants.VIEWPORT_X;
import static pathx.PathXConstants.VIEWPORT_Y;
import pathx.data.PathXLevel;
/**
 *
 * @author Andrew
 */
public class PathXLevelSprite extends Sprite{
    
    private String name;
    private PathXLevel level;
    private PathXMiniGame game;
    private PathXEventHandler eventHandler;
    
    private float constantX, constantY;
    
    public PathXLevelSprite (SpriteType initSpriteType, float initX, float initY, float initVx, 
            float initVy, String initState, PathXLevel level, PathXMiniGame game){
        super(initSpriteType, initX, initY, initVx, initVy, initState);
        
        this.level = level;
        eventHandler = game.getEventHandler();
        this.game = game;
        name = level.getLevelName();
        constantX = initX;
        constantY = initY;
        
        setActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                eventHandler.switchToGameScreen(getLevel());
            }
        });
    }

    public String getName() {
        return name;
    }

    public PathXLevel getLevel() {
        return level;
    }
    
    @Override
    public boolean testForClick(MiniGame game, int x, int y){
        if (state.indexOf("COMPLETED") >= 0)
            return super.testForClick(game, x, y);
        else if (level.getPrevious() == null || level.getPrevious().isCompleted())
            return super.testForClick(game, x, y);
        else
            return false;
    }
    
//    @Override
//    public String getState(){
//        if (level.getPrevious() != null && !level.getPrevious().isCompleted())
//            return PathXSpriteState.LOCKED.toString();
//        else if (!level.isCompleted())
//            return PathXSpriteState.INCOMPLETE.toString();
//        else
//            return PathXSpriteState.COMPLETED.toString();
//    }
//    
    public void update(){
        Viewport vp = game.getDataModel().getViewport();
        setX(VIEWPORT_X + constantX - vp.getViewportX());
        setY(VIEWPORT_Y + constantY - vp.getViewportY());
        
//        if (level.getPrevious() != null && !level.getPrevious().isCompleted())
//             setState(PathXSpriteState.LOCKED.toString());
//        else if (!level.isCompleted())
//            setState(PathXSpriteState.INCOMPLETE.toString());
//        else
//            setState(PathXSpriteState.COMPLETED.toString());
    }

    public void updateState() {
        if (level.getPrevious() != null && !level.getPrevious().isCompleted())
             setState(PathXSpriteState.LOCKED.toString());
        else if (!level.isCompleted())
            setState(PathXSpriteState.INCOMPLETE.toString());
        else
            setState(PathXSpriteState.COMPLETED.toString());
    }
}
