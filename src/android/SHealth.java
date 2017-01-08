package com.wopo.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.app.Activity;
import android.util.Log;

public class Hello extends CordovaPlugin {

    String APP_TAG = "TAG_CordovaSHealth";

	Activity activity = null;
    SHealthConnector stepCount = null;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        if( activity == null) {
            Log.e(APP_TAG, "activity == null");
            activity = this.cordova.getActivity();
        }

        if( stepCount == null) {
            Log.e(APP_TAG, "stepCount == null");
            stepCount = new SHealthConnector(activity, callbackContext);
        }

        if (action.equals("greet")) {
            Log.e(APP_TAG, "greet");

            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;

        } else if (action.equals("connect")) {
            Log.e(APP_TAG, "connect");

            String string = data.getString(0);

            String[] parts = string.split(";");

            long startTime = Long.parseLong(parts[0]);
            long endTime = Long.parseLong(parts[1]);

            Log.e(APP_TAG, startTime + " - " + endTime);

            stepCount.connect();

            return true;

        } else if (action.equals("getData")) {
            Log.e(APP_TAG, "getData");

            stepCount.create();

            return true;

        } else if (action.equals("footsteps")) {
            Log.e(APP_TAG, "footsteps");

            //stepCount = new StepCount(activity,callbackContext);
            stepCount.connect();
            stepCount.create();

            //callbackContext.success(stepCount.toString());

            return true;

        } else {
            
            return false;

        }
    }
}
