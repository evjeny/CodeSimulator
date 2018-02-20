package com.evjeny.hackersimulator.game;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Evjeny on 18.02.2018 16:47.
 */

public class FileLocalizer {

    private Context context;
    private AssetManager manager;

    public FileLocalizer(Context context) {
        this.context = context;
        this.manager = context.getAssets();
    }

    public String translate(String name) throws IOException {
        InputStream stream = manager.open(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        String content = "";
        String cur;
        while ((cur = reader.readLine()) != null) {
            content += cur + '\n';
        }
        return replaceResourceStrings(context, content);
    }

    public String getString(String name) {
        return getStringByName(context, name);
    }

    private String replaceResourceStrings(Context context, String source) {
        String REGEX_RESOURCE_STRING = "@string/([A-Za-z0-9-_]*)";
        Pattern p = Pattern.compile(REGEX_RESOURCE_STRING);
        Matcher m = p.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String stringFromResources = getStringByName(context, m.group(1));
            if (stringFromResources == null) {
                stringFromResources = m.group(1);
            }
            m.appendReplacement(sb,
                    replaceResourceStrings(context, stringFromResources));
        }
        m.appendTail(sb);
        return sb.toString();
    }
    private String getStringByName(Context context, String name) {
        String DEF_TYPE_STRING = "string";
        int resourceId = getResourceId(context, DEF_TYPE_STRING, name);
        if (resourceId != 0) {
            return context.getString(resourceId);
        } else {
            return null;
        }
    }
    private int getResourceId(Context context, String defType,
                                     String name) {
        return context.getResources().getIdentifier(name, defType,
                context.getPackageName());
    }
}
