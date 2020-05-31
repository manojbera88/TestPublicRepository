package com.manoj.trivawithanimation.controller;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

//*******Singleton Class AppController
//super class Application. At the time of creating mention superclass
public class AppController extends Application {

        public static final String TAG =AppController.class.getSimpleName();
        private static AppController mInstance;
        private RequestQueue mRequestQueue;
//        private ImageLoader imageLoader;
//        private static Context ctx;

//        private AppController(Context context) {
//            ctx = context;
//            requestQueue = getRequestQueue();
//
//            imageLoader = new ImageLoader(requestQueue,
//                    new ImageLoader.ImageCache() {
//                        private final LruCache<String, Bitmap>
//                                cache = new LruCache<String, Bitmap>(20);
//
//                        @Override
//                        public Bitmap getBitmap(String url) {
//                            return cache.get(url);
//                        }
//
//                        @Override
//                        public void putBitmap(String url, Bitmap bitmap) {
//                            cache.put(url, bitmap);
//                        }
//                    });
//        }
        //constructor
        public static synchronized AppController getInstance() {
            //this condition not needed in this app
//            if (instance == null) {
//                instance = new AppController(context);
//            }
            return mInstance;
        }
//??
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public RequestQueue getRequestQueue() {
            if (mRequestQueue == null) {
                // getApplicationContext() is key, it keeps you from leaking the
                // Activity or BroadcastReceiver if someone passes one in.
                mRequestQueue = Volley.newRequestQueue(getApplicationContext());
            }
            return mRequestQueue;
        }

        public <T> void addToRequestQueue(Request<T> req, String tag) {
            // set the default tag if tag is empty
            req.setTag(TextUtils.isEmpty(tag)?TAG:tag);
            getRequestQueue().add(req);
        }

        public <T> void addToRequestQueue(Request<T> req) {
       req.setTag(TAG);
        getRequestQueue().add(req);
        }

        public void cancelPendingRequests(Object tag){
            if (mRequestQueue!=null){
                mRequestQueue.cancelAll(tag);
            }


        }
//        public ImageLoader getImageLoader() {
//            return imageLoader;
//        }
    }




