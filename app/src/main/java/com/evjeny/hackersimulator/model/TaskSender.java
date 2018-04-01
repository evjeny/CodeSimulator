package com.evjeny.hackersimulator.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by evjeny on 13.03.2018 19:17.
 */

@SuppressWarnings("FieldCanBeLocal")
public class TaskSender {

    private RequestQueue queue;

    private final String SEND_CODE_ADDRESS = "http://silvertests.ru/XML/PythonTask.asmx/SendSource";
    private final String GET_RESULT_ADDRESS = "http://silvertests.ru/XML/PythonTask.asmx/GetRezult";

    public TaskSender(Context context) {
        queue = QueueSingleton.getInstance(context.getApplicationContext()).getRequestQueue();
    }

    /**
     * Посылает на сервер исходный код и id задачи, принимает номер проверки на сервере
     * Вызывает метод repeatableCheck, чтобы получить результат
     *
     * @param source - исходный код для задачи
     * @param id     - id задачи
     * @param ri     - интерфейс для принятия значений
     * @throws JSONException {@link #repeatableCheck(int, ResultInterface)}
     *                       {@link #getRes(int, CheckResultInterface)}
     */
    public void sendRequest(String source, long id, final ResultInterface ri) {
        JSONObject params = new JSONObject();

        try {
            params.put("Source", source);
            params.put("IDZadanie_Vopros", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, SEND_CODE_ADDRESS, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int idToWait = response.getInt("d");
                            logd("Received wait id:", idToWait);
                            repeatableCheck(idToWait, ri);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        JSONObject res = new JSONObject();
                        try {
                            res.put("resultCode", -1);
                            res.put("result", "");
                            res.put("input", "");
                            res.put("log_test", -1);
                            res.put("isRunning", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ri.result(res);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };
        queue.add(postRequest);
    }

    /**
     * Вызывает проверку кода на сервере через промежуток в 2 секунды, если приходит результат,
     * возвращает значение в ResultInterface
     *
     * @param waitId
     * @param ri
     * @see ResultInterface
     */
    private void repeatableCheck(final int waitId, final ResultInterface ri) {
        final Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                getRes(waitId, new CheckResultInterface() {
                    @Override
                    public void result(JSONObject checkResult) {
                        try {
                            checkResult = checkResult.getJSONObject("d");
                            boolean isRunning = checkResult.getBoolean("isRunning");
                            if (!isRunning) {
                                ri.result(checkResult);
                                timer.cancel();
                                timer.purge();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void error() {
                        JSONObject res = new JSONObject();
                        try {
                            res.put("resultCode", -1);
                            res.put("result", "");
                            res.put("input", "");
                            res.put("log_test", -1);
                            res.put("isRunning", false);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ri.result(res);
                        timer.cancel();
                        timer.purge();
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(task, 0, 2000);
    }

    /**
     * Выполняет посылку кода на сервер и возвращает в интерфейс CheckResultInterface
     * значение с сервера:
     * Принято
     * Выполнено 75% тестов
     * Превышено время ожидания ответа.
     * Ошибка компиляции
     * Превышен лимит памяти
     * Runtime error
     *
     * @param waitId
     * @param cr
     * @throws JSONException
     * @see CheckResultInterface
     */
    private void getRes(int waitId, final CheckResultInterface cr) {
        JSONObject params = new JSONObject();
        try {
            params.put("IDLog", waitId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, GET_RESULT_ADDRESS, params,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        cr.result(response);
                        logd("Received req after wait request:", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        logd(error.toString());
                        cr.error();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }

        };
        queue.add(postRequest);
    }

    private void logd(Object... args) {
        StringBuilder builder = new StringBuilder();
        for (Object arg : args) {
            builder.append(arg);
            builder.append(" ");
        }
        Log.d("TaskSender", builder.toString());
    }

    public interface ResultInterface {
        void result(JSONObject res);
    }

    private interface CheckResultInterface {
        void result(JSONObject checkResult);

        void error();
    }
}
