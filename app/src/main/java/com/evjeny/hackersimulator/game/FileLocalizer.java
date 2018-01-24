package com.evjeny.hackersimulator.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.XmlResourceParser;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Evjeny on 20.01.2018 16:44.
 */

public class FileLocalizer {

    private Context context;
    private AssetManager manager;

    private String REGEX_RESOURCE_STRING = "@string/([A-Za-z0-9-_]*)";

    public FileLocalizer(Context context) {
        this.context = context;
        this.manager = context.getAssets();
    }

    public Scene get(int num) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        InputStream raw = manager.open("scenes/" + num + ".xml");
        parser.setInput(raw, null);
        //XmlResourceParser parser = manager.openXmlResourceParser("file:///android_asset/scenes/" + num + ".xml");
        int eventType = parser.getEventType();

        Scene result;
        SceneType type = null;
        Act act = null;
        Content content = null;
        ArrayList<Object> contents = null;
        Bar bar = null;
        ArrayList<Button> buttons = null;
        Button button = null;
        String btype = "text";
        Object bname = null;
        String action = null;


        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                String name = parser.getName();
                if (name.equals("scene")) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if (parser.getAttributeName(i).equals("type")) {
                            switch (parser.getAttributeValue(i)) {
                                case "text_scene":
                                    type = SceneType.STORY;
                                    break;
                                case "image_scene":
                                    type = SceneType.IMAGE;
                                    break;
                                case "code_scene":
                                    type = SceneType.CODE;
                                    break;
                                default:
                                    type = SceneType.STORY;
                                    break;
                            }
                        }
                    }
                } else if (name.equals("act")) {
                } else if (name.equals("content")) {
                    contents = new ArrayList<>();
                } else if (name.equals("ci")) {
                    switch (type) {
                        case STORY:
                            contents.add(getValFromText(parser.nextText()));
                            break;
                        case IMAGE:
                            contents.add(getImageFromText(parser.nextText()));
                            break;
                        case CODE:
                            String codeType = parser.getAttributeValue(null, "type");
                            CodePart codePart = null;
                            if (codeType.equals("code")) {
                                codePart = new CodePart(CodePart.Type.WRITABLE,
                                        replaceResourceStrings(parser.nextText()));
                            } else if (codeType.equals("text")) {
                                codePart = new CodePart(CodePart.Type.TEXT,
                                        replaceResourceStrings(parser.nextText()));
                            }
                            contents.add(codePart);
                            break;
                        default:
                            contents.add(getValFromText(parser.nextText()));
                            break;
                    }
                } else if (name.equals("bar")) {
                    buttons = new ArrayList<>();
                } else if (name.equals("button")) {
                    for (int i = 0; i < parser.getAttributeCount(); i++) {
                        if (parser.getAttributeName(i).equals("type")) {
                            btype = parser.getAttributeValue(i);
                        }
                    }
                } else if (name.equals("name")) {
                    bname = parser.nextText();
                } else if (name.equals("action")) {
                    action = parser.nextText();
                }
            } else if (parser.getEventType() == XmlPullParser.END_TAG) {
                String name = parser.getName();
                ArrayList<String> messages = new ArrayList<>();
                if (name.equals("content")) {
                    switch (type) {
                        case STORY:
                            for (Object con : contents) messages.add(con.toString());
                            content = new StoryContent(messages);
                            break;
                        case IMAGE:
                            ArrayList<Bitmap> images = new ArrayList<>();
                            for (Object con : contents) images.add((Bitmap) con);
                            content = new ImageContent(images);
                            break;
                        case CODE:
                            ArrayList<CodePart> parts = new ArrayList<>();
                            for (Object con : contents) parts.add((CodePart) con);
                            content = new CodeContent(parts);
                            break;
                        default:
                            for (Object con : contents) messages.add(con.toString());
                            content = new StoryContent(messages);
                            break;
                    }
                } else if (name.equals("button")) {
                    switch (btype) {
                        case "text":
                            button = new TextButton(getValFromText((String) bname), action);
                            break;
                        case "image":
                            break;
                        default:
                            button = new TextButton((String) bname, action);
                            break;
                    }
                    buttons.add(button);
                } else if (name.equals("bar")) {
                    bar = new Bar(buttons);
                } else if (name.equals("act")) {
                    act = new Act(content, bar);
                }
            }
            eventType = parser.next();
        }
        result = new Scene(type, act);
        return result;
    }

    private String getValFromText(String text) {

        if(text.matches(REGEX_RESOURCE_STRING)) {
            return getStringByName(this.context, text);
        } else {
            return text;
        }
    }

    private String replaceResourceStrings(String source) {
        Pattern p = Pattern.compile(REGEX_RESOURCE_STRING);
        Matcher m = p.matcher(source);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String stringFromResources = getStringByName(context, m.group(1));
            if (stringFromResources == null) {
                stringFromResources = m.group(1);
            }
            m.appendReplacement(sb,
                    replaceResourceStrings(stringFromResources));
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

    private Bitmap getImageFromText(String file) throws IOException {
        InputStream stream = manager.open("images/"+file);
        return BitmapFactory.decodeStream(stream);
    }

    private static int getResourceId(Context context, String defType,
                                     String name) {
        return context.getResources().getIdentifier(name, defType,
                context.getPackageName());
    }
}
