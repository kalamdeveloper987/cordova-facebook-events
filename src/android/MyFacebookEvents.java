package com.kadev.facebookevents;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Iterator;

public class MyFacebookEvents extends CordovaPlugin {

    private AppEventsLogger logger;

    @Override
    public boolean execute(String action, JSONArray args,
            CallbackContext callbackContext) throws JSONException {

        // 🔹 Initialize Facebook SDK dynamically from JS
        // if ("init".equals(action)) {

        // final String appId = args.getString(0);
        // final String clientToken = args.getString(1);

        // cordova.getActivity().runOnUiThread(() -> {
        // try {
        // Context ctx = cordova.getActivity().getApplicationContext();

        // FacebookSdk.setApplicationId(appId);
        // FacebookSdk.setClientToken(clientToken);
        // FacebookSdk.setAutoInitEnabled(true);
        // FacebookSdk.sdkInitialize(ctx);
        // FacebookSdk.fullyInitialize();

        // logger = AppEventsLogger.newLogger(cordova.getActivity());
        // AppEventsLogger.activateApp(ctx);

        // callbackContext.success("Facebook SDK initialized");
        // } catch (Exception e) {
        // callbackContext.error("Init failed: " + e.getMessage());
        // }
        // });

        // return true;
        // }

        if ("init".equals(action)) {

            final String appId = args.getString(0);
            final String clientToken = args.getString(1);

            cordova.getActivity().runOnUiThread(() -> {
                try {
                    // Context ctx = cordova.getActivity().getApplicationContext();

                    // // ✅ Correct order
                    // FacebookSdk.setApplicationId(appId);
                    // FacebookSdk.setClientToken(clientToken);

                    // FacebookSdk.sdkInitialize(ctx); // <-- MUST be first init call
                    // FacebookSdk.setAutoInitEnabled(true);
                    // FacebookSdk.fullyInitialize();

                    // logger = AppEventsLogger.newLogger(cordova.getActivity());
                    // AppEventsLogger.activateApp(ctx);

                    Context ctx = cordova.getActivity().getApplicationContext();

                    FacebookSdk.setApplicationId(appId);
                    FacebookSdk.setClientToken(clientToken);

                    FacebookSdk.sdkInitialize(ctx);
                    FacebookSdk.setAutoInitEnabled(true);
                    FacebookSdk.fullyInitialize();

                    logger = AppEventsLogger.newLogger(cordova.getActivity());
                    AppEventsLogger.activateApp(cordova.getActivity().getApplication()); // ✅

                    callbackContext.success("Facebook SDK initialized");
                } catch (Exception e) {
                    callbackContext.error("Init failed: " + e.getMessage());
                }
            });

            return true;
        }

        // 🔒 Ensure SDK is initialized before logging
        if (logger == null) {
            callbackContext.error("Facebook SDK not initialized. Call init() first.");
            return true;
        }

        // 🔹 Log custom event
        if ("logEvent".equals(action)) {
            String name = args.getString(0);
            JSONObject params = args.getJSONObject(1);

            Bundle bundle = new Bundle();
            Iterator<String> keys = params.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                bundle.putString(key, params.getString(key));
            }

            logger.logEvent(name, bundle);
            callbackContext.success("FB Event sent: " + name);
            return true;
        }

        // 🔹 Log purchase event
        if ("logPurchase".equals(action)) {
            double amount = args.getDouble(0);
            String currency = args.getString(1);

            logger.logPurchase(
                    BigDecimal.valueOf(amount),
                    Currency.getInstance(currency));

            callbackContext.success("FB Purchase sent");
            return true;
        }

        // 🔹 Set user ID for events
        if ("setUserId".equals(action)) {
            String userId = args.getString(0);

            AppEventsLogger.setUserID(userId);
            callbackContext.success("FB User ID set: " + userId);
            return true;
        }

        return false;
    }
}
