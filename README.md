# Cordova SHealth Plugin

SHealth Plugin

#bugs ( maybe not up to date)
- filter by given timestamp (in milliseconds) not working
- Samsung SHealth permission windows showing up to often


## Using

Create a new Cordova Project

    $ cordova create hello com.example.helloapp Hello
    
Install the plugin

    $ cd hello
    $ cordova plugin add https://github.com/dbene/cordova-plugin-shealth.git
    

Edit `platforms/android/AndroidManifest.xml` and add the following code inside `manifest/application`

```xml
<meta-data android:name="com.samsung.android.health.permission.read" android:value="com.samsung.health.food_info;com.samsung.health.food_intake;com.samsung.health.uv_exposure;com.samsung.health.weight;com.samsung.health.ambient_temperature;com.samsung.health.body_temperature;com.samsung.health.step_count;com.samsung.health.sleep;com.samsung.health.blood_glucose;com.samsung.health.hba1c;com.samsung.health.oxygen_saturation;com.samsung.health.blood_pressure;com.samsung.health.heart_rate;com.samsung.health.electrocardiogram;com.samsung.health.exercise;com.samsung.health.water_intake;com.samsung.health.caffeine_intake" />
        
```

Edit `www/js/index.js` and add the following code

```js
	var success = function(message) {					
		var div = document.getElementById('greeting-content');
		div.innerHTML = div.innerHTML + message + "<br>";
	}

	var failure = function() {
		alert("Error calling Hello Plugin");
	}

	shealth.getData("1483455300000;1583455300000", success, failure);
```

Install Android platform

    cordova platform add android
    
Run the code

    cordova run 
