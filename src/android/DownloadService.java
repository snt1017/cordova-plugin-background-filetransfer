package cordova.plugin.BackgroundFileTransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import org.apache.cordova.CallbackContext;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;

import org.apache.cordova.camera.FileProvider;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class DownloadService {


  private static final String TAG = "DownloadService";
  public static final String DEFAULT_SERVICE_TITLE = "DownloadService";


  public static JSONObject getDownloadPreferences(Context context) throws JSONException {
    Log.d(TAG, "getDownloadPreferences");
    SharedPreferences settings = context.getSharedPreferences("BG-FileTransfer", Context.MODE_PRIVATE);
    // Get settings
    boolean allowNetworkMobile = settings.getBoolean("allowNetworkMobile", true);
    boolean allowNetworkWifi = settings.getBoolean("allowNetworkWifi", true);
    boolean allowedOverMetered = settings.getBoolean("allowedOverMetered", true);
    boolean allowedOverRoaming = settings.getBoolean("allowedOverRoaming", true);
    String defaultTitle = settings.getString("defaultTitle", DEFAULT_SERVICE_TITLE);
    // Add settings to JSON
    JSONObject result = new JSONObject();
    result.put("allowNetworkMobile", allowNetworkMobile);
    result.put("allowNetworkWifi", allowNetworkWifi);
    result.put("allowedOverMetered", allowedOverMetered);
    result.put("allowedOverRoaming", allowedOverRoaming);
    result.put("defaultTitle", defaultTitle);
    // Return JSON
    return result;
  }

  public static void setDownloadPreferences(Context context, CallbackContext callbackContext,
                                            boolean allowNetworkMobile, boolean allowNetworkWifi, boolean allowedOverMetered,
                                            boolean allowedOverRoaming, String defaultTitle) throws JSONException {
    Log.d(TAG, "setDownloadPreferences");
    // Get preferences of the configuration
    SharedPreferences settings = context.getSharedPreferences("BG-FileTransfer", Context.MODE_PRIVATE);
    // Open editor
    final SharedPreferences.Editor editor = settings.edit();
    // Update values
    editor.putBoolean("allowNetworkMobile", allowNetworkMobile);
    editor.putBoolean("allowNetworkWifi", allowNetworkWifi);
    editor.putBoolean("allowedOverMetered", allowedOverMetered);
    editor.putBoolean("allowedOverRoaming", allowedOverRoaming);
    editor.putString("defaultTitle", defaultTitle);
    // Save preferences
    editor.apply();
    // Return updated preferences
    callbackContext.success(getDownloadPreferences(context));
  }


  // This method checks if can fire the one of a list of intents and fire it
  public static void addUrlToDownload(Context context, CallbackContext callbackContext, String url, JSONObject headers, String description,
                                      String relativePath, String filename) throws JSONException {
    Log.d(TAG, "addUrlToDownload: " + url + " - " + description + " - " + relativePath + " - " + filename);
    JSONObject result = new JSONObject();
    Uri downloadUri = Uri.parse(url);
    Request request = new Request(downloadUri);

    // Add headers
    if (headers != null) {
      Iterator<String> keys = headers.keys();
      while (keys.hasNext()) {
        String key = keys.next();
        // If is string add it to headers, avoid it if is not string
        if (headers.get(key) instanceof String) {
          String value = (String) headers.get(key);
          request.addRequestHeader(key, value);
        }
      }
    }

    // Add description
    if (description != null) request.setDescription(description);
    else request.setDescription(filename);

    if (relativePath == null) relativePath = "";
    request.setDestinationInExternalFilesDir(context, relativePath, filename);

    SharedPreferences settings = context.getSharedPreferences("BG-FileTransfer", Context.MODE_PRIVATE);
    boolean allowNetworkMobile = settings.getBoolean("allowNetworkMobile", true);
    boolean allowNetworkWifi = settings.getBoolean("allowNetworkWifi", true);
    boolean allowedOverMetered = settings.getBoolean("allowedOverMetered", true);
    boolean allowedOverRoaming = settings.getBoolean("allowedOverRoaming", true);
    String defaultTitle = settings.getString("defaultTitle", DEFAULT_SERVICE_TITLE);

    if (!allowNetworkMobile) request.setAllowedNetworkTypes(Request.NETWORK_MOBILE);
    if (!allowNetworkWifi) request.setAllowedNetworkTypes(Request.NETWORK_WIFI);
    request.setAllowedOverMetered(allowedOverMetered);
    request.setAllowedOverRoaming(allowedOverRoaming);
    request.setTitle(defaultTitle);
    // Get download manager
    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
    try {
      long resultId = dm.enqueue(request);
      // Save id into result JSON
      result.put("id", resultId);
      // Return result to cordova app
      callbackContext.success(result);
    } catch (Exception e) {
      Log.d(TAG, "startDownload = " + e.getMessage());
      e.printStackTrace();
      callbackContext.success(e.getMessage());
    }
  }

  public static JSONArray makeDownloadQuery(Context context, DownloadManager.Query query) throws JSONException {
    DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

    Cursor c = downloadManager.query(query);
    int columns = c.getColumnCount();
    JSONArray list = new JSONArray();
    while (c.moveToNext()) {
      JSONObject obj = new JSONObject();
      for (int i = 1; i < columns; ++i) {
        try {
          // This is deprecated attribute
          if(c.getColumnName(i).equals(DownloadManager.COLUMN_LOCAL_FILENAME)){
            continue;
          }
          switch (c.getType(i)) {
            case Cursor.FIELD_TYPE_FLOAT:
              obj.put(c.getColumnName(i), c.getFloat(i));
              break;
            case Cursor.FIELD_TYPE_INTEGER:
              obj.put(c.getColumnName(i), c.getInt(i));
              break;
            case Cursor.FIELD_TYPE_STRING:
              obj.put(c.getColumnName(i), c.getString(i));
              break;
          }
        } catch (Exception e) {
          Log.w(TAG, e);
        }
      }
      list.put(obj);
    }
    return list;
  }


  public static boolean getFilesByStatus(Context context, CallbackContext callbackContext, int status) throws JSONException {
    DownloadManager.Query query = new DownloadManager.Query();
    query.setFilterByStatus(status);
    JSONArray list = makeDownloadQuery(context, query);
    callbackContext.success(list);
    return false;
  }


  public static boolean getFileInfoById(Context context, CallbackContext callbackContext, long id) throws JSONException {
    DownloadManager.Query query = new DownloadManager.Query();
    query.setFilterById(id);
    JSONArray list = makeDownloadQuery(context, query);
    callbackContext.success(list);
    return false;
  }

}
