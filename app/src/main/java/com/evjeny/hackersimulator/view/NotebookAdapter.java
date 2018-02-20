package com.evjeny.hackersimulator.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.evjeny.hackersimulator.game.NoteItem;

import java.util.List;

/**
 * Created by Evjeny on 18.02.2018 17:05.
 */

public class NotebookAdapter extends ArrayAdapter<NoteItem> {

    public NotebookAdapter(@NonNull Context context, @NonNull List<NoteItem> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final NoteItem item = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,
                    null);
        TextView tv = convertView.findViewById(android.R.id.text1);
        tv.setText(item.label);
        return convertView;
    }
}
