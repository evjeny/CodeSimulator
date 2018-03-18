package com.evjeny.hackersimulator.model;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by evjeny on 17.03.2018 11:29.
 */

public class QueueSingleton {

    private static QueueSingleton mInstance;
    private RequestQueue mRequestQueue;
    private static Context context;

    private QueueSingleton(Context context) {
        this.context = context;
        mRequestQueue = getRequestQueue();
    }

    public static synchronized QueueSingleton getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new QueueSingleton(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        getRequestQueue().add(request);
    }
}
