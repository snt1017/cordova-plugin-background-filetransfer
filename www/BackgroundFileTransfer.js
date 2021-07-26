var exec = require('cordova/exec');

var MODULE = "BackgroundFileTransfer";

var execute = function (function_name, args) {
    return new Promise(function (resolve, reject) {
        console.debug("BackgroundFileTransfer:execute", { MODULE: MODULE, function_name: function_name, arg: args })
        exec(resolve, reject, MODULE, function_name, args);
    });
}

exports.getDownloadPreferences = function () {
    return execute('getDownloadPreferences', []);
};

exports.setDownloadPreferences = function (args) {
    // Get all params
    let allowNetworkMobile = true;
    if(args.allowNetworkMobile !== undefined) allowNetworkMobile = args.allowNetworkMobile;
    let allowNetworkWifi = true;
    if(args.allowNetworkWifi !== undefined) allowNetworkWifi = args.allowNetworkWifi;
    let allowedOverMetered = true;
    if(args.allowedOverMetered !== undefined) allowedOverMetered = args.allowedOverMetered;
    let allowedOverRoaming = true;
    if(args.allowedOverRoaming !== undefined) allowedOverRoaming = args.allowedOverRoaming;
    let defaultTitle = true;
    if(args.defaultTitle !== undefined) defaultTitle = args.defaultTitle;
    return execute('setDownloadPreferences', [allowNetworkMobile, allowNetworkWifi, allowedOverMetered, allowedOverRoaming, defaultTitle]);
};

exports.addUrlToDownload = function (args) {
    // Get all params
    let url = null;
    if(args.url !== undefined) url = args.url;
    let headers = null;
    if(args.headers !== undefined) headers = args.headers;
    let description = null;
    if(args.description !== undefined) description = args.description;
    let relativePath = null;
    if(args.relativePath !== undefined) relativePath = args.relativePath;
    let filename = null;
    if(args.filename !== undefined) filename = args.filename;
    return execute('addUrlToDownload', [url, headers, description, relativePath, filename]);
};

exports.getFilesByStatus = function (args) {
    // Get all params
    let status = null;
    if(args.status !== undefined) status = args.status;
    return execute('getFilesByStatus', [status]);
};

exports.getFileInfoById = function (args) {
    // Get all params
    let fileId = null;
    if(args.fileId !== undefined) fileId = args.fileId;
    return execute('getFileInfoById', [fileId]);
};
