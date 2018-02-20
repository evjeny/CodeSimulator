package com.evjeny.hackersimulator.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.evjeny.hackersimulator.R;

/**
 * Created by Evjeny on 18.01.2018 6:04.
 */

public class ConsoleFragment extends Fragment {

    public View rootView;

    public ConsoleFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), getStyle());
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        rootView = localInflater.inflate(R.layout.console_fragment, container, false);
        return rootView;
    }

    private int getStyle() {
        return getArguments().getInt("style");
    }
}
