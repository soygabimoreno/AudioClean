package soy.gabimoreno.audioclean

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import soy.gabimoreno.audioclean.di.serviceLocator
import soy.gabimoreno.audioclean.framework.KLog

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        KLog.launch(BuildConfig.DEBUG)
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) logger(AndroidLogger(Level.DEBUG))
            androidContext(this@App)
            modules(serviceLocator)
        }
    }
}
