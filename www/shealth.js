/*global cordova, module*/

module.exports = {
	greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "greet", [name]);
    },
    callHealthPermissionManager: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "callHealthPermissionManager", [name]);
    },
    getData: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "getData", [name]);
    }
};
