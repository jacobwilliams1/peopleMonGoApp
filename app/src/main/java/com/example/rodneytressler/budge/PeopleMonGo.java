package com.example.rodneytressler.budge;

import android.app.Application;

import com.example.rodneytressler.budge.Stages.MapStage;

import flow.Flow;
import flow.History;

/**
 * Created by rodneytressler on 10/31/16.
 */

public class PeopleMonGo extends Application {
    private static PeopleMonGo application;
    public final Flow mainFlow =
            new Flow(History.single(new MapStage())); //flow works off of stack information which are in history. stage calls xml file which calls java file. .single means only one thing in this History.
    public static final String API_BASE_URL = "https://efa-peoplemon-api.azurewebsites.net:443/";
    @Override
    public void onCreate() {
        super.onCreate();

        application = this;
    }

    public static PeopleMonGo getInstance() {
        return application;
    }

    public static Flow getMainFlow() {
        return getInstance().mainFlow;
    }

}
