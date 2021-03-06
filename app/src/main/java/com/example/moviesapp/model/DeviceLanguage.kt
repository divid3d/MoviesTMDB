package com.example.moviesapp.model

data class DeviceLanguage(
    val region: String,
    val languageCode: String
) {
    companion object {
        val default: DeviceLanguage = DeviceLanguage(
            region = "US",
            languageCode = "en-US"
        )
    }
}
