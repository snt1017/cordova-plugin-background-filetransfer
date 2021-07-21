package cordova.plugin.BackgroundFileTransfer;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import org.apache.cordova.CallbackContext;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class DownloadService {


  private static final String TAG = "DownloadService";
  private static final String DEFAULT_SERVICE_TITLE = "DownloadService";


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
  public static void addUrlToDownload(Activity activity, CallbackContext callbackContext, String url, JSONObject headers, String description,
                                      String destinationUri) throws JSONException {
    Log.d(TAG, "addUrlToDownload: " + url);
    JSONObject result = new JSONObject();
    Uri downloadUri = new Uri.parse(url);
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
    if(description!=null)request.setDescription(description);
    // TODO Validate this
    // Add destination uri
    if(destinationUri!=null)request.setDestinationUri(Uri.parse(destinationUri));
    // TODO Add preferences
    // Get download manager
    DownloadManager dm = (DownloadManager)activity.getSystemService(activity.DOWNLOAD_SERVICE);
    long resultId = dm.enqueue(request);
    // Save id into result JSON
    result.put("id", resultId);
    // Return result to cordova app
    callbackContext.success(result);
  }


}
