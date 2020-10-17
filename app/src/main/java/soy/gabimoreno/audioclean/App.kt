package soy.gabimoreno.audioclean

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import com.amplitude.api.AmplitudeClient
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import soy.gabimoreno.audioclean.data.analytics.error.ErrorTrackerComponent
import soy.gabimoreno.audioclean.data.remoteconfig.RemoteConfig
import soy.gabimoreno.audioclean.di.serviceLocator
import soy.gabimoreno.audioclean.framework.KLog

class App : Application() {

    companion object {
        const val CHANNEL_ID = "CHANNEL_ID"
    }

    override fun onCreate() {
        super.onCreate()
        KLog.launch(BuildConfig.DEBUG)
        initKoin()
        initFirebase()
        initAmplitude()
        createNotificationChannel()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) logger(AndroidLogger(Level.ERROR))
            androidContext(this@App)
            modules(serviceLocator)
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }

    private fun initAmplitude() {
        val remoteConfig: RemoteConfig by inject()
        val errorTrackerComponent: ErrorTrackerComponent by inject()
        val amplitudeClient: AmplitudeClient by inject()
        GlobalScope.launch {
            remoteConfig.getAmplitudeApiKey()
                .fold({
                    errorTrackerComponent.trackError(it)
                }, { amplitudeApiKey ->
                    amplitudeClient.initialize(this@App, amplitudeApiKey)
                })
        }
    }

    private fun createNotificationChannel() {
        val serviceChannel = NotificationChannel(
            CHANNEL_ID,
            "AudioClean Service Channel",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(serviceChannel)
    }
}
