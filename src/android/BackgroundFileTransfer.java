package cordova.plugin.BackgroundFileTransfer;

import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cordova.plugin.PowerOptimization.ProtectedApps;

import static android.net.ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED;
import static android.net.ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED;
import static android.net.ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED;

/**
 * This class echoes a string called from JavaScript.
 */
public class BackgroundFileTransfer extends CordovaPlugin {

  public static final String TAG = "BackgroundFileTransfer";

  @Override
  public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
    Context context = cordova.getActivity().getApplicationContext();
    String packageName = context.getPackageName();
    Log.d(TAG, "Action: " + action + ", Package name: " + packageName);
    if (action.equals("getDownloadPreferences")) {
      this.getDownloadPreferences(context, callbackContext);
      return true;
      // Check if the app is allowed to do this https://developer.android.com/training/monitoring-device-state/doze-standby
    } else if (action.equals("setDownloadPreferences")) {
      this.setDownloadPreferences(context, callbackContext, args);
      return true;
    } else if (action.equals("addUrlToDownload")) {
      this.addUrlToDownload(context, callbackContext, args);
      return true;
    } else if (action.equals("getFilesByStatus")) {
      int status = args.optInt(0, DownloadManager.STATUS_SUCCESSFUL);
      this.getFilesByStatus(context, callbackContext, status);
      return true;
    } else if (action.equals("getFileInfoById")) {
      long id = args.optLong(0, 0);
      this.getFileInfoById(context, callbackContext, id);
      return true;
    }
    return false;
  }


  public void getDownloadPreferences(Context context, CallbackContext callbackContext) throws JSONException {
    // Return JSON download preferences
    callbackContext.success(DownloadService.getDownloadPreferences(context));
  }


  public void setDownloadPreferences(Context context, CallbackContext callbackContext, JSONArray args) throws JSONException {
    boolean allowNetworkMobile = args.optBoolean(0, true);
    boolean allowNetworkWifi = args.optBoolean(1, true);
    boolean allowedOverMetered = args.optBoolean(2, true);
    boolean allowedOverRoaming = args.optBoolean(3, true);
    String defaultTitle = args.optString(4, DownloadService.DEFAULT_SERVICE_TITLE);
    // Call method, it returns callback
    DownloadService.setDownloadPreferences(context, callbackContext, allowNetworkMobile, allowNetworkWifi, allowedOverMetered, allowedOverRoaming, defaultTitle);
  }


  public void addUrlToDownload(Context context, CallbackContext callbackContext, JSONArray args) throws JSONException {
    String url = args.optString(0, null);
    if (url.equals("null")) url = null;
    if (url == null) {
      callbackContext.error("url param is required");
      return;
    }
    JSONObject headers = args.optJSONObject(1);
    String description = args.optString(2, null);
    if (description.equals("null")) description = null;
    String relativePath = args.optString(3, null);
    if (relativePath.equals("null")) relativePath = null;
    String filename = args.optString(4, null);
    if (filename.equals("null")) filename = null;
    if (filename == null) {
      callbackContext.error("filename param is required");
      return;
    }
    // Call method, it returns callback
    DownloadService.addUrlToDownload(context, callbackContext, url, headers, description, relativePath, filename);
  }

  public void getFilesByStatus(Context context, CallbackContext callbackContext, long id) throws JSONException {
    DownloadService.getFileInfoById(context, callbackContext, id);
  }

  public void getFileInfoById(Context context, CallbackContext callbackContext, long id) throws JSONException {
    DownloadService.getFileInfoById(context, callbackContext, id);
  }

}
