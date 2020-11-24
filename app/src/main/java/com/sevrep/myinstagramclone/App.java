package com.sevrep.myinstagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("WTLA1IUAgidM5Hs60P5mK64VjElim7k5tqDrV9PM")
                .clientKey("Zf2HnHyNCA4KFnN9uhkGf929V2rrGfea30yT046X")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}