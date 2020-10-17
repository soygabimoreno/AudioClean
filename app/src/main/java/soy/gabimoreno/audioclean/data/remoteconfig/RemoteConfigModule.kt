package soy.gabimoreno.audioclean.data.remoteconfig

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import org.koin.dsl.module

val remoteConfigModule = module {
    single {
        RemoteConfig(
            firebaseRemoteConfig = FirebaseRemoteConfig.getInstance(),
            errorTrackerComponent = get()
        )
    }
}
