package com.evjeny.hackersimulator.model;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.evjeny.hackersimulator.game.Task;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by evjeny on 11.03.2018 13:04.
 */

public class TaskDownloader {

    private RequestQueue queue;

    private File mainDir;
    private File mainFile;

    private final String hashTag = "hash", tasksTag = "tasks", taskTag = "task", textTag = "text";
    private final String hashUrl = "http://silvertests.ru/XML/GetQuestionsHashPython.ashx";
    private final String tasksUrl = "http://silvertests.ru/XML/GetQuestionsPython.ashx";

    public TaskDownloader(Context context) {

        mainDir = new File(context.getFilesDir(), "tasks");
        if (!mainDir.exists()) mainDir.mkdir();

        mainFile = new File(mainDir, "main.xml");

        queue = QueueSingleton.getInstance(context.getApplicationContext()).getRequestQueue();

        StringRequest hashRequest = new StringRequest(Request.Method.GET, hashUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        response = response.trim();
                        try {
                            if (need2dl(response)) {
                                downloadFile();
                            }
                        } catch (XmlPullParserException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(hashRequest);
    }

    private String hashFromReader(Reader reader) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(reader);
        int eventType = parser.getEventType();

        String hash = null;

        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG && parser.getName().equals(hashTag)) {
                hash = parser.nextText();
            }
            eventType = parser.next();
        }
        return hash;
    }

    private boolean need2dl(String hashXml) throws XmlPullParserException, IOException {
        String hash = hashFromReader(new StringReader(hashXml));

        if (hash == null) return true;

        if (!mainFile.exists()) return true;
        boolean hashesAreEq = areHashesEqual(hash);

        return !hashesAreEq;
    }

    private boolean areHashesEqual(String hash) throws XmlPullParserException, IOException {
        File hashFile = new File(mainDir, "hash.xml");

        if (!hashFile.exists()) return false;

        String fileHash = hashFromReader(new FileReader(hashFile));

        return fileHash != null && fileHash.equals(hash);

    }

    private void downloadFile() {

        StringRequest tasksRequest = new StringRequest(Request.Method.GET, tasksUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            logd("Got file:", response);
                            writeFile(response);
                            logd("File has been written: " + mainFile.exists());
                        } catch (IOException | XmlPullParserException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                });
        queue.add(tasksRequest);
    }

    private void writeFile(String content) throws IOException, XmlPullParserException {
        FileOutputStream fos = new FileOutputStream(mainFile);
        fos.write(content.getBytes());
        fos.close();

        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = factory.newPullParser();
        parser.setInput(new StringReader(content));

        int eventType = parser.getEventType();

        ArrayList<Task> tasks = new ArrayList<>();

        String name = null, text = null;
        long id = 0;

        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                switch (parser.getName()) {
                    case "task":
                        name = parser.getAttributeValue(null, "name")
                                .replace("![CDATA[", "")
                                .replace("]]", "");
                        id = Long.parseLong(parser.getAttributeValue(null, "id"));
                        break;
                    case "text":
                        text = parser.nextText();
                        break;
                }
            } else if (parser.getEventType() == XmlPullParser.END_TAG) {
                switch (parser.getName()) {
                    case "task":
                        tasks.add(new Task(name, id, text));
                        break;
                }
            }
            eventType = parser.next();
        }
        for (Task task : tasks) {
            File currentFile = new File(mainDir, task.name);
            FileOutputStream tfos = new FileOutputStream(currentFile);
            tfos.write(task.toString().getBytes());
            tfos.close();
            logd("Wrote file:", task.name, ", content: ", task);
        }
    }

    private void logd(Object... args) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            builder.append(arg);
            builder.append(" ");
        }
        Log.d("FileDownloader", builder.toString());
    }
}
