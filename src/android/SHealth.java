package com.wopo.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import android.app.Activity;
import android.util.Log;

public class SHealth extends CordovaPlugin {

    String APP_TAG = "CordovaSHealthPlugin";

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

            String name = data.getString(0);

            Log.e(APP_TAG, "Hello, " + name + " this is a cordova shealth plugin!");
            String message = "Hello, " + name + " this is a cordova shealth plugin!";

            callbackContext.success(message);
            return true;

        } else if (action.equals("callHealthPermissionManager")) {
            Log.e(APP_TAG, "callHealthPermissionManager");

            stepCount.callHealthPermissionManager();

            return true;

        } else if (action.equals("getData")) {
            Log.e(APP_TAG, "getData");

            String string = data.getString(0);
            String[] parts = string.split(";");

            long startTime = Long.parseLong(parts[0]);
            long endTime = Long.parseLong(parts[1]);

            Log.e(APP_TAG, "StartTime: " + startTime + " - EndTime: " + endTime);

            stepCount.getData(startTime, endTime);

            return true;

        } else {
            
            return false;

        }
    }
}
