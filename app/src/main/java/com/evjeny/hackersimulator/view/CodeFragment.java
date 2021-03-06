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
 * Created by Evjeny on 21.01.2018 21:02.
 */

public class CodeFragment extends Fragment {

    public CodeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), getStyle());
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        return localInflater.inflate(R.layout.code_fragment, container, false);
    }

    private int getStyle() {
        return getArguments().getInt("style");
    }
}
