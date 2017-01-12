/*global cordova, module*/

module.exports = {
	greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "greet", [name]);
    },
    connectToSHealth: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "connectToSHealth", [name]);
    },
    callHealthPermissionManager: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "callHealthPermissionManager", [name]);
    },
    getDataFromSHealth: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "getDataFromSHealth", [name]);
    }
};
