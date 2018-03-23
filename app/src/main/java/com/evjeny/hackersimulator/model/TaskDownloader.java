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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by evjeny on 11.03.2018 13:04.
 */

public class TaskDownloader {

    private RequestQueue queue;

    private File mainDir;
    private File mainFile;
    private File hashFile;

    private final String hashTag = "hash";
    private final String hashUrl = "http://silvertests.ru/XML/GetQuestionsHashPython.ashx";
    private final String tasksUrl = "http://silvertests.ru/XML/GetQuestionsPython.ashx";

    public TaskDownloader(Context context) {

        mainDir = new File(context.getFilesDir(), "tasks");
        if (!mainDir.exists()) mainDir.mkdir();

        mainFile = new File(mainDir, "main.xml");

        hashFile = new File(mainDir, "hash.xml");

        queue = QueueSingleton.getInstance(context.getApplicationContext()).getRequestQueue();

        StringRequest hashRequest = new StringRequest(Request.Method.GET, hashUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            if (need2dl(response)) {
                                writeFile(hashFile, response); // saving hash
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

    private boolean need2dl(String hashXml) throws XmlPullParserException, IOException {
        boolean hashFileExists = hashFile.exists();
        boolean mainFileExists = mainFile.exists();
        logd("Hash file exists:", hashFileExists);
        logd("Main file exists:", mainFileExists);
        if (!hashFileExists || !mainFileExists) return true;

        String hashFileContent = readFile(hashFile);
        logd("Hash file content:", hashFileContent);
        logd("Internet content:", hashXml);
        boolean hashesAreEqual = hashXml.equals(hashFileContent);
        logd("Hashes are equal:", hashesAreEqual);
        return !hashesAreEqual;

    }

    private void downloadFile() {

        StringRequest tasksRequest = new StringRequest(Request.Method.GET, tasksUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            logd("Got file:", response);
                            writeTask(response.trim());
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

    private String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line).append('\n');
        }
        reader.close();
        return builder.toString();
    }

    private void writeFile(File file, String content) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(content.getBytes());
        fos.close();
    }

    private void writeTask(String content) throws IOException, XmlPullParserException {
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
        parser = null;
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
