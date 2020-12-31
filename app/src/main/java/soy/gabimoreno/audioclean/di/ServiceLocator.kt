package soy.gabimoreno.audioclean.di

import soy.gabimoreno.audioclean.data.analytics.analyticsTrackerModule
import soy.gabimoreno.audioclean.data.analytics.error.errorTrackerModule

val serviceLocator = listOf(
    analyticsTrackerModule,
    errorTrackerModule,

    appModule
)
