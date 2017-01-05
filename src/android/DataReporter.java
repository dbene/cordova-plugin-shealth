/**
 * Copyright (C) 2014 Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Mobile Communication Division,
 * Digital Media & Communications Business, Samsung Electronics Co., Ltd.
 *
 * This software and its documentation are confidential and proprietary
 * information of Samsung Electronics Co., Ltd.  No part of the software and
 * documents may be copied, reproduced, transmitted, translated, or reduced to
 * any electronic medium or machine-readable form without the prior written
 * consent of Samsung Electronics.
 *
 * Samsung Electronics makes no representations with respect to the contents,
 * and assumes no responsibility for any errors that might appear in the
 * software and documents. This publication and the contents hereof are subject
 * to change without notice.
 */

package com.samsung.android.simplehealth;

import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataObserver;
import com.samsung.android.sdk.healthdata.HealthDataResolver;
import com.samsung.android.sdk.healthdata.HealthDataResolver.Filter;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadRequest;
import com.samsung.android.sdk.healthdata.HealthDataResolver.ReadResult;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import android.app.AlertDialog;
import android.database.Cursor;
import android.util.Log;

import org.apache.cordova.*;

import android.app.Activity;

import java.util.Calendar;

public class DataReporter {
    private final HealthDataStore mStore;

    Activity activity;
    CallbackContext callbackContext;

    String APP_TAG = "TAG_StepCount";

    String resultString = "result: ";

    public DataReporter(HealthDataStore store, Activity pActivity, CallbackContext pCallbackContext) {
        mStore = store;
        activity = pActivity;
        this.callbackContext = pCallbackContext;
    }

    public void start() {
        // Register an observer to listen changes of step count and get today step count
        HealthDataObserver.addObserver(mStore, HealthConstants.StepCount.HEALTH_DATA_TYPE, mObserver);
        //HealthDataObserver.addObserver(mStore, HealthConstants.HeartRate.HEALTH_DATA_TYPE, mObserver);
        HealthDataObserver.addObserver(mStore, HealthConstants.CaffeineIntake.HEALTH_DATA_TYPE, mObserver);
        HealthDataObserver.addObserver(mStore, HealthConstants.WaterIntake.HEALTH_DATA_TYPE, mObserver);
        HealthDataObserver.addObserver(mStore, HealthConstants.FoodIntake.HEALTH_DATA_TYPE, mObserver);

        //readTodayStepCount();
        //readTodayCaffeineIntake();

        readToday(HealthConstants.StepCount.START_TIME, HealthConstants.StepCount.HEALTH_DATA_TYPE, HealthConstants.StepCount.COUNT, mListenerStepCount);
        readToday(HealthConstants.CaffeineIntake.START_TIME, HealthConstants.CaffeineIntake.HEALTH_DATA_TYPE, HealthConstants.CaffeineIntake.AMOUNT, mListenerCaffeineIntake);
        readToday(HealthConstants.WaterIntake.START_TIME, HealthConstants.WaterIntake.HEALTH_DATA_TYPE, HealthConstants.WaterIntake.AMOUNT, mListenerWaterIntake);
        readToday(HealthConstants.FoodIntake.START_TIME, HealthConstants.FoodIntake.HEALTH_DATA_TYPE, HealthConstants.FoodIntake.AMOUNT, mListenerFoodIntake);
    }

    private void readToday(String hcStartTime, String hcHDT, String hcString, HealthResultHolder.ResultListener<ReadResult> pmListener) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = System.currentTimeMillis();
        Filter filter = Filter.and(Filter.greaterThanEquals(hcStartTime, startTime),
                Filter.lessThanEquals(hcStartTime, endTime));

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(hcHDT)
                .setProperties(new String[] {hcString})
                .setFilter(filter)
                .build();


        try {
            resolver.read(request).setResultListener(pmListener);
        } catch (Exception e) {
            Log.e(APP_TAG, e.getClass().getName() + " - " + e.getMessage());
            Log.e(APP_TAG, "Getting step count fails.");
        }
    }

    // Read the today's step count on demand
    private void readTodayStepCount() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = System.currentTimeMillis();
        Filter filter = Filter.and(Filter.greaterThanEquals(HealthConstants.StepCount.START_TIME, startTime),
                                   Filter.lessThanEquals(HealthConstants.StepCount.START_TIME, endTime));

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                                                        .setDataType(HealthConstants.StepCount.HEALTH_DATA_TYPE)
                                                        .setProperties(new String[] {HealthConstants.StepCount.COUNT})
                                                        .setFilter(filter)
                                                        .build();


        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {
            Log.e(APP_TAG, e.getClass().getName() + " - " + e.getMessage());
            Log.e(APP_TAG, "Getting step count fails.");
        }
    }

    private void readTodayCaffeineIntake() {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        // Set time range from start time of today to the current time
        long startTime = getStartTimeOfToday();
        long endTime = System.currentTimeMillis();
        Filter filter = Filter.and(Filter.greaterThanEquals(HealthConstants.CaffeineIntake.START_TIME, startTime),
                Filter.lessThanEquals(HealthConstants.CaffeineIntake.START_TIME, endTime));

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(HealthConstants.CaffeineIntake.HEALTH_DATA_TYPE)
                .setProperties(new String[] {HealthConstants.CaffeineIntake.AMOUNT})
                .setFilter(filter)
                .build();


        try {
            resolver.read(request).setResultListener(mListener);
        } catch (Exception e) {
            Log.e(APP_TAG, e.getClass().getName() + " - " + e.getMessage());
            Log.e(APP_TAG, "Getting step count fails.");
        }
    }

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance();

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Letzer Monat
        today.add(Calendar.DAY_OF_MONTH, -1);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListenerStepCount = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            int count = 0;
            Cursor c = null;

            try {
                c = result.getResultCursor();

                if (c != null) {
                    while (c.moveToNext()) {
                        count += c.getInt(c.getColumnIndex(HealthConstants.StepCount.COUNT));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "StepCountCOUNT;" + String.valueOf(count));
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    };

    private final HealthResultHolder.ResultListener<ReadResult> mListenerWaterIntake = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            int count = 0;
            Cursor c = null;

            try {
                c = result.getResultCursor();

                if (c != null) {
                    while (c.moveToNext()) {
                        count += c.getInt(c.getColumnIndex(HealthConstants.WaterIntake.AMOUNT));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "WaterIntakeAMOUNT;" + String.valueOf(count));
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    };

    private final HealthResultHolder.ResultListener<ReadResult> mListenerFoodIntake = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            int count = 0;
            Cursor c = null;

            try {
                c = result.getResultCursor();

                if (c != null) {
                    while (c.moveToNext()) {
                        count += c.getInt(c.getColumnIndex(HealthConstants.FoodIntake.AMOUNT));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "FoodIntakeAMOUNT;" + String.valueOf(count));
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    };


    private final HealthResultHolder.ResultListener<ReadResult> mListenerCaffeineIntake = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            int count = 0;
            Cursor c = null;

            try {
                c = result.getResultCursor();

                if (c != null) {
                    while (c.moveToNext()) {
                        count += c.getInt(c.getColumnIndex(HealthConstants.CaffeineIntake.AMOUNT));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "CaffeineIntakeAMOUNT;" + String.valueOf(count));
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    };


    private final HealthResultHolder.ResultListener<ReadResult> mListener = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            int count = 0;
            Cursor c = null;

            try {
                c = result.getResultCursor();

                if (c != null) {
                    while (c.moveToNext()) {
                        count += c.getInt(c.getColumnIndex(HealthConstants.StepCount.COUNT));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }
            //MainActivity.getInstance().drawStepCount(String.valueOf(count));
            //callbackContext.success(test + " _StepCount: " + String.valueOf(count));

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, "StepCountCOUNT;" + String.valueOf(count));
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    };

    private final HealthDataObserver mObserver = new HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            Log.d(APP_TAG, "Observer receives a data changed event");
            readTodayStepCount();
        }
    };

}
