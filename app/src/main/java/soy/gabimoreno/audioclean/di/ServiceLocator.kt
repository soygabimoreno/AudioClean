package soy.gabimoreno.audioclean.di

import soy.gabimoreno.audioclean.data.analytics.analyticsTrackerModule
import soy.gabimoreno.audioclean.data.analytics.error.errorTrackerModule
import soy.gabimoreno.audioclean.data.remoteconfig.remoteConfigModule

val serviceLocator = listOf(
    remoteConfigModule,

    analyticsTrackerModule,
    errorTrackerModule,

    appModule
)
