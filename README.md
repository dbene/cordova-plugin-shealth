# Cordova SHealth Plugin

SHealth Plugin

#bugs ( maybe not up to date)
- none that i know specifically


## Using

Create a new Cordova Project

    $ cordova create hello com.example.helloapp Hello
    
Install the plugin

    $ cd hello
    $ cordova plugin add https://github.com/dbene/cordova-plugin-shealth.git
    

Edit `platforms/android/AndroidManifest.xml` and add the following code inside `manifest/application`

```xml
<meta-data android:name="com.samsung.android.health.permission.read" android:value="com.samsung.health.sleep_stage;com.samsung.health.food_info;com.samsung.health.food_intake;com.samsung.health.uv_exposure;com.samsung.health.weight;com.samsung.health.ambient_temperature;com.samsung.health.body_temperature;com.samsung.health.step_count;com.samsung.health.sleep;com.samsung.health.blood_glucose;com.samsung.health.hba1c;com.samsung.health.oxygen_saturation;com.samsung.health.blood_pressure;com.samsung.health.heart_rate;com.samsung.health.electrocardiogram;com.samsung.health.exercise;com.samsung.health.water_intake;com.samsung.health.caffeine_intake" />
        
```

Example of an api call in your `www/js/index.js`

```js
	var success = function(message) {
		alert(message);
	}

	var failure = function(message) {
		alert("ERROR: " + message);
	}
		
	function greet() {
		shealth.greet("World", success, failure);
	}
	
	function apiConnect() {
		shealth.connectToSHealth("", success, failure);
	}
	
	function apiPermissions() {
		shealth.callHealthPermissionManager("", success, failure);
	}
	
	function apiGetData() {
		// Unixtime in milliseconds, [<start_time>,<end_time>]
		shealth.getDataFromSHealth([1484422968001,1489520568002], success, failure);
	}
```

Install Android platform

    cordova platform add android
    
Run the code

    cordova run 
