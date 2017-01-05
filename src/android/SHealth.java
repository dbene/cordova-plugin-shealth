package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.util.Log;

public class SHealth extends CordovaPlugin {

    String APP_TAG = "TAG_StepCount";

	Activity activity = null;
    SHealthConnector connector = null;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if( activity == null) {
            Log.e(APP_TAG, "activity == null");
            activity = this.cordova.getActivity();
        }

        if( connector == null) {
            Log.e(APP_TAG, "connector == null");
            connector = new connector(activity, callbackContext);
        }

        if (action.equals("greet")) {
            Log.e(APP_TAG, "greet");

            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;

        } else if (action.equals("connect")) {
            Log.e(APP_TAG, "connect");

            connector.connect();

            return true;

        } else if (action.equals("getSteps")) {
            Log.e(APP_TAG, "getSteps");

            connector.create();

            return true;

        } else {
            
            return false;

        }
    }
}
