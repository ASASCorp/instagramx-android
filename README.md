# InstagramX
Unofficial Instagram API for android

# Installation

[![](https://jitpack.io/v/kamaravichow/instagramx-android.svg)](https://jitpack.io/#kamaravichow/instagramx-android)


```groovy
	implementation 'com.github.kamaravichow:instagramx-android:LATEST'
```

Project level build gradle or settings under dependency resolution(new)

```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```


# Usage 

This library uses DI by Koin so you'll have to add below code to your application class

```kotlin
class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        // make sure you add this to your application class
        startKoin {
            androidContext(this@App)
            modules(instagramX)
        }

    }
}
```

Now you can initialise the InstagramApi by injection

```kotlin
val instagramApi: InstagramApi by inject()
```

then use this to init the InstagramX class

```kotlin
val instagramX = InstagramX(instagramApi)
```

Checkout the sample app for more details

**Advanced Usage Guide** : https://docs.aravi.me/android/instagramx

### Todo

- [x] Login & Persistance
- [x] Basic Profile
- [x] Posts
- [x] Followers & Following
- [x] Public Profiles
- [ ] Profile Settings
- [ ] Posts with multiple media support
- [ ] Reels support

Work in progress
