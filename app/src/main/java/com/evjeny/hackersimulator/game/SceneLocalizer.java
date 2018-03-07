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
 * Created by evjeny on 20.01.2018 16:44 6:44.
 */

class SceneLocalizer {

    private Context context;
    private AssetManager manager;

    private String type_prefix;
    private String REGEX_RESOURCE_STRING = "@string/([A-Za-z0-9-_]*)";

    private final static String sceneTag = "scene",
    actTag = "act", contentTag = "content", contentItemTag = "ci",
    barTag = "bar", buttonTag = "button", nameTag = "name", actionTag = "action",
    typeTag = "type";
    private final static String textType = "text", imageType = "image", codeType = "code";

    SceneLocalizer(Context context, GameType type) {
        this.context = context;
        this.manager = context.getAssets();
        this.type_prefix = type.text;
    }

    Scene get(int num) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        InputStream raw = manager.open(type_prefix + "/scenes/" + num + ".xml");
        parser.setInput(raw, null);
        int eventType = parser.getEventType();

        Scene result = null;

        ArrayList<Act> acts = new ArrayList<>();
        ActType actType = null;

        ArrayList<Object> contents = null;
        Content content = null;

        Bar bar = null;
        ArrayList<Button> buttons = null;
        Button button = null;

        String buttonType = "text";
        Object buttonName = null;
        String action = null;

        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                String name = parser.getName();
                switch (name) {
                    case sceneTag:
                        break;
                    case actTag:
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equals(typeTag)) {
                                switch (parser.getAttributeValue(i)) {
                                    case textType:
                                        actType = ActType.STORY;
                                        break;
                                    case imageType:
                                        actType = ActType.IMAGE;
                                        break;
                                    case codeType:
                                        actType = ActType.CODE;
                                        break;
                                    default:
                                        actType = ActType.STORY;
                                        break;
                                }
                            }
                        }
                        break;
                    case contentTag:
                        contents = new ArrayList<>();
                        break;
                    case contentItemTag:
                        switch (actType) {
                            case STORY:
                                contents.add(getValFromText(parser.nextText()));
                                break;
                            case IMAGE:
                                contents.add(getImageFromText(parser.nextText()));
                                break;
                            case CODE:
                                String codeType = parser.getAttributeValue(null, typeTag);
                                CodePart codePart = null;
                                if (codeType.equals(codeType)) { // FIXME: 07.03.2018
                                    codePart = new CodePart(CodePart.Type.WRITABLE,
                                            replaceResourceStrings(parser.nextText()));
                                } else if (codeType.equals(textType)) {
                                    codePart = new CodePart(CodePart.Type.TEXT,
                                            replaceResourceStrings(parser.nextText()));
                                }
                                contents.add(codePart);
                                break;
                            default:
                                contents.add(getValFromText(parser.nextText()));
                                break;
                        }
                        break;
                    case barTag:
                        buttons = new ArrayList<>();
                        break;
                    case buttonTag:
                        for (int i = 0; i < parser.getAttributeCount(); i++) {
                            if (parser.getAttributeName(i).equals(typeTag)) {
                                buttonType = parser.getAttributeValue(i);
                            }
                        }
                        break;
                    case nameTag:
                        buttonName = parser.nextText();
                        break;
                    case actionTag:
                        action = parser.nextText();
                        break;
                }
            } else if (parser.getEventType() == XmlPullParser.END_TAG) {
                String name = parser.getName();
                ArrayList<String> messages = new ArrayList<>();
                switch (name) {
                    case contentTag:
                        switch (actType) {
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
                        break;
                    case buttonTag:
                        switch (buttonType) {
                            case textType:
                                button = new TextButton(getValFromText((String) buttonName), action);
                                break;
                            case imageType:
                                break;
                            default:
                                button = new TextButton((String) buttonName, action);
                                break;
                        }
                        buttons.add(button);
                        break;
                    case barTag:
                        bar = new Bar(buttons);
                        break;
                    case actTag:
                        acts.add(new Act(actType, content, bar));
                        break;
                    case sceneTag:
                        result = new Scene(acts);
                        break;
                }
            }
            eventType = parser.next();
        }
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
        InputStream stream = manager.open(type_prefix + "/images/" +file);
        return BitmapFactory.decodeStream(stream);
    }

    private static int getResourceId(Context context, String defType,
                                     String name) {
        return context.getResources().getIdentifier(name, defType,
                context.getPackageName());
    }
}
