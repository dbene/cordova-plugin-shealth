/*global cordova, module*/

module.exports = {
	greet: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "greet", [name]);
    },
    connect: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "connect", [name]);
    },
    getData: function (name, successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "SHealth", "getData", [name]);
    }
};
