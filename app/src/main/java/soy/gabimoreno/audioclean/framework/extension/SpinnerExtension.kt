package soy.gabimoreno.audioclean.framework.extension

import android.view.View
import android.widget.AdapterView
import android.widget.Spinner

fun Spinner.setOnItemSelected(listener: (position: Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            listener(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}
