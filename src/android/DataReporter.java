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

    String APP_TAG = "CordovaSHealthPlugin";

    String resultString = "result: ";

    public DataReporter(HealthDataStore store, Activity pActivity, CallbackContext pCallbackContext) {
        mStore = store;
        activity = pActivity;
        this.callbackContext = pCallbackContext;
    }

    public void start() {
        // Register an observer to listen changes of step count and get today step count
        // HealthDataObserver.addObserver(mStore, HealthConstants.StepCount.HEALTH_DATA_TYPE, mObserver);

        readToday(
                getStartTimeOfToday(),
                System.currentTimeMillis(),
                HealthConstants.StepCount.START_TIME,
                HealthConstants.StepCount.HEALTH_DATA_TYPE, new String[] {
                        HealthConstants.StepCount.COUNT,
                        HealthConstants.StepCount.START_TIME,
                        HealthConstants.StepCount.END_TIME
                },
                mListenerStepCount
        );

        readToday(
                getStartTimeOfToday(),
                System.currentTimeMillis(),
                HealthConstants.WaterIntake.START_TIME,
                HealthConstants.WaterIntake.HEALTH_DATA_TYPE, new String[] {
                        HealthConstants.WaterIntake.AMOUNT,
                        HealthConstants.WaterIntake.START_TIME
                },
                mListenerWaterIntake
        );

        readToday(
                getStartTimeOfToday(),
                System.currentTimeMillis(),
                HealthConstants.CaffeineIntake.START_TIME,
                HealthConstants.CaffeineIntake.HEALTH_DATA_TYPE, new String[] {
                        HealthConstants.CaffeineIntake.AMOUNT,
                        HealthConstants.CaffeineIntake.START_TIME
                },
                mListenerCaffeineIntake
        );
    }

    private void readToday(long pStatTime, long pEndTime, String hcStartTime, String hcHDT, String[] hcString, HealthResultHolder.ResultListener<ReadResult> pmListener) {
        HealthDataResolver resolver = new HealthDataResolver(mStore, null);

        Filter filter = Filter.and(Filter.greaterThanEquals(hcStartTime, pStatTime),
                Filter.lessThanEquals(hcStartTime, pEndTime));

        HealthDataResolver.ReadRequest request = new ReadRequest.Builder()
                .setDataType(hcHDT)
                .setProperties(hcString)
                .setFilter(filter)
                .build();

        try {
            resolver.read(request).setResultListener(pmListener);
        } catch (Exception e) {
            Log.e(APP_TAG, e.getClass().getName() + " - " + e.getMessage());
        }
    }

    private long getStartTimeOfToday() {
        Calendar today = Calendar.getInstance();

        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // Letzer Monat
        today.add(Calendar.DAY_OF_MONTH, -5);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListenerStepCount = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;COUNT;START_TIME;END_TIME");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("StepCount");
                        sb.append(";");
                        sb.append(String.valueOf(c.getInt(c.getColumnIndex(HealthConstants.StepCount.COUNT))));
                        sb.append(";");
                        sb.append(String.valueOf(c.getLong(c.getColumnIndex(HealthConstants.StepCount.START_TIME))));
                        sb.append(";");
                        sb.append(String.valueOf(c.getLong(c.getColumnIndex(HealthConstants.StepCount.END_TIME))));
                        sb.append(System.getProperty("line.separator"));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            Log.d(APP_TAG,sb.toString());

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, sb.toString());
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    };

    private final HealthResultHolder.ResultListener<ReadResult> mListenerWaterIntake = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;AMOUNT;START_TIME");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("WaterIntake");
                        sb.append(";");
                        sb.append(String.valueOf(c.getInt(c.getColumnIndex(HealthConstants.WaterIntake.AMOUNT))));
                        sb.append(";");
                        sb.append(String.valueOf(c.getLong(c.getColumnIndex(HealthConstants.WaterIntake.START_TIME))));
                        sb.append(System.getProperty("line.separator"));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            Log.d(APP_TAG,sb.toString());

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, sb.toString());
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    };

    private final HealthResultHolder.ResultListener<ReadResult> mListenerCaffeineIntake = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;AMOUNT;START_TIME");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("CaffeineIntake");
                        sb.append(";");
                        sb.append(String.valueOf(c.getInt(c.getColumnIndex(HealthConstants.CaffeineIntake.AMOUNT))));
                        sb.append(";");
                        sb.append(String.valueOf(c.getLong(c.getColumnIndex(HealthConstants.CaffeineIntake.START_TIME))));
                        sb.append(System.getProperty("line.separator"));
                    }
                }
            } finally {
                if (c != null) {
                    c.close();
                }
            }

            Log.d(APP_TAG,sb.toString());

            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, sb.toString());
            pluginResult.setKeepCallback(true);
            callbackContext.sendPluginResult(pluginResult);
        }
    };

    /*
    private final HealthDataObserver mObserver = new HealthDataObserver(null) {

        // Update the step count when a change event is received
        @Override
        public void onChange(String dataTypeName) {
            Log.d(APP_TAG, "Observer receives a data changed event");
            readTodayStepCount();
        }
    };
    */
}
