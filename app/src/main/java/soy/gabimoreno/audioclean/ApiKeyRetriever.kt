package soy.gabimoreno.audioclean

object ApiKeyRetriever {

    init {
        System.loadLibrary("api-keys")
    }

    external fun getAmplitudeApiKey(): String
}
