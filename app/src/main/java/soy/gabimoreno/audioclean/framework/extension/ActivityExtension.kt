package soy.gabimoreno.audioclean.framework.extension

import android.app.Activity
import android.widget.Toast
import soy.gabimoreno.audioclean.BuildConfig

fun Activity.toast(message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Activity.debugToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
    if (BuildConfig.DEBUG) Toast.makeText(this, message, duration).show()
}
