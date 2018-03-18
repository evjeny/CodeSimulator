package com.evjeny.hackersimulator.game;

import java.io.Serializable;

/**
 * Created by Evjeny on 26.01.2018 11:10.
 */

public class GameSave implements Serializable {

    private final String worldName;
    private final GameType gameType;
    private String playerName;
    private int currentScene;

    public GameSave(String worldName, GameType gameType) {
        this.worldName = worldName;
        this.gameType = gameType;
    }

    public GameSave(String worldName, String playerName, GameType gameType, int currentScene) {
        this.worldName = worldName;
        this.playerName = playerName;
        this.gameType = gameType;
        this.currentScene = currentScene;

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

    public int getCurrentScene() {
        return currentScene;
    }

    public void setCurrentScene(int currentScene) {
        this.currentScene = currentScene;
    }
}
