package com.example.moviesapp.other

import com.example.moviesapp.model.Config

fun Config.getImageUrl(
    imagePath: String?,
    size: String = "w500"
): String? {
    return imagePath?.let { path ->
        val baseUrl = imagesConfig.secureBaseUrl
        //val smallestSize = imagesConfig.backdropSizes.first()

        "${baseUrl}${size}${path}"
    }
}