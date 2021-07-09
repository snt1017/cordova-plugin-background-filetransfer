var exec = require('cordova/exec');

var MODULE = "BackgroundFileTransfer";

var execute = function (function_name, arg0) {
    return new Promise(function (resolve, reject) {
        console.debug("BackgroundFileTransfer:execute", { MODULE: MODULE, function_name: function_name, arg: [arg0] })
        exec(resolve, reject, MODULE, function_name, [arg0]);
    });
}

var executeBoolean = function (function_name, arg0) {
    return new Promise(function (resolve, reject) {
        var success = function (data) {
            if (data == "true") resolve(true);
            else resolve(false);
        }
        exec(success, reject, MODULE, function_name, [arg0]);
    });
}

exports.download = function (arg0) {
    return execute('DOWNLOAD', arg0);
};