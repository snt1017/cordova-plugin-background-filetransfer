*Cordova plugin background file transfer*
=========================================================
This plugin givess you the android DownloadManager methods for cordova. At the moment we only supports android projects, but we are planning implement the features for iOS. In the future, the possibility of uploading files will be added too.



# Instalation <a name="instalation"></a>
```bash
cordova plugin add cordova-plugin-background-filetransfer
```

# API Reference <a name="reference"></a>
* [Download service](#downloadService)
    * [.setDownloadPreferences(options)](#downloadService.setDownloadPreferences) : <code>Promise</code>
    * [.getDownloadPreferences()](#downloadService.getDownloadPreferences) : <code>Promise</code>
    * [DownloadPreferences](#downloadService.DownloadPreferences) : <code>Object</code>
    * [.addUrlToDownload(options)](#downloadService.addUrlToDownload) : <code>Promise</code>
    * [DownloadOptions](#downloadService.DownloadOptions) : <code>Object</code>
    * [.getFilesByStatus()](#downloadService.getFilesByStatus) : <code>Promise</code>
    * [.getFileInfoById()](#downloadService.getFileInfoById) : <code>Promise</code>



## Download service <a name="downloadService"></a>


### .setDownloadPreferences(options) <a name="downloadService.setDownloadPreferences"></a>

Set default values of the plugin.

| Param | Type | Description |
| --- | --- | --- |
| options | <code>[DownloadPreferences](#downloadService.DownloadPreferences)</code> | Default download preferences |


```javascript
cordova.plugins.BackgroundFileTransfer.setDownloadPreferences(downloadPreferences).then((result)=> {
  console.log(result);
}, (err) => {
  console.error(err);
});
```

### .getDownloadPreferences() <a name="downloadService.getDownloadPreferences"></a>

Get default values of the plugin.

```javascript
cordova.plugins.BackgroundFileTransfer.getDownloadPreferences().then((result)=> {
  console.log(result);
}, (err) => {
  console.error(err);
});
```

### DownloadPreferences <a name="downloadService.DownloadPreferences"></a>

Optional parameters to customize download settings.


| Name | Type | Default | Description |
| --- | --- | --- | --- |
| allowNetworkMobile | <code>Boolean</code> | <code>true</code> | Allow download files from mobile network |
| allowNetworkWifi | <code>Boolean</code> | <code>true</code> | Allow download files from wifi network |
| allowedOverMetered | <code>Boolean</code> | <code>true</code> | Allow download files in metered networks |
| allowedOverRoaming | <code>Boolean</code> | <code>true</code> | Allow download files when roaming is activated |
| defaultTitle | <code>String</code> | <code>"DownloadService"</code> | This is notification title |

### addUrlToDownload(downloadOptions) <a name="downloadService.addUrlToDownload"></a>

Add new url to enqueue download files. It returns the id of the file download.

```javascript
cordova.plugins.BackgroundFileTransfer.addUrlToDownload(downloadOptions).then((result)=> {
  console.log(result);
}, (err) => {
  console.error(err);
});
```

### DownloadOptions <a name="downloadService.DownloadOptions"></a>

The options that are required for add new download to enqueue.

| Name | Type | Default | Description |
| --- | --- | --- | --- |
| url | <code>String</code> | Required | Allow download files from mobile network. |
| filename | <code>String</code> | Required | Filename, the name under which the file will be saved in the folder.  |
| relativePath | <code>String</code> | Optional value | Name of the folder in which the file is to be saved the file to be saved the file to be saved. |
| headers | <code>{"KEY": "VALUE"}</code> | Optional value | Headers for download if it are required. |
| description | <code>String</code> | filename | The description of the download, this value is displayed in the notification. |


### getFilesByStatus(downloadFileStatus) <a name="downloadService.getFilesByStatus"></a>

Returns the list of files filtered by status. Posible values:

| Status | Value |
| --- | --- |
| PENDING | 1 |
| RUNNING | 2 |
| PAUSED | 4 |
| SUCCESSFUL | 8 |
| FAILED | 16 |


```javascript
cordova.plugins.BackgroundFileTransfer.getFilesByStatus(downloadFileStatus).then((result)=> {
  console.log(result);
}, (err) => {
  console.error(err);
});
```


### getFileInfoById(fileId) <a name="downloadService.getFileInfoById"></a>

Returns the file list filtered by downloaded id.

```javascript
cordova.plugins.BackgroundFileTransfer.getFileInfoById(fileId).then((result)=> {
  console.log(result);
}, (err) => {
  console.error(err);
});
```
