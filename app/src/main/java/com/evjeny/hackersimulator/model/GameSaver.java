package com.evjeny.hackersimulator.model;

import android.content.Context;
import android.util.Log;

import com.evjeny.hackersimulator.game.GameSave;
import com.evjeny.hackersimulator.game.GameType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by Evjeny on 26.01.2018 10:59.
 */

public class GameSaver {

    private final File savesDir;

    private static final String path = "saves";
    private static final String hacker_ext = ".hack";
    private static final String proger_ext = ".prog";

    public GameSaver(Context context) {
        savesDir = context.getDir(path, Context.MODE_PRIVATE);
    }

    public ArrayList<GameSave> getSavesWithoutData() {
        String[] filenames = savesDir.list();
        ArrayList<GameSave> res = new ArrayList<>();
        for (String name : filenames) {
            Log.d("DEBUGGER", "file in saves : " + name);
            if (name.endsWith(GameSaver.hacker_ext)) {
                String worldName = name.substring(0, name.length() - GameSaver.hacker_ext.length());
                res.add(new GameSave(worldName, GameType.hacker));
            } else if (name.endsWith(GameSaver.proger_ext)) {
                String worldName = name.substring(0, name.length() - GameSaver.proger_ext.length());
                res.add(new GameSave(worldName, GameType.proger));
            }
        }
        return res;
    }

    public GameSave getLevel(GameSave shortSave) throws IOException {
        return getLevel(shortSave.getWorldName(), shortSave.getGameType());
    }

    private GameSave getLevel(String worldName, GameType gameType) throws IOException {
        File saveFile = new File(savesDir, worldName + getExtension(gameType));
        FileInputStream fis = new FileInputStream(saveFile);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        worldName = reader.readLine();
        String playerName = reader.readLine();
        String currentAct = reader.readLine();
        reader.close();
        return new GameSave(worldName, playerName, gameType, currentAct);
    }

    public void writeLevel(GameSave save) throws IOException {
        writeLevel(save.getWorldName(), save.getPlayerName(), save.getGameType(),
                save.getCurrentAct());
    }

    public GameSave writeLevel(String worldName, String playerName,
                               GameType gameType, String currentAct) throws IOException {
        File saveFile = new File(savesDir, worldName + getExtension(gameType));
        FileOutputStream fos = new FileOutputStream(saveFile);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fos));
        writer.write(worldName);
        writer.newLine();
        writer.write(playerName);
        writer.newLine();
        writer.write(currentAct);
        writer.close();
        return new GameSave(worldName, playerName, gameType, currentAct);
    }

    public void removeLevel(GameSave gameSave) {
        removeLevel(gameSave.getWorldName(), gameSave.getGameType());
    }

    public void removeLevel(String worldName, GameType gameType) {
        File saveFile = new File(savesDir, worldName + getExtension(gameType));
        saveFile.delete();
    }

    private String getExtension(GameType type) {
        if (type == GameType.hacker) return hacker_ext;
        return proger_ext;
    }
}
