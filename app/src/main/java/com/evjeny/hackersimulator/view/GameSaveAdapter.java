package com.evjeny.hackersimulator.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.evjeny.hackersimulator.R;
import com.evjeny.hackersimulator.game.GameSave;

import java.util.ArrayList;

/**
 * Created by Evjeny on 26.01.2018 11:54.
 */

public class GameSaveAdapter extends BaseAdapter {

    private LayoutInflater inflater;

    private ArrayList<GameSave> objects;

    GameSaveAdapter(Context context, ArrayList<GameSave> saves) {
        this.objects = saves;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int i) {
        return objects.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    GameSave getGameSave(int i) {
        return objects.get(i);
    }

    void remove(int index) {
        objects.remove(index);
        this.notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.level_item, viewGroup, false);
        }
        GameSave save = getGameSave(i);

        TextView worldName = view.findViewById(R.id.level_item_world_name);
        TextView gameType = view.findViewById(R.id.level_item_game_type);


        worldName.setText(save.getWorldName());
        gameType.setText(save.getGameType().text);

        return view;
    }
}
