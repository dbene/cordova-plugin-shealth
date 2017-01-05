package com.example.plugin;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;

public class Hello extends CordovaPlugin {

	Activity activity;
    StepCount stepCount;

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) throws JSONException {

        activity = this.cordova.getActivity();

        if (action.equals("greet")) {

            String name = data.getString(0);
            String message = "Hello, " + name;
            callbackContext.success(message);

            return true;

        } else if (action.equals("connect")) {
			
            callbackContext.success("connect");

            return true;

        } else if (action.equals("getData")) {

            callbackContext.success("getSteps");

            return true;

        } else {
            
            return false;

        }
    }
}
