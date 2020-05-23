package soy.gabimoreno.audioclean.presenter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import soy.gabimoreno.audioclean.R
import soy.gabimoreno.audioclean.domain.AudioProcessor

class MainActivity : AppCompatActivity() {

    private val ap = AudioProcessor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ap.start()
    }
}
