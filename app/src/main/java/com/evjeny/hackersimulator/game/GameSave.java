package com.evjeny.hackersimulator.game;

import java.io.Serializable;

/**
 * Created by Evjeny on 26.01.2018 11:10.
 */

public class GameSave implements Serializable {

    private final String worldName;
    private final GameType gameType;
    private String playerName;
    private String currentAct;

    public GameSave(String worldName, GameType gameType) {
        this.worldName = worldName;
        this.gameType = gameType;
    }

    public GameSave(String worldName, String playerName, GameType gameType, String currentAct) {
        this.worldName = worldName;
        this.playerName = playerName;
        this.gameType = gameType;
        this.currentAct = currentAct;

    }

    public String getWorldName() {
        return worldName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public GameType getGameType() {
        return gameType;
    }

    public String getCurrentAct() {
        return currentAct;
    }

    public void setCurrentAct(String currentAct) {
        this.currentAct = currentAct;
    }
}
