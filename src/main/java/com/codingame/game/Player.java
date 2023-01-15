package com.codingame.game;
import java.util.HashMap;

import com.codingame.gameengine.core.AbstractMultiplayerPlayer;
import com.codingame.gameengine.module.entities.Circle;

// Uncomment the line below and comment the line under it to create a Solo Game
// public class Player extends AbstractSoloPlayer {
public class Player extends AbstractMultiplayerPlayer {
    public HashMap<Hex, Circle> pieces = new HashMap<>();
    
    @Override
    public int getExpectedOutputLines() {
        return 1;
    }
}
