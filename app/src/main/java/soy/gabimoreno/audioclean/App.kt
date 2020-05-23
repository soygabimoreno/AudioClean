package soy.gabimoreno.audioclean

import android.app.Application
import soy.gabimoreno.audioclean.framework.KLog

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        KLog.launch(BuildConfig.DEBUG)
    }
}
