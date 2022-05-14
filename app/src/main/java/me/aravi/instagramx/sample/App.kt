package me.aravi.instagramx.sample

import me.aravi.commons.base.BaseApplication
import me.aravi.instagramx.di.instagramX
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

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