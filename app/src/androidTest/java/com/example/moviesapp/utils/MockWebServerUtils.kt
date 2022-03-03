package com.example.moviesapp.utils

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source
import java.io.FileNotFoundException
import java.nio.charset.StandardCharsets

internal fun MockWebServer.enqueueFileResponse(fileName: String, code: Int) {
    val classLoader = javaClass.classLoader
    if (classLoader != null) {
        val inputStream = classLoader.getResourceAsStream("api_response/$fileName")

        inputStream?.source()?.buffer()?.let { source ->
            val body = source.readString(StandardCharsets.UTF_8)
            val mockResponse = MockResponse()
                .setResponseCode(code)
                .setBody(body)

            enqueue(mockResponse)
        } ?: throw FileNotFoundException("Could not find the specified file: $fileName")
    } else {
        throw IllegalStateException(
            """Classloader is null. Can't open an InputStream for the specified file: $fileName without it."""
        )
    }
}