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

    public void start(long pStartTime, long pEndTime) {
        Log.d(APP_TAG,"Time: " + pStartTime + " - " + pEndTime);

        // Debug
        long startTime = pStartTime; // 1480546800000l;
        long endTime = pEndTime; // System.currentTimeMillis();

        // StepCount
        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.StepCount.START_TIME,
                HealthConstants.StepCount.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.StepCount.START_TIME,
                        HealthConstants.StepCount.END_TIME,
                        HealthConstants.StepCount.TIME_OFFSET,
                        HealthConstants.StepCount.COUNT,
                        HealthConstants.StepCount.DISTANCE,
                        HealthConstants.StepCount.CALORIE,
                        HealthConstants.StepCount.SPEED,
                        HealthConstants.StepCount.SAMPLE_POSITION_TYPE
                },
                mListenerStepCount
        );

        // Exercise
        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.Exercise.START_TIME,
                HealthConstants.Exercise.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.Exercise.START_TIME,
                        HealthConstants.Exercise.END_TIME,
                        HealthConstants.Exercise.TIME_OFFSET,
                        HealthConstants.Exercise.CALORIE,
                        HealthConstants.Exercise.DURATION,
                        HealthConstants.Exercise.EXERCISE_TYPE,
                        HealthConstants.Exercise.EXERCISE_CUSTOM_TYPE,
                        HealthConstants.Exercise.DISTANCE,
                        HealthConstants.Exercise.ALTITUDE_GAIN,
                        HealthConstants.Exercise.ALTITUDE_LOSS,
                        HealthConstants.Exercise.COUNT,
                        HealthConstants.Exercise.COUNT_TYPE,
                        HealthConstants.Exercise.MAX_SPEED,
                        HealthConstants.Exercise.MEAN_SPEED,
                        HealthConstants.Exercise.MAX_CALORICBURN_RATE,
                        HealthConstants.Exercise.MEAN_CALORICBURN_RATE,
                        HealthConstants.Exercise.MAX_CADENCE,
                        HealthConstants.Exercise.MEAN_CADENCE,
                        HealthConstants.Exercise.MAX_HEART_RATE,
                        HealthConstants.Exercise.MEAN_HEART_RATE,
                        HealthConstants.Exercise.MIN_HEART_RATE,
                        HealthConstants.Exercise.MAX_ALTITUDE,
                        HealthConstants.Exercise.MIN_ALTITUDE,
                        HealthConstants.Exercise.INCLINE_DISTANCE,
                        HealthConstants.Exercise.DECLINE_DISTANCE,
                        HealthConstants.Exercise.MAX_POWER,
                        HealthConstants.Exercise.MEAN_POWER,
                        HealthConstants.Exercise.MEAN_RPM,
                        //HealthConstants.Exercise.LIVE_DATA,
                        HealthConstants.Exercise.LOCATION_DATA
                },
                mListenerExercise
        );

        // Sleep
        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.Sleep.START_TIME,
                HealthConstants.Sleep.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.Sleep.START_TIME,
                        HealthConstants.Sleep.END_TIME,
                        HealthConstants.Sleep.TIME_OFFSET
                },
                mListenerSleep
        );

        // SleepStage
        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.SleepStage.START_TIME,
                HealthConstants.SleepStage.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.SleepStage.START_TIME,
                        HealthConstants.SleepStage.END_TIME,
                        HealthConstants.SleepStage.TIME_OFFSET,
                        HealthConstants.SleepStage.SLEEP_ID,
                        HealthConstants.SleepStage.STAGE
                },
                mListenerSleepStage
        );

        // ToDo FoodInfo

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.FoodIntake.START_TIME,
                HealthConstants.FoodIntake.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.FoodIntake.START_TIME,
                        HealthConstants.FoodIntake.TIME_OFFSET,
                        HealthConstants.FoodIntake.CALORIE,
                        HealthConstants.FoodIntake.FOOD_INFO_ID,
                        HealthConstants.FoodIntake.AMOUNT,
                        HealthConstants.FoodIntake.UNIT,
                        HealthConstants.FoodIntake.NAME,
                        HealthConstants.FoodIntake.MEAL_TYPE
                },
                mListenerFoodIntake
        );

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.WaterIntake.START_TIME,
                HealthConstants.WaterIntake.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.WaterIntake.START_TIME,
                        HealthConstants.WaterIntake.TIME_OFFSET,
                        HealthConstants.WaterIntake.AMOUNT,
                        HealthConstants.WaterIntake.UNIT_AMOUNT
                },
                mListenerWaterIntake
        );

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.CaffeineIntake.START_TIME,
                HealthConstants.CaffeineIntake.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.CaffeineIntake.START_TIME,
                        HealthConstants.CaffeineIntake.TIME_OFFSET,
                        HealthConstants.CaffeineIntake.AMOUNT,
                        HealthConstants.CaffeineIntake.UNIT_AMOUNT
                },
                mListenerCaffeineIntake
        );

        // ToDo Weight

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.HeartRate.START_TIME,
                HealthConstants.HeartRate.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.HeartRate.START_TIME,
                        HealthConstants.HeartRate.END_TIME,
                        HealthConstants.HeartRate.TIME_OFFSET,
                        HealthConstants.HeartRate.HEART_RATE,
                        HealthConstants.HeartRate.HEART_BEAT_COUNT
                },
                mListenerHeartRate
        );

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.BodyTemperature.START_TIME,
                HealthConstants.BodyTemperature.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.BodyTemperature.START_TIME,
                        HealthConstants.BodyTemperature.TIME_OFFSET,
                        HealthConstants.BodyTemperature.TEMPERATURE
                },
                mListenerBodyTemperature

        );

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.BloodPressure.START_TIME,
                HealthConstants.BloodPressure.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.BloodPressure.START_TIME,
                        HealthConstants.BloodPressure.TIME_OFFSET,
                        HealthConstants.BloodPressure.SYSTOLIC,
                        HealthConstants.BloodPressure.DIASTOLIC,
                        HealthConstants.BloodPressure.MEAN,
                        HealthConstants.BloodPressure.PULSE
                },
                mListenerBloodPressure
        );

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.BloodGlucose.START_TIME,
                HealthConstants.BloodGlucose.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.BloodGlucose.START_TIME,
                        HealthConstants.BloodGlucose.TIME_OFFSET,
                        HealthConstants.BloodGlucose.GLUCOSE,
                        HealthConstants.BloodGlucose.MEAL_TIME,
                        HealthConstants.BloodGlucose.MEAL_TYPE,
                        HealthConstants.BloodGlucose.MEASUREMENT_TYPE,
                        HealthConstants.BloodGlucose.SAMPLE_SOURCE_TYPE
                },
                mListenerBloodGlucose
        );

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.OxygenSaturation.START_TIME,
                HealthConstants.OxygenSaturation.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.OxygenSaturation.START_TIME,
                        HealthConstants.OxygenSaturation.END_TIME,
                        HealthConstants.OxygenSaturation.TIME_OFFSET,
                        HealthConstants.OxygenSaturation.SPO2,
                        HealthConstants.OxygenSaturation.HEART_RATE
                },
                mListenerOxygenSaturation
        );

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.HbA1c.START_TIME,
                HealthConstants.HbA1c.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.HbA1c.START_TIME,
                        HealthConstants.HbA1c.TIME_OFFSET,
                        HealthConstants.HbA1c.HBA1C
                },
                mListenerHbA1c
        );

        // ToDo Electrocardiogram

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.AmbientTemperature.START_TIME,
                HealthConstants.AmbientTemperature.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.AmbientTemperature.START_TIME,
                        HealthConstants.AmbientTemperature.TIME_OFFSET,
                        HealthConstants.AmbientTemperature.TEMPERATURE,
                        HealthConstants.AmbientTemperature.HUMIDITY,
                        HealthConstants.AmbientTemperature.LATITUDE,
                        HealthConstants.AmbientTemperature.LONGITUDE,
                        HealthConstants.AmbientTemperature.ALTITUDE,
                        HealthConstants.AmbientTemperature.ACCURACY
                },
                mListenerAmbientTemperature
        );

        readHealthConstant(
                startTime,
                endTime,
                HealthConstants.UvExposure.START_TIME,
                HealthConstants.UvExposure.HEALTH_DATA_TYPE,
                new String[] {
                        HealthConstants.UvExposure.START_TIME,
                        HealthConstants.UvExposure.TIME_OFFSET,
                        HealthConstants.UvExposure.UV_INDEX,
                        HealthConstants.UvExposure.LATITUDE,
                        HealthConstants.UvExposure.LONGITUDE,
                        HealthConstants.UvExposure.ALTITUDE,
                        HealthConstants.UvExposure.ACCURACY
                },
                mListenerUvExposure
        );
    }

    private void readHealthConstant(long pStatTime, long pEndTime, String hcStartTime, String hcHDT, String[] hcString, HealthResultHolder.ResultListener<ReadResult> pmListener) {
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
        today.add(Calendar.DAY_OF_MONTH, -8);

        return today.getTimeInMillis();
    }

    private final HealthResultHolder.ResultListener<ReadResult> mListenerStepCount = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;END_TIME;TIME_OFFSET;COUNT;DISTANCE;CALORIE;SPEED;SAMPLE_POSITION_TYPE");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {

                        sb.append("StepCount");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.StepCount.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.StepCount.END_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.StepCount.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.StepCount.COUNT)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.StepCount.DISTANCE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.StepCount.CALORIE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.StepCount.SPEED)));
                        sb.append(";");
                        sb.append(c.getInt(c.getColumnIndex(HealthConstants.StepCount.SAMPLE_POSITION_TYPE)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerExercise = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;END_TIME;TIME_OFFSET;CALORIE;" +
                        "DURATION;EXERCISE_TYPE;EXERCISE_CUSTOM_TYPE;DISTANCE;ALTITUDE_GAIN;" +
                        "ALTITUDE_LOSS;COUNT;COUNT_TYPE;MAX_SPEED;MEAN_SPEED;" +
                        "MAX_CALORICBURN_RATE;MEAN_CALORICBURN_RATE;MAX_CADENCE;MEAN_CADENCE;MAX_HEART_RATE" +
                        "MEAN_HEART_RATE;MIN_HEART_RATE;MAX_ALTITUDE;MEAN_ALTITUDE;INCLINE_DISTANCE;DECLINE_DISTANCE;MAX_POWER" +
                        "MEAN_POWER;MEAN_RPM;" + /*LIVE_DATA*/ "LOCATION_DATA");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {

                        sb.append("Exercise");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.END_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.CALORIE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.DURATION)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.EXERCISE_TYPE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.EXERCISE_CUSTOM_TYPE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.DISTANCE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.ALTITUDE_GAIN)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.ALTITUDE_LOSS)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.COUNT)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.COUNT_TYPE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MAX_SPEED)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MEAN_SPEED)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MAX_CALORICBURN_RATE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MEAN_CALORICBURN_RATE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MAX_CADENCE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MAX_CADENCE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MAX_HEART_RATE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MEAN_HEART_RATE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MIN_HEART_RATE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MAX_ALTITUDE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MIN_ALTITUDE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.INCLINE_DISTANCE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.DECLINE_DISTANCE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MAX_POWER)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MEAN_POWER)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.MEAN_RPM)));
                        sb.append(";");
                        //sb.append(c.getString(c.getColumnIndex(HealthConstants.Exercise.LIVE_DATA)));
                        //sb.append(";");
                        sb.append(c.getInt(c.getColumnIndex(HealthConstants.Exercise.LOCATION_DATA)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerSleep = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;END_TIME;TIME_OFFSET");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("Sleep");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Sleep.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Sleep.END_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.Sleep.TIME_OFFSET)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerSleepStage = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;END_TIME;TIME_OFFSET;SLEEP_ID;STAGE");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("SleepStage");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.SleepStage.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.SleepStage.END_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.SleepStage.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.SleepStage.SLEEP_ID)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.SleepStage.STAGE)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerFoodIntake = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;TIME_OFFSET;CALORIE;FOOD_INFO_ID;AMOUNT;UNIT;NAME;MEAL_TYPE");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("FoodIntake");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.FoodIntake.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.FoodIntake.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.FoodIntake.CALORIE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.FoodIntake.FOOD_INFO_ID)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.FoodIntake.AMOUNT)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.FoodIntake.UNIT)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.FoodIntake.NAME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.FoodIntake.MEAL_TYPE)));
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

                sb.append("NAME;START_TIME;TIME_OFFSET;AMOUNT;UNIT_AMOUNT");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("WaterIntake");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.WaterIntake.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.WaterIntake.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.WaterIntake.AMOUNT)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.WaterIntake.UNIT_AMOUNT)));
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

                sb.append("NAME;START_TIME;TIME_OFFSET;AMOUNT;UNIT_AMOUNT");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("CaffeineIntake");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.CaffeineIntake.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.CaffeineIntake.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.CaffeineIntake.AMOUNT)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.CaffeineIntake.UNIT_AMOUNT)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerHeartRate = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;END_TIME;TIME_OFFSET;HEART_RATE;HEART_BEAT_COUNT");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("HeartRate");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.HeartRate.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.HeartRate.END_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.HeartRate.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.HeartRate.HEART_RATE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.HeartRate.HEART_BEAT_COUNT)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerBodyTemperature = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;TIME_OFFSET;TEMPERATURE");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("BodyTemperature");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BodyTemperature.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BodyTemperature.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BodyTemperature.TEMPERATURE)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerBloodPressure = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;TIME_OFFSET;SYSTOLIC;DIASTOLIC;MEAN;PULSE");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("BloodPressure");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodPressure.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodPressure.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodPressure.SYSTOLIC)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodPressure.DIASTOLIC)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodPressure.MEAN)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodPressure.PULSE)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerBloodGlucose = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;TIME_OFFSET;GLUCOSE;MEAL_TIME;MEAL_TYPE;MEASUREMENT_TYPE;SAMPLE_SOURCE_TYPE");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("BloodGlucose");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodGlucose.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodGlucose.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodGlucose.GLUCOSE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodGlucose.MEAL_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodGlucose.MEAL_TYPE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodGlucose.MEASUREMENT_TYPE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.BloodGlucose.SAMPLE_SOURCE_TYPE)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerOxygenSaturation = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;END_TIME;TIME_OFFSET;SPO2;HEART_RATE");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("OxygenSaturation");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.OxygenSaturation.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.OxygenSaturation.END_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.OxygenSaturation.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.OxygenSaturation.SPO2)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.OxygenSaturation.HEART_RATE)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerHbA1c = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;TIME_OFFSET;HBA1C");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("HbA1c");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.HbA1c.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.HbA1c.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.HbA1c.HBA1C)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerAmbientTemperature = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;TIME_OFFSET;TEMPERATURE;HUMIDITY;LATITUDE;LONGITUDE;ALTITUDE;ACCURACY");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("AmbientTemperature");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.AmbientTemperature.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.AmbientTemperature.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.AmbientTemperature.TEMPERATURE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.AmbientTemperature.HUMIDITY)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.AmbientTemperature.LATITUDE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.AmbientTemperature.LONGITUDE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.AmbientTemperature.ALTITUDE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.AmbientTemperature.ACCURACY)));
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

    private final HealthResultHolder.ResultListener<ReadResult> mListenerUvExposure = new HealthResultHolder.ResultListener<ReadResult>() {
        @Override
        public void onResult(ReadResult result) {
            Cursor c = null;
            StringBuilder sb = new StringBuilder();

            try {
                c = result.getResultCursor();

                sb.append("NAME;START_TIME;TIME_OFFSET;UV_INDEX;LATITUDE;LONGITUDE;ALTITUDE;ACCURACY");
                sb.append(System.getProperty("line.separator"));

                if (c != null) {
                    while (c.moveToNext()) {
                        sb.append("UvExposure");
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.UvExposure.START_TIME)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.UvExposure.TIME_OFFSET)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.UvExposure.UV_INDEX)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.UvExposure.LATITUDE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.UvExposure.LONGITUDE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.UvExposure.ALTITUDE)));
                        sb.append(";");
                        sb.append(c.getString(c.getColumnIndex(HealthConstants.UvExposure.ACCURACY)));
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

}
